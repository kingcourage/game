package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.JOIN_ROOM_RESPONSE;

@Data
public class JoinRoomResponsePacket extends Packet {
    private String roomId;
    private String userName;
    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return JOIN_ROOM_RESPONSE;
    }
}
