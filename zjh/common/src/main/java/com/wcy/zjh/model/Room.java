package com.wcy.zjh.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Room {
    private String roomName;
    public Room(String roomId) {
        this.roomId = roomId;
    }
    private String roomId;


    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
