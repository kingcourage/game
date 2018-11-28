package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.EXIT_ROOM_REQUEST;

@Data
public class ExitRoomRequestPacket extends Packet {
    String roomId;
    @Override
    public Byte getCommand() {
        return EXIT_ROOM_REQUEST;
    }
}
