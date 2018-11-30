package com.wcy.wechat.handler;

import com.wcy.netty.client.NettyClient;
import com.wcy.netty.protocol.request.JoinRoomRequestPacket;
import com.wcy.netty.protocol.request.LoginRequestPacket;
import com.wcy.wechat.common.SendMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ZjhHandler implements WxMpMessageHandler, WxMpMessageMatcher {
  @Autowired
  @Lazy
  private WxMpServiceHttpClientImpl wxMpServiceHttpClient;

  private static Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
  private Pattern pattern = Pattern.compile("\\d+");

  @Override
  public boolean match(WxMpXmlMessage message) {
    return isUserWantGuess(message) || isUserAnswering(message) || isJoinroom(message);
  }

  private boolean isUserWantGuess(WxMpXmlMessage message) {
    return "炸金花".equals(message.getContent());
  }
  private boolean isJoinroom(WxMpXmlMessage message) {
    return "加入房间".equals(message.getContent());
  }

  private boolean isUserAnswering(WxMpXmlMessage message) {
    return this.pattern.matcher(message.getContent()).matches();
  }

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) throws WxErrorException {

    if (isUserWantGuess(wxMessage)) {
      //当第一次发消息，初始化客户端
      if(!userChannelMap.containsKey(wxMessage.getFromUser())){
        NettyClient nettyClient = new NettyClient();
        try {
          nettyClient.start();
          while (true){
            //一直等待连接成功
            if(nettyClient.getChannel() != null){
              log.info("连接服务器成功");
              break;
            }
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        String lang = "zh_CN"; //语言
        WxMpUser user = wxMpServiceHttpClient.getUserService().userInfo(wxMessage.getFromUser(),lang);
        loginRequestPacket.setUserName(user.getNickname());
        nettyClient.getChannel().writeAndFlush(loginRequestPacket);
        log.info("发送登录请求");
        userChannelMap.put(wxMessage.getFromUser(),nettyClient.getChannel());
        SendMessage.channelUserMap.put(nettyClient.getChannel().toString(),wxMessage.getFromUser());
      }

    }

    if (isUserAnswering(wxMessage)) {
      giveHint(wxMessage, wxMpService, sessionManager);
    }

    if(isJoinroom(wxMessage)){
      letsGo(wxMessage, wxMpService, sessionManager);
    }
    return null;

  }

  protected void letsGo(WxMpXmlMessage wxMessage, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
    WxSession session = sessionManager.getSession(wxMessage.getFromUser());

    if (session.getAttribute("zjhing") == null) {
        log.info("收到消息{}",wxMessage.getContent());

      String lang = "zh_CN"; //语言
      WxMpUser user = wxMpServiceHttpClient.getUserService().userInfo(wxMessage.getFromUser(),lang);
      //进入房间
      JoinRoomRequestPacket joinRoomRequestPacket = new JoinRoomRequestPacket();
      joinRoomRequestPacket.setRoomName(user.getNickname()+"的房间");
      userChannelMap.get(wxMessage.getFromUser()).writeAndFlush(joinRoomRequestPacket);
    }

    session.setAttribute("zjhing", Boolean.TRUE);
  }


  protected void giveHint(WxMpXmlMessage wxMessage, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

    WxSession session = sessionManager.getSession(wxMessage.getFromUser());

    if (session.getAttribute("zjhing") == null) {
      return;
    }
    boolean guessing = (Boolean) session.getAttribute("zjhing");
    if (!guessing) {
      return;
    }

    int answer = (Integer) session.getAttribute("number");
    int guessNumber = Integer.valueOf(wxMessage.getContent());
    if (guessNumber < answer) {
      WxMpKefuMessage m = WxMpKefuMessage
        .TEXT()
        .toUser(wxMessage.getFromUser())
        .content("小了")
        .build();
      wxMpService.getKefuService().sendKefuMessage(m);

    } else if (guessNumber > answer) {
      WxMpKefuMessage m = WxMpKefuMessage
        .TEXT()
        .toUser(wxMessage.getFromUser())
        .content("大了")
        .build();
      wxMpService.getKefuService().sendKefuMessage(m);
    } else {
      WxMpKefuMessage m = WxMpKefuMessage
        .TEXT()
        .toUser(wxMessage.getFromUser())
        .content("Bingo!")
        .build();
      session.removeAttribute("zjhing");
      wxMpService.getKefuService().sendKefuMessage(m);
    }

  }
}
