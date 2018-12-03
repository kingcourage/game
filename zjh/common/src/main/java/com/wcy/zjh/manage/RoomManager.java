package com.wcy.zjh.manage;

import com.wcy.zjh.exception.BusiException;
import com.wcy.zjh.model.Game;
import com.wcy.zjh.model.Player;
import com.wcy.zjh.model.Room;
import io.netty.channel.group.ChannelGroup;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RoomManager {
    public static RoomManager INSTRANCE = new RoomManager();
    private  Map<String, Room> roomMap;
    private  Map<String, List<Player>> roomUserMap;
    private  Map<String,ChannelGroup> roomChannelGroup;
    private RoomManager(){
        roomMap = new ConcurrentHashMap();
        roomUserMap = new ConcurrentHashMap<>();
        roomChannelGroup = new HashMap<>();
        ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
        cacheThreadPool.execute(()->{
            checkRoomUserCounZero();
        });
    }


    /**
     * 新建房间
     * @param room
     * @return
     */
    public  void addRoom(Room room){
        roomMap.put(room.getRoomId(),room);
    }

    /**
     * 删除房间
     * @param roomId
     * @return
     */
    public  void removeRoom(String roomId){
        roomMap.remove(roomId);
    }

    /**
     * 加入房间
     * @param roomId
     * @param player
     */
    public  void joinRoom(String roomId, Player player){
        Room room = roomMap.get(roomId);
        if(room == null){
            throw new BusiException("房间不存在");
        }
        //保存房间和用户的关系
        if(roomUserMap.containsKey(roomId)){
            roomUserMap.get(roomId).add(player);
        }else{
            List<Player> playerList = new ArrayList<>();
            playerList.add(player);
            roomUserMap.put(roomId,playerList);
        }
    }

    /**
     * 获取已有的房间列表
     */
    public  List<Room> getRoomList(){
        return roomMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 从房间踢出玩家
     */
    public  void removeUser(String roomId,String userId){
        List<Player> playerList = roomUserMap.get(roomId);
        if(!CollectionUtils.isEmpty(playerList)) {
            for (Player player : playerList) {
                if (userId.equals(player.getUserId())) ;
                playerList.remove(player);
                break;
            }
        }
    }

    /**
     * 获取房间的玩家列表
     * @return
     */
    public  List<Player> getPlayerList(String roomId){
        return  roomUserMap.get(roomId);
    }

    public  void addChannelGroup(String roomId,ChannelGroup channelGroup){
        roomChannelGroup.put(roomId,channelGroup);
    }

    public  ChannelGroup getChannelGroup(String roomId){
      return  roomChannelGroup.get(roomId);
    }

    public  void checkRoomUserCounZero() {
        //启动房间回收检测
        while (true) {
            if(roomMap.keySet() != null && roomMap.keySet().size()>0){
                roomMap.keySet().stream().forEach(roomId -> {
                    if (getPlayerList(roomId) == null || getPlayerList(roomId).size() < 1) {
                        //系统回收房间
                        Room room = roomMap.get(roomId);
                        roomMap.remove(roomId);
                        System.out.println("系统回收房间" + room);
                    }
                });
            }
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
