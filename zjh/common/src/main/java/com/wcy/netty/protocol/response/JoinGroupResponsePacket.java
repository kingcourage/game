package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.JOIN_GROUP_RESPONSE;

@Data
public class JoinGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;
    @Override
    public Byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }
}
