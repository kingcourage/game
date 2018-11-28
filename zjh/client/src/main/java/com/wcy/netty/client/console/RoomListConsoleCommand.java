package com.wcy.netty.client.console;

import com.wcy.netty.protocol.request.RoomListRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class RoomListConsoleCommand implements ConsoleCommand{
    @Override
    public void exec(Scanner scanner, Channel channel) {
        RoomListRequestPacket roomListRequestPacket = new RoomListRequestPacket();
        channel.writeAndFlush(roomListRequestPacket);
    }

}
