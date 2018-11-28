package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.QUIT_GROUP_REQUEST;

@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupId;
    @Override
    public Byte getCommand() {
        return QUIT_GROUP_REQUEST;
    }
}
