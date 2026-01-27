package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.repository.projection.RoomWithDistance;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomGeoRepository {
    Flux<RoomWithDistance> findNearest(double lat, double lon, int radiusMeters, int page, int size, Criteria extraCriteria);
    Mono<Long> countNearest(double lat, double lon, int radiusMeters, Criteria extraCriteria);
}