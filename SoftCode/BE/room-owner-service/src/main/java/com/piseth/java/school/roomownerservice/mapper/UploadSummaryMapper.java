package com.piseth.java.school.roomownerservice.mapper;

import org.mapstruct.Mapper;

import com.piseth.java.school.roomownerservice.dto.UploadSummary;
import com.piseth.java.school.roomownerservice.util.ImportAccumulator;

@Mapper(componentModel = "spring")
public interface UploadSummaryMapper {

    UploadSummary toUploadSummary(ImportAccumulator accumulator);
}