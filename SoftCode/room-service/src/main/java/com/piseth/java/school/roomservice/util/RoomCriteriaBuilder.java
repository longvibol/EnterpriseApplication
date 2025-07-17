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
		
		Query query = new Query(criteria);
		
		return query;
	}
}
