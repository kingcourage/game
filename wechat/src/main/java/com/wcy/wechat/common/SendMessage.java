package com.wcy.wechat.common;

import com.wcy.netty.client.msg.ClientMsgManager;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class SendMessage{
   @Autowired
   private WxMpServiceHttpClientImpl wxMpServiceHttpClient;

   @Autowired
   private ExecutorService cachedThreadPool;

   @PostConstruct
   public void init(){
      log.info("启动发消息线程");
      cachedThreadPool.execute(()->{
         run();
      });
   }

   public static Map<String,String> channelUserMap = new ConcurrentHashMap<>();

   public void run() {
      while (true){
         if(channelUserMap.size() != 0){
            for(String channelId : channelUserMap.keySet()){
               String msg = ClientMsgManager.getMsg(channelId);
               if(StringUtils.isNotBlank(msg)){
                  WxMpKefuMessage m = WxMpKefuMessage
                          .TEXT()
                          .toUser(channelUserMap.get(channelId))
                          .content(msg)
                          .build();
                  try {
                     wxMpServiceHttpClient.getKefuService().sendKefuMessage(m);
                  } catch (WxErrorException e) {
                     e.printStackTrace();
                  }
               }

            }
         }
      }
   }
}
