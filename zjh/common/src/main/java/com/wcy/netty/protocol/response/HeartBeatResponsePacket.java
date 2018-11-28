package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;

import static com.wcy.netty.protocol.command.Command.HEARTBEAT_RESPONSE;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
