package com.piseth.java.school.roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoomFilterDTO {
	@Schema(description = "Filter by floor number", example = "3")
	private Integer floor;
	private String name;
	private String type;
	private Double price;
	private Double priceMin;
	private Double priceMax;
	private String priceOp;
	
	private int size = 10;
	private int page = 0;
}
