package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import com.wcy.zjh.model.Room;
import lombok.Data;

import java.util.List;

import static com.wcy.netty.protocol.command.Command.ROOM_LIST_RESPONSE;

@Data
public class RoomListResponsePacket extends Packet {
    private boolean success;
    private String reason;
    private List<Room> roomList;

    @Override
    public Byte getCommand() {
        return ROOM_LIST_RESPONSE;
    }
}
