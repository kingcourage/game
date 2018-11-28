package com.wcy.netty.protocol.request;


import com.wcy.netty.protocol.Packet;
import lombok.Data;

import static com.wcy.netty.protocol.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
