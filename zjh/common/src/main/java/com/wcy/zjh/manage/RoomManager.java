package com.wcy.zjh.manage;

import com.wcy.zjh.exception.BusiException;
import com.wcy.zjh.model.Player;
import com.wcy.zjh.model.Room;
import io.netty.channel.group.ChannelGroup;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RoomManager {
    private static Map<String, Room> roomMap = new ConcurrentHashMap();
    private static Map<String, Set<Player>> roomUserMap = new ConcurrentHashMap<>();
    private static Map<String,ChannelGroup> roomChannelGroup = new HashMap<>();
    /**
     * 新建房间
     * @param room
     * @return
     */
    public static void addRoom(Room room){
        roomMap.put(room.getRoomId(),room);
    }

    /**
     * 删除房间
     * @param roomId
     * @return
     */
    public static void removeRoom(String roomId){
        roomMap.remove(roomId);
    }

    /**
     * 加入房间
     * @param roomId
     * @param player
     */
    public static void joinRoom(String roomId, Player player){
        Room room = roomMap.get(roomId);
        if(room == null){
            throw new BusiException("房间不存在");
        }
        //保存房间和用户的关系
        if(roomUserMap.containsKey(roomId)){
            roomUserMap.get(roomId).add(player);
        }else{
            Set<Player> playerList = new HashSet<>();
            playerList.add(player);
            roomUserMap.put(roomId,playerList);
        }
    }

    /**
     * 获取已有的房间列表
     */
    public static List<Room> getRoomList(){
        return roomMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 从房间踢出玩家
     */
    public static void removeUser(String roomId,String userId){
        Set<Player> playerSet = roomUserMap.get(roomId);
        if(!CollectionUtils.isEmpty(playerSet)) {
            for (Player player : playerSet) {
                if (userId.equals(player.getUserId())) ;
                playerSet.remove(player);
                break;
            }
        }
    }

    /**
     * 获取房间的玩家列表
     * @return
     */
    public static Set<Player> getPlayerList(String roomId){
        return  roomUserMap.get(roomId);
    }

    public static void addChannelGroup(String roomId,ChannelGroup channelGroup){
        roomChannelGroup.put(roomId,channelGroup);
    }

    public static ChannelGroup getChannelGroup(String roomId){
      return  roomChannelGroup.get(roomId);
    }
}
