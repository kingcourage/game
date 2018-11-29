package com.wcy.netty.client.msg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientMsgManager {
    private static Map<String, ConcurrentLinkedQueue<String>> clientMsgMap = new ConcurrentHashMap();

    public static void putMsg(String channelId,String msg){
        if(clientMsgMap.get(channelId) != null){
            clientMsgMap.get(channelId).add(msg);
        }else{
            ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
            concurrentLinkedQueue.add(msg);
            clientMsgMap.put(channelId,concurrentLinkedQueue);
        }
    }

    public static String getMsg(String channelId){
        String msg = "";
        if(clientMsgMap.get(channelId) != null){
           msg = clientMsgMap.get(channelId).poll();
        }
      return msg;
    }
}
