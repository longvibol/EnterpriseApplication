package com.piseth.java.school.roomservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.NearbyRoomFilterDTO;
import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.exception.RoomNotFoundException;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;
import com.piseth.java.school.roomservice.repository.RoomGeoRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.repository.projection.RoomWithDistance;
import com.piseth.java.school.roomservice.service.RoomService;
import com.piseth.java.school.roomservice.util.RoomCriteriaBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService{
	
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;
	private final RoomCustomRepository roomCustomRepository;
	private final RoomGeoRepository roomGeoRepository;
	
	@Value("${storage.publicBaseUrl}")
	private String publicBaseUrl;

	

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.info("Retreiving room with ID: {}", id);
		return roomRepository.findById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnNext(room -> log.info("Room received : {}", room))
				//.map(roomMapper::toRoomDTO);
				.flatMap(this::toResponse);
				
	}

	@Override
	public Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO filterDTO) {
		Criteria criteria = RoomCriteriaBuilder.build(filterDTO);
		
		Mono<Long> countMono = roomCustomRepository.coundByFilter(new Query(criteria));
		
		Query query = new Query(criteria)
				.skip((long) filterDTO.getPage() * filterDTO.getSize())
				.limit(filterDTO.getSize());
		
		query.with(RoomCriteriaBuilder.sort(filterDTO));
		
		Flux<RoomDTO> contentFlux = roomCustomRepository.findByFilter(query)
										.map(roomMapper::toRoomDTO);
		
		return Mono.zip(countMono, contentFlux.collectList())
			.map(tuple ->{
				long total = tuple.getT1();
				List<RoomDTO> content = tuple.getT2(); 
				int totalPages = (int) Math.ceil((double)total/ filterDTO.getSize());
				return new PageDTO<>(filterDTO.getPage(), filterDTO.getSize(),total,totalPages, content);
			});
		
	}
	
	@Override
	public Flux<RoomDTO> getRoomsByIds(List<String> ids) {
	    if (ids == null || ids.isEmpty()) {
	        return Flux.empty();
	    }

	    return roomRepository.findAllById(ids)
	            .map(roomMapper::toRoomDTO);
	}
	/*
	private Mono<RoomDTO> toResponseWithUrls(Room room) {
		RoomDTO resp = roomMapper.toRoomDTO(room);
	    List<String> keys = room.getPhotoObjectKeys() == null ? List.of() : room.getPhotoObjectKeys();

	    return Flux.fromIterable(keys)
	        .map(key -> publicBaseUrl + "/room-media/" + key) // if bucket is public
	        // or presignedGetUrl if visitor service has MinIO creds
	        .collectList()
	        .map(urls -> {
	            resp.setPhotoUrls(urls);
	            return resp;
	        });
	}
*/
	@Override


    public Mono<PageDTO<RoomDTO>> getNearestRooms(NearbyRoomFilterDTO filter) {

        int page = filter.getPage() == null ? 0 : filter.getPage();
        int size = filter.getSize() == null ? 20 : filter.getSize();

        int radiusMeters = filter.getRadiusMeters() == null ? 3000 : filter.getRadiusMeters();

        
        
        Criteria extraCriteria = null;
        if (filter.getOtherFilters() != null) {
            extraCriteria = RoomCriteriaBuilder.build(filter.getOtherFilters());
        }

        

       //RoomFilterDTO merged = mergedFilter(filter);

     // build criteria only when there is at least 1 filter set
//     Criteria extraCriteria = null;
//    if (merged != null) {
//         extraCriteria = RoomCriteriaBuilder.build(merged);
//     }

        Mono<Long> countMono = roomGeoRepository.countNearest(
                filter.getLat(),
                filter.getLon(),
                radiusMeters,
                extraCriteria
        );


        Flux<RoomDTO> contentFlux = roomGeoRepository.findNearest(
                        filter.getLat(),
                        filter.getLon(),
                        radiusMeters,
                        page,
                        size,
                        extraCriteria
                )
                .flatMap(this::toRoomDtoWithDistanceAndUrls);

        return Mono.zip(countMono, contentFlux.collectList())
                .map(tuple -> {
                    long total = tuple.getT1();
                    List<RoomDTO> content = tuple.getT2();


                    int totalPages = (int) Math.ceil((double) total / size);


                    return new PageDTO<>(page, size, total, totalPages, content);


                });


    }


	


	private Mono<RoomDTO> toRoomDtoWithDistanceAndUrls(RoomWithDistance row) {
        if (row == null || row.getRoom() == null) {
            return Mono.empty();
        }
        Room room = row.getRoom();
        RoomDTO dto = roomMapper.toRoomDTO(room);
        dto.setDistanceMeters(row.getDistanceMeters());

        return buildPhotoUrls(dto, room);
    }


	


	private Mono<RoomDTO> buildPhotoUrls(RoomDTO resp, Room room) {
        List<String> keys = room.getPhotoObjectKeys() == null ? List.of() : room.getPhotoObjectKeys();

        return Flux.fromIterable(keys)
                .map(key -> publicBaseUrl + "/room-media/" + key)
                .collectList()
                .map(urls -> {
                    resp.setPhotoUrls(urls);
                    return resp;
                });


    }


	


	private Mono<RoomDTO> toResponse(Room room) {


	    RoomDTO dto = roomMapper.toRoomDTO(room);


	    return buildPhotoUrls(dto, room);


	}



/*

	private RoomFilterDTO mergedFilter(NearbyRoomFilterDTO filter) {

	    RoomFilterDTO f = filter.getOtherFilters() == null ? new RoomFilterDTO() : filter.getOtherFilters();

	    if(filter.getPriceMin() != null) {
	    	f.setPriceMin(filter.getPriceMin());
	    }
	    
	    if(filter.getPriceMax() != null) {
	    	f.setPriceMax(filter.getPriceMax());
	    }
	    
	    
	    if (filter.getRoomType() != null) {
	        f.setRoomType(filter.getRoomType());

	    }


	    if (filter.getPropertyType() != null) {


	        f.setPropertyType(filter.getPropertyType());


	    }


	    if (filter.getHasWiFi() != null) {


	        f.setHasWiFi(filter.getHasWiFi());


	    }
	   

	    return f;
	}
*/

	

}