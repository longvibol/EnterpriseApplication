package com.piseth.java.school.roomownerservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebInputException;

import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.service.RoomMediaService;
import com.piseth.java.school.roomownerservice.service.security.CurrentOwnerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomMediaController {

    private final RoomMediaService roomMediaService;
    private final CurrentOwnerService currentOwnerService;

    @PostMapping(value = "/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<RoomResponse> uploadPhotos(@PathVariable final String id,
                                          @RequestPart("files") final Flux<FilePart> files) {

        return currentOwnerService.getCurrentOwnerId()
                .flatMap(ownerId -> roomMediaService.uploadRoomPhotos(id, ownerId, files));
    }

    @GetMapping("/{id}/photos")
    public Mono<RoomResponse> getPhotos(@PathVariable final String id) {
        return currentOwnerService.getCurrentOwnerId()
                .flatMap(ownerId -> roomMediaService.getRoomWithPhotoUrls(id, ownerId));
    }

    @DeleteMapping("/{id}/photos")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RoomResponse> deletePhoto(@PathVariable final String id,
                                         @RequestParam final String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return Mono.error(new ServerWebInputException("objectKey is required"));
        }
        return currentOwnerService.getCurrentOwnerId()
                .flatMap(ownerId -> roomMediaService.deleteRoomPhoto(id, ownerId, objectKey));
    }
}