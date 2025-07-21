package com.piseth.java.school.roomservice.util;

import java.util.Objects;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

public class RoomCriteriaBuilder {
	
	// we want to get the Query that we create 
	// so in this class need to have Query return 
	
	// We thing what can we get to build Query ==> RoomFilterDTO that we just create 
	
	public static Query build(RoomFilterDTO filter) {
		// criteria is our condition 
		Criteria criteria = new Criteria();
		
		// we check one they input from request parameter  
		if(Objects.nonNull(filter.getName())) {
			criteria.and("name").is(filter.getName());
		}		
		
		if(filter.getFloor()!=null) {
			//we check if it have value mean they want to filter by floor 
			criteria.and("attributes.floor").is(filter.getFloor());			
		}
		
		if(Objects.nonNull(filter.getPrice()) && Objects.nonNull(filter.getPriceOp())){
			switch(filter.getPriceOp()) {
			case "lt" -> criteria.and("attributes.price").lt(filter.getPrice());
			case "lte" -> criteria.and("attributes.price").lte(filter.getPrice());
			case "gt" -> criteria.and("attributes.price").gt(filter.getPrice());
			case "gte" -> criteria.and("attributes.price").gte(filter.getPrice());
			case "eq" -> criteria.and("attributes.price").is(filter.getPrice());			
			}
		}else if(Objects.nonNull(filter.getPriceMin()) && Objects.nonNull(filter.getPriceMax())) {
			criteria.and("attributes.price").gte(filter.getPriceMin()).lte(filter.getPriceMax());			
			// Max > price > min
		}
		
		Query query = new Query(criteria);
				
		
		return query;
	}
}
