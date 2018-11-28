package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.ROOM_USER_REQUEST;

@Data
public class RoomUserRequestPacket extends Packet {
    private String roomId;

    @Override
    public Byte getCommand() {
        return ROOM_USER_REQUEST;
    }
}
