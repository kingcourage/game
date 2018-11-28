package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.EXIT_ROOM_RESPONSE;

@Data
public class ExitRoomResponsePacket extends Packet {
    private boolean success;
    private String reason;
    @Override
    public Byte getCommand() {
        return EXIT_ROOM_RESPONSE;
    }
}
