package com.piseth.java.school.roomservice.repository.projection;

import com.piseth.java.school.roomservice.domain.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomWithDistance {
    private Room room;
    private Double distanceMeters;
}