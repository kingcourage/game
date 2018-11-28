package com.wcy.netty.client.console;

import com.wcy.netty.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCommandManager implements ConsoleCommand{

    private Map<String,ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager(){
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("joinRoom", new JoinRoomConsoleCommand());
        consoleCommandMap.put("exitRoom",new ExitRoomConsoleCommand());
        consoleCommandMap.put("roomList",new RoomListConsoleCommand());
        consoleCommandMap.put("roomUser",new RoomUserConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        String command = scanner.next();

        if(!SessionUtil.hasLogin(channel)){
            return;
        }
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if(consoleCommand != null){
            consoleCommand.exec(scanner,channel);
        }else{
            System.out.println("无法识别["+command+"]指令，请重新输入！");
        }

    }
}
