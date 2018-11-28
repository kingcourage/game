package com.wcy.netty.protocol.request;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.LOGOUT_REQUEST;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
