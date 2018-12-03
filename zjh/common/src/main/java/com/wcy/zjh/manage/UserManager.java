package com.wcy.zjh.manage;

import com.wcy.zjh.model.Player;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<String,String> userRoomMap = new HashMap<>();

    public static void joinRoom(Player player, String roomId){
        //加入之前，看是否已经在房间中，在则退出之前的房间
        if(userRoomMap.get(player.getUserId()) != null){
            exitRoom(player.getUserId());
        }
        userRoomMap.put(player.getUserId(),roomId);
        RoomManager.INSTRANCE.joinRoom(roomId,player);
    }

    public static String getRoomId(String userId){
        return userRoomMap.get(userId);
    }

    /**
     * 退出房间
     * @param userId
     */
    public static void exitRoom(String userId){
        String roomId = userRoomMap.get(userId);
        RoomManager.INSTRANCE.removeUser(roomId,userId);
    }
}
