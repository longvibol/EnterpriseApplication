package com.piseth.java.school.roomownerservice.storage;

import java.nio.file.Path;
import java.time.Duration;

import reactor.core.publisher.Mono;

public interface ObjectStorage { // SOLID principle : "O" Open Extension, Closed modification
	// it is command not Query 
	Mono<Void> putObject(String objectKey, String contentType, Path filePath);

    Mono<Void> removeObject(String objectKey);

    Mono<String> presignedGetUrl(String objectKey, Duration expiry);
}