package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piseth.java.school.roomservice.domain.SkippedRoomDocument;

public interface SkippedRoomDocumentRepository extends ReactiveMongoRepository<SkippedRoomDocument, String>{

}
