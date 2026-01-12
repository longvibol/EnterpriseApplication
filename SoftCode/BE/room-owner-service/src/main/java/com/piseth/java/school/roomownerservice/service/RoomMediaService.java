package com.piseth.java.school.roomownerservice.service;

import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.roomownerservice.dto.RoomResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomMediaService {

    Mono<RoomResponse> uploadRoomPhotos(String roomId, String ownerId, Flux<FilePart> files);

    Mono<RoomResponse> getRoomWithPhotoUrls(String roomId, String ownerId);

    Mono<RoomResponse> deleteRoomPhoto(String roomId, String ownerId, String objectKey);
}