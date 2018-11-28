package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {
    private String userId;

    private String userName;

    private boolean success;

    private String reason;
    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
