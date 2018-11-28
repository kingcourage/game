package com.wcy.netty.protocol.response;

import com.wcy.netty.protocol.Packet;
import com.wcy.zjh.model.Player;
import lombok.Data;

import java.util.Set;

import static com.wcy.netty.protocol.command.Command.ROOM_USER_RESPONSE;

@Data
public class RoomUserResponsePacket extends Packet {

    private boolean success;
    private String message;
    private Set<Player> playerList;

    @Override
    public Byte getCommand() {
        return ROOM_USER_RESPONSE;
    }
}
