package com.piseth.java.school.roomservice.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.util.RoomCriteriaBuilder;

import nl.altindag.log.LogCaptor;

class RoomCriterialBuilderTest {

	// Empty input
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
		assertThat(jsonConvert).contains("name", "Luxury Room");

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
		assertThat(jsonConvert).contains("floor", "2");
	}

	// price

	@ParameterizedTest(name = "Should build criteria with op {0} and expect keyword {1}")
	@CsvSource({ "lt, $lt", // input = "lt", expectedOutput = "$lt"
			"lte, $lte", // input = "lte", expectedOutput = "$lte"
			"gt, $gt", "gte, $gte", "eq, 60" })
	void shouldAddPriceCriteria_withVariousOperations(String op, String expectedJsonFragment) {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setPrice(60d);
		filter.setPriceOp(op);

		// When
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		String json = criteria.getCriteriaObject().toJson();

		// Then
		assertThat(json).contains("price").contains(expectedJsonFragment);
	}

	@Test
	void testInvalidPriceOperatorLogsWarning() {
		LogCaptor logCaptor = LogCaptor.forClass(RoomCriteriaBuilder.class);

		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setPrice(150.0);
		filter.setPriceOp("INVALID_OP");

		RoomCriteriaBuilder.build(filter);

		assertTrue(
				logCaptor.getWarnLogs().stream().anyMatch(log -> log.contains("Invalid price operator: INVALID_OP")));
	}

	@Test
	void shouldAddPriceMin_PriceMax() {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setPriceMin(50d);
		filter.setPriceMax(100d);

		// When
		Criteria criteria = RoomCriteriaBuilder.build(filter);
		String jsonConvert = criteria.getCriteriaObject().toJson();

		assertThat(jsonConvert)
        .contains("price")
        .contains("$gte")
        .contains("50")
        .contains("$lte")
        .contains("100");
	}
	
	//Test: price set, priceOp null
	@Test
	void shouldSkipPriceCriteria_whenPriceOpIsNull() {
	    RoomFilterDTO filter = new RoomFilterDTO();
	    filter.setPrice(60d); // valid
	    filter.setPriceOp(null); // missing operator

	    Criteria criteria = RoomCriteriaBuilder.build(filter);
	    String json = criteria.getCriteriaObject().toJson();

	    assertThat(json).doesNotContain("price");
	}
	
	//Test: price null, priceOp set
	@Test
	void shouldSkipPriceCriteria_whenPriceIsNull() {
	    RoomFilterDTO filter = new RoomFilterDTO();
	    filter.setPrice(null); // missing price
	    filter.setPriceOp("lt"); // valid op

	    Criteria criteria = RoomCriteriaBuilder.build(filter);
	    String json = criteria.getCriteriaObject().toJson();

	    assertThat(json).doesNotContain("price");
	}
	
	//Test: priceMin set, priceMax null
	@Test
	void shouldSkipPriceRange_whenPriceMaxIsNull() {
	    RoomFilterDTO filter = new RoomFilterDTO();
	    filter.setPriceMin(50d);
	    filter.setPriceMax(null); // missing max

	    Criteria criteria = RoomCriteriaBuilder.build(filter);
	    String json = criteria.getCriteriaObject().toJson();

	    assertThat(json).doesNotContain("price");
	}
	
	//Test: priceMin null, priceMax set
	@Test
	void shouldSkipPriceRange_whenPriceMinIsNull() {
	    RoomFilterDTO filter = new RoomFilterDTO();
	    filter.setPriceMin(null); // missing min
	    filter.setPriceMax(100d);

	    Criteria criteria = RoomCriteriaBuilder.build(filter);
	    String json = criteria.getCriteriaObject().toJson();

	    assertThat(json).doesNotContain("price");
	}

	// Sort function testing

	@Test
	void sort_withValidFieldASC() {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setDirection("asc");
		filter.setSortBy("price"); // sort by asc

		// When
		Sort sort = RoomCriteriaBuilder.sort(filter);
		// When
		assertThat(sort.getOrderFor("attributes.price")).isNotNull();
		assertThat(sort.getOrderFor("attributes.price").getDirection()).isEqualTo(Sort.Direction.ASC);
	}

	@Test
	void sort_withDefaultValue() {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();

		// When
		Sort sort = RoomCriteriaBuilder.sort(filter);
		// When
		assertThat(sort.getOrderFor("name")).isNotNull();
		assertThat(sort.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
	}
	
	@Test
	void sort_withInvalidField_throwException() {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();
		
		filter.setDirection("asc");
		filter.setSortBy("invalid_field_xyz"); // type is invalid field

		System.out.println("Trying to sort by: " + filter.getSortBy());
		
		// When
		assertThatThrownBy(() -> RoomCriteriaBuilder.sort(filter)).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Invalid sort field:");
	}

	@Test
	void sort_withValidFieldDESC() {
		// Given
		RoomFilterDTO filter = new RoomFilterDTO();
		filter.setDirection("desc");

		// When
		Sort sort = RoomCriteriaBuilder.sort(filter);
		// When
		assertThat(sort.getOrderFor("name")).isNotNull();
		assertThat(sort.getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
	}

}
