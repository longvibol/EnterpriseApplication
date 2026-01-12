package com.piseth.java.school.roomownerservice.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.piseth.java.school.roomownerservice.config.MinioProperties;
import com.piseth.java.school.roomownerservice.domain.Room;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.exception.RoomNotFoundException;
import com.piseth.java.school.roomownerservice.mapper.RoomMapper;
import com.piseth.java.school.roomownerservice.repository.RoomRepository;
import com.piseth.java.school.roomownerservice.service.RoomMediaService;
import com.piseth.java.school.roomownerservice.storage.ObjectStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomMediaServiceImpl implements RoomMediaService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final ObjectStorage objectStorage;
    private final MinioProperties props;

    @Override
    public Mono<RoomResponse> uploadRoomPhotos(final String roomId,
                                              final String ownerId,
                                              final Flux<FilePart> files) {

        return roomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new RoomNotFoundException(roomId)))
                .flatMap(room -> {
                    if (!Objects.equals(room.getOwnerId(), ownerId)) {
                        return Mono.error(new IllegalStateException("You do not own this room"));
                    }

                    int existing = room.getPhotoObjectKeys() == null ? 0 : room.getPhotoObjectKeys().size();
                    int maxFiles = props.maxFilesPerRoom() == null ? 10 : props.maxFilesPerRoom();

                    return files
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("files is required")))
                            .flatMap(file -> validateAndUploadOne(roomId, file), 3) // concurrency=3
                            .collectList() // Mono<List<String>>
                            .flatMap(uploadedKeys -> {
                                if (existing + uploadedKeys.size() > maxFiles) {
                                    // if exceeded, cleanup uploaded
                                    return Flux.fromIterable(uploadedKeys)
                                            .flatMap(objectStorage::removeObject)
                                            .then(Mono.error(new IllegalArgumentException("Too many photos. Max=" + maxFiles)));
                                }

                                List<String> merged = new ArrayList<>(
                                        room.getPhotoObjectKeys() == null ? List.of() : room.getPhotoObjectKeys()
                                );
                                merged.addAll(uploadedKeys);
                                room.setPhotoObjectKeys(merged);

                                return roomRepository.save(room)
                                        .onErrorResume(ex ->
                                                // compensation: DB failed → delete uploaded objects
                                                Flux.fromIterable(uploadedKeys)
                                                        .flatMap(objectStorage::removeObject)
                                                        .then(Mono.error(ex))
                                        );
                            })
                            .flatMap(saved -> toRoomResponseWithUrls(saved))
                            .doOnSuccess(r -> log.info("Uploaded photos roomId={}, count={}", roomId,
                                    r.getPhotoUrls() == null ? 0 : r.getPhotoUrls().size()))
                            .doOnError(ex -> log.error("Upload photos failed roomId={}, err={}", roomId, ex.getMessage(), ex));
                });
    }

    @Override
    public Mono<RoomResponse> getRoomWithPhotoUrls(final String roomId, final String ownerId) {
        return roomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new RoomNotFoundException(roomId)))
                .flatMap(room -> {
                    if (!Objects.equals(room.getOwnerId(), ownerId)) {
                        return Mono.error(new IllegalStateException("You do not own this room"));
                    }
                    return toRoomResponseWithUrls(room);
                });
    }

    @Override
    public Mono<RoomResponse> deleteRoomPhoto(final String roomId, final String ownerId, final String objectKey) {
        return roomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new RoomNotFoundException(roomId)))
                .flatMap(room -> {
                    if (!Objects.equals(room.getOwnerId(), ownerId)) {
                        return Mono.error(new IllegalStateException("You do not own this room"));
                    }

                    List<String> keys = room.getPhotoObjectKeys() == null ? new ArrayList<>() : new ArrayList<>(room.getPhotoObjectKeys());
                    boolean removed = keys.remove(objectKey);
                    if (!removed) {
                        return toRoomResponseWithUrls(room); // idempotent
                    }

                    room.setPhotoObjectKeys(keys);

                    return roomRepository.save(room)
                            .flatMap(saved -> objectStorage.removeObject(objectKey).thenReturn(saved))
                            .flatMap(this::toRoomResponseWithUrls);
                });
    }

    private Mono<String> validateAndUploadOne(final String roomId, final FilePart file) {
        String contentType = file.headers().getContentType() != null ? file.headers().getContentType().toString() : null;
        if (!StringUtils.hasText(contentType) || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return Mono.error(new IllegalArgumentException("Only jpg, png, webp are allowed"));
        }

        String ext = contentTypeToExt(contentType);
        String objectKey = "rooms/" + roomId + "/" + UUID.randomUUID() + ext;

        return writeToTempFile(file)
                .flatMap(tmp -> {
                    long maxBytes = props.maxFileSizeBytes() == null ? 5_000_000L : props.maxFileSizeBytes();
                    return Mono.fromCallable(() -> Files.size(tmp))
                            .flatMap(size -> {
                                if (size > maxBytes) {
                                    return Mono.error(new IllegalArgumentException("File too large. Max=" + maxBytes + " bytes"));
                                }
                                return objectStorage.putObject(objectKey, contentType, tmp).thenReturn(tmp);
                            })
                            .doFinally(sig -> safeDelete(tmp));
                })
                .thenReturn(objectKey);
    }

    private Mono<Path> writeToTempFile(final FilePart file) {
        return Mono.fromCallable(() -> Files.createTempFile("room-photo-", ".upload"))
                .flatMap(tmp -> file.transferTo(tmp).thenReturn(tmp));
    }

    private void safeDelete(final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            log.warn("Failed to delete temp file: {}", path);
        }
    }

    private String contentTypeToExt(final String contentType) {
        if ("image/jpeg".equals(contentType)) {
            return ".jpg";
        }
        if ("image/png".equals(contentType)) {
            return ".png";
        }
        if ("image/webp".equals(contentType)) {
            return ".webp";
        }
        return "";
    }

    private Mono<RoomResponse> toRoomResponseWithUrls(final Room room) {
        RoomResponse resp = roomMapper.toResponse(room);

        //List<String> keys = room.getPhotoObjectKeys() == null ? List.of() : room.getPhotoObjectKeys();
        
        List<String> keys = Optional.ofNullable(room.getPhotoObjectKeys())
        							.orElse(List.of());

        return Flux.fromIterable(keys)
                .flatMap(key -> objectStorage.presignedGetUrl(key, Duration.ofHours(6)), 5)
                .collectList()
                .map(urls -> {
                    // Use RoomResponse.photoUrls to show actual viewable URLs
                    resp.setPhotoUrls(urls);
                    return resp;
                });
    }
}