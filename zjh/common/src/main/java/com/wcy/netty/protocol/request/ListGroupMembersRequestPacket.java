package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.LIST_GROUP_MEMBERS_REQUEST;

@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBERS_REQUEST;
    }
}
