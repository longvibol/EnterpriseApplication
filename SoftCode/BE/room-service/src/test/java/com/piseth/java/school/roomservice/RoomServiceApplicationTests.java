package com.piseth.java.school.roomservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.piseth.java.school.roomservice.service.RoomService;

@SpringBootTest
class RoomServiceApplicationTests {

	@Autowired
    private RoomService roomService;

    @Test
    void contextLoads() {
        assertThat(roomService).isNotNull();
    }

}
