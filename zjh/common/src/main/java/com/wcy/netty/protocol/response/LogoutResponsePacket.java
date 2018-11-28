package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.LOGOUT_RESPONSE;

@Data
public class LogoutResponsePacket extends Packet {
    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGOUT_RESPONSE;
    }
}
