package com.wcy.netty.client.console;

import com.wcy.netty.protocol.request.RoomUserRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class RoomUserConsoleCommand implements ConsoleCommand{
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入房间号:");
        String roomId = scanner.next();
        RoomUserRequestPacket roomUserRequestPacket = new RoomUserRequestPacket();
        roomUserRequestPacket.setRoomId(roomId);
        channel.writeAndFlush(roomUserRequestPacket);
    }
}
