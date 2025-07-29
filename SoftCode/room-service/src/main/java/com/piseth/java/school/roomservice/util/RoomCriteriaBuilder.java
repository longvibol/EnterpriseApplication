package com.piseth.java.school.roomservice.util;

import static com.piseth.java.school.roomservice.util.RoomConstants.ALLOWED_SORT_FIELD;
import static com.piseth.java.school.roomservice.util.RoomConstants.ATT;
import static com.piseth.java.school.roomservice.util.RoomConstants.FIELD_FLOOR;
import static com.piseth.java.school.roomservice.util.RoomConstants.FIELD_NAME;
import static com.piseth.java.school.roomservice.util.RoomConstants.FIELD_PRICE;
import static com.piseth.java.school.roomservice.util.RoomConstants.OP_EQ;
import static com.piseth.java.school.roomservice.util.RoomConstants.OP_GT;
import static com.piseth.java.school.roomservice.util.RoomConstants.OP_GTE;
import static com.piseth.java.school.roomservice.util.RoomConstants.OP_LT;
import static com.piseth.java.school.roomservice.util.RoomConstants.OP_LTE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class RoomCriteriaBuilder {	

	private RoomCriteriaBuilder() {
		
	}

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
		
		Sort.Direction direction = "desc".equalsIgnoreCase(filter.getDirection()) 
				? Sort.Direction.DESC : Sort.Direction.ASC;		
		
		String sortField = Objects.nonNull(filter.getSortBy()) ? filter.getSortBy() : FIELD_NAME;
				
			if(!ALLOWED_SORT_FIELD.contains(sortField)) {
				throw new IllegalArgumentException("Invalid sort field: " + sortField);

			}				
		 
			if(!sortField.equals(FIELD_NAME)) {
				sortField = ATT + sortField;
			}
		
		return Sort.by(direction, sortField);

	}
	
		
}
