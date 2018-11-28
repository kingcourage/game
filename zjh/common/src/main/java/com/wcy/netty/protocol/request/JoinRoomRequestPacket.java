package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.JOIN_ROOM_REQUEST;


@Data
public class JoinRoomRequestPacket extends Packet {
    private String roomId;
    private String roomName;
    @Override
    public Byte getCommand() {
        return JOIN_ROOM_REQUEST;
    }
}
