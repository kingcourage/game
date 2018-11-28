package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.wcy.netty.protocol.command.Command.GROUP_MESSAGE_REQUEST;

@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {

    private String toGroupId;
    private String message;

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }
    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
