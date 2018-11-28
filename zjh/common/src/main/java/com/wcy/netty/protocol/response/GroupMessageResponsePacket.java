package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import com.wcy.netty.session.Session;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}
