package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.WX_MESSAGE_RESPONSE;

@Data
public class WxMessageResponsePacket extends Packet {

    private String fromUserName;

    private String fromUserId;

    private String message;

    @Override
    public Byte getCommand() {
        return WX_MESSAGE_RESPONSE;
    }
}
