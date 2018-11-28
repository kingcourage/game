package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;

import static com.wcy.netty.protocol.command.Command.ROOM_LIST_REQUEST;

public class RoomListRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return ROOM_LIST_REQUEST;
    }
}
