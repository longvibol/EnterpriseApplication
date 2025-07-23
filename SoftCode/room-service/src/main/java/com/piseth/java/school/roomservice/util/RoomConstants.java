package com.piseth.java.school.roomservice.util;

import java.util.List;

public final class RoomConstants {
	
	// class final can not extends 
	// close contractor : can not create object  
	
	private RoomConstants() {}
	
	public static final String ATT = "attributes.";
	
	public static final String FIELD_NAME = "name";
	public static final String FIELD_FLOOR = ATT+"floor";
	public static final String FIELD_PRICE = ATT+"price";
	
	//Price Operator 
	
	public static final String OP_LT = "lt";
	public static final String OP_LTE = "lte";
	public static final String OP_GT = "gt";
	public static final String OP_GTE = "gte";
	public static final String OP_EQ = "eq";
	
	
	// Sort allow- list	
	public static final List<String> ALLOWED_SORT_FIELD = List.of("name","price","floor");
	
	
	
	

}
