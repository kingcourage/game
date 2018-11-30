package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.WX_MESSAGE_REQUEST;

@Data
public class WxMessageRequestPacket extends Packet {

    private String fromUserName;

    private String fromUserId;

    private String message;

    @Override
    public Byte getCommand() {
        return WX_MESSAGE_REQUEST;
    }
}
