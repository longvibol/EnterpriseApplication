package com.piseth.java.school.roomservice.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.geoNear;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GeoNearOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.repository.projection.RoomWithDistance;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RoomGeoRepositoryImpl implements RoomGeoRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<RoomWithDistance> findNearest(
            double lat,
            double lon,
            int radiusMeters,
            int page,
            int size,
            Criteria extraCriteria) {

        Criteria baseCriteria = Criteria.where("deleted").is(false)
                .and("status").is("AVAILABLE");

        Criteria finalCriteria = (extraCriteria != null)
                ? new Criteria().andOperator(baseCriteria, extraCriteria)
                : baseCriteria;

        // GeoJSON uses [lon, lat]
        Point nearPoint = new Point(lon, lat);

        double radiusKm = radiusMeters / 1000.0;

        // IMPORTANT:
        // - Use metric-aware NearQuery so maxDistance is interpreted correctly
        // - distanceMultiplier(1000.0) converts km -> meters for returned "distanceMeters"
        NearQuery nearQuery = NearQuery.near(nearPoint, Metrics.KILOMETERS)
                .maxDistance(new Distance(radiusKm, Metrics.KILOMETERS))
                .spherical(true)
                .query(new Query(finalCriteria))
                .distanceMultiplier(1000.0); // km -> meters

        GeoNearOperation geoNear = geoNear(nearQuery, "distanceMeters");

        Aggregation agg = newAggregation(
                geoNear,
                project()
                        .and(Aggregation.ROOT).as("room")
                        .and("distanceMeters").as("distanceMeters"),
                sort(Sort.Direction.ASC, "distanceMeters"),
                skip((long) page * size),
                limit(size)
        );

        return mongoTemplate.aggregate(agg, Room.class, RoomWithDistance.class);
    }

    @Override
    public Mono<Long> countNearest(
            double lat,
            double lon,
            int radiusMeters,
            Criteria extraCriteria) {

        Criteria baseCriteria = Criteria.where("deleted").is(false)
                .and("status").is("AVAILABLE");

        Criteria finalCriteria = (extraCriteria != null)
                ? new Criteria().andOperator(baseCriteria, extraCriteria)
                : baseCriteria;

        Point nearPoint = new Point(lon, lat);

        double radiusKm = radiusMeters / 1000.0;

        NearQuery nearQuery = NearQuery.near(nearPoint, Metrics.KILOMETERS)
                .maxDistance(new Distance(radiusKm, Metrics.KILOMETERS))
                .spherical(true)
                .query(new Query(finalCriteria));

        GeoNearOperation geoNear = geoNear(nearQuery, "distanceMeters");

        Aggregation agg = newAggregation(
                geoNear,
                count().as("total")
        );

        return mongoTemplate.aggregate(agg, Room.class, Document.class)
                .next()
                .map(doc -> ((Number) doc.get("total")).longValue())
                .defaultIfEmpty(0L);
    }
}