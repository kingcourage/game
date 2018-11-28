package com.wcy.netty.client.console;

import com.wcy.netty.protocol.request.ExitRoomRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class ExitRoomConsoleCommand implements ConsoleCommand{
    @Override
    public void exec(Scanner scanner, Channel channel) {
        ExitRoomRequestPacket exitRoomRequestPacket = new ExitRoomRequestPacket();
        String roomId = scanner.next();
        exitRoomRequestPacket.setRoomId(roomId);
        channel.writeAndFlush(exitRoomRequestPacket);
    }
}
