package com.piseth.java.school.roomservice.util;

import static com.piseth.java.school.roomservice.util.RoomConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomCriteriaBuilder {

	// we want to get the Query that we create
	// so in this class need to have Query return
	// We thing what can we get to build Query ==> RoomFilterDTO that we just create

	public static Criteria build(RoomFilterDTO filter) {

		List<Criteria> criteria = new ArrayList<>();
  
		if (Objects.nonNull(filter.getName())) {
 
			criteria.add(Criteria.where(FIELD_NAME).is(filter.getName()));
		}

		if (filter.getFloor() != null) {

			criteria.add(Criteria.where(FIELD_FLOOR).is(filter.getFloor()));
		}

		if (Objects.nonNull(filter.getPrice()) && Objects.nonNull(filter.getPriceOp())) {
			
			switch (filter.getPriceOp()) {
			case OP_LT -> criteria.add(Criteria.where(FIELD_PRICE).lt(filter.getPrice()));
			case OP_LTE -> criteria.add(Criteria.where(FIELD_PRICE).lte(filter.getPrice()));
			case OP_GT -> criteria.add(Criteria.where(FIELD_PRICE).gt(filter.getPrice()));
			case OP_GTE -> criteria.add(Criteria.where(FIELD_PRICE).gte(filter.getPrice()));
			case OP_EQ -> criteria.add(Criteria.where(FIELD_PRICE).is(filter.getPrice()));
			default -> log.warn("Invalid price operator: {}", filter.getPriceOp());
			
			}
			
		} else if (Objects.nonNull(filter.getPriceMin()) && Objects.nonNull(filter.getPriceMax())) {
			criteria.add(Criteria.where(FIELD_PRICE).gte(filter.getPriceMin()).lte(filter.getPriceMax()));
		}

		return criteria.isEmpty() ? new Criteria() : new Criteria().andOperator(criteria.toArray(new Criteria[0]));
	}
		
	public static Sort sort(RoomFilterDTO filter) {
		
		// sort direction 
		
		/*
		Sort.Direction direction = Sort.Direction.ASC;		
		if("desc".equalsIgnoreCase(filter.getDirection())) {
			direction =Sort.Direction.DESC;
		}		
		*/		
		
		// We use Binary operator
		Sort.Direction direction = "desc".equalsIgnoreCase(filter.getDirection()) 
				? Sort.Direction.DESC : Sort.Direction.ASC;		
		
		// sort field 	
		// this mean we get what they input String sortField = filter.getSortBy();
		
		// get the name input form they typing
		String sortField = Objects.nonNull(filter.getSortBy()) ? filter.getSortBy() : FIELD_NAME;
				
			if(!ALLOWED_SORT_FIELD.contains(sortField)) {
				throw new IllegalArgumentException("Invalid sort fiels: " + sortField);
			}				
		
			if(!sortField.equals(FIELD_NAME)) {
				sortField = ATT + sortField;
			}
		
		return Sort.by(direction, sortField);

	}
	
		
}
