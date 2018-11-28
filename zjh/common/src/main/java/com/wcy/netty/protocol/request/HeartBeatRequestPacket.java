package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;

import static com.wcy.netty.protocol.command.Command.HEARTBEAT_REQUEST;

public class HeartBeatRequestPacket  extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
