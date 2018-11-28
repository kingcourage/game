package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.QUIT_GROUP_RESPONSE;

@Data
public class QuitGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String reason;
    @Override
    public Byte getCommand() {
        return QUIT_GROUP_RESPONSE;
    }
}
