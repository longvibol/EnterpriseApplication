package com.piseth.java.school.roomservice.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.util.RoomCriteriaBuilder;

public class RoomCriterialBuilderTest {
	
	//Empty input 
	
	@Test
	void shouldReturnEmptyCriteria_whenNoFilterProvided() {
		// Given 		
		RoomFilterDTO filter = new RoomFilterDTO();
		 
		// When 		
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		
		// Then
		assertThat(criteria.getCriteriaObject()).isEmpty();
		
	}
	
	// Name
	
	@Test
	void shouldAddNameCriteria_whenNameProvided() {
		// Given 		
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setName("Luxury Room");
		 
		// When 		
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		String jsonConvert = criteria.getCriteriaObject().toJson();
		
		// Then
		// We check Key and Value from Map 
		assertThat(jsonConvert).contains("name","Luxury Room");
		
	}
	
	// floor
	
	@Test
	void shouldAddFloorCriteria_whenFloorProvided() {
		// Given 		
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setFloor(2);
		 
		// When 		
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		String jsonConvert = criteria.getCriteriaObject().toJson();
		
		// Then
		// We check Key and Value from Map 
		assertThat(jsonConvert).contains("floor","2");		
	}
	
	// price
	
	@Test
	void shouldAddPriceCriteria_withOperationLT() {
		// Given 		
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setPrice(60d);
		filter.setPriceOp("lt");
		 
		// When 		
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		String jsonConvert = criteria.getCriteriaObject().toJson();
		
		// Then
		// We check Key and Value from Map 
		assertThat(jsonConvert).contains("price").contains("$lt");		
	}
	
 
}
