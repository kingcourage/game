package com.wcy.netty.client.console;

import com.alibaba.fastjson.JSONObject;
import com.wcy.netty.protocol.request.JoinRoomRequestPacket;
import com.wcy.zjh.manage.RoomManager;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class JoinRoomConsoleCommand implements ConsoleCommand{

    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinRoomRequestPacket joinRoomRequestPacket = new JoinRoomRequestPacket();
        System.out.println("输入要加入的房间id(#结束):");
        String roomId = scanner.next().replace("#","");
        joinRoomRequestPacket.setRoomId(roomId);
        if(StringUtils.isBlank(roomId)){
            System.out.println("输入房间名称");
            String roomName = scanner.next();
            joinRoomRequestPacket.setRoomName(roomName);
        }

        channel.writeAndFlush(joinRoomRequestPacket);
    }
}
