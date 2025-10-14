package com.piseth.java.school.roomownerservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piseth.java.school.roomownerservice.domain.SkippedRoomDocument;

public interface SkippedRoomDocumentRepository extends ReactiveMongoRepository<SkippedRoomDocument, String>{

}
