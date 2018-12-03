package com.wcy.wechat.handler;

import com.wcy.netty.client.NettyClient;
import com.wcy.netty.client.msg.ClientMsgManager;
import com.wcy.netty.protocol.command.PacketId;
import com.wcy.netty.protocol.request.JoinRoomRequestPacket;
import com.wcy.netty.protocol.request.LoginRequestPacket;
import com.wcy.netty.protocol.request.WxMessageRequestPacket;
import com.wcy.netty.util.SessionUtil;
import com.wcy.wechat.common.SendMessage;
import com.wcy.zjh.manage.UserManager;
import com.wcy.zjh.model.Game;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ZjhHandler implements WxMpMessageHandler, WxMpMessageMatcher {
    @Autowired
    @Lazy
    private WxMpServiceHttpClientImpl wxMpServiceHttpClient;


    private WxSessionManager wxSessionManager;
    private static Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    @Override
    public boolean match(WxMpXmlMessage message) {
        return isUserWantGuess(message) || isUserAnswering(message) || isInZjh(message);
    }

    private boolean isUserWantGuess(WxMpXmlMessage message) {
        return "炸金花".equals(message.getContent());
    }

    private boolean isInZjh(WxMpXmlMessage message) {
        WxSession session = wxSessionManager.getSession(message.getFromUser());
        if (session.getAttribute("zjhing") != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUserAnswering(WxMpXmlMessage message) {
        if (isInZjh(message)) {
            return true;
        }
        return false;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        this.wxSessionManager = sessionManager;
        if (isUserWantGuess(wxMessage)) {
            //当第一次发消息，初始化客户端
            if (!userChannelMap.containsKey(wxMessage.getFromUser())) {
                NettyClient nettyClient = new NettyClient();
                try {
                    nettyClient.start();
                    while (true) {
                        //一直等待连接成功
                        if (nettyClient.getChannel() != null) {
                            log.info("连接服务器成功");
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                String lang = "zh_CN"; //语言
                WxMpUser user = wxMpServiceHttpClient.getUserService().userInfo(wxMessage.getFromUser(), lang);
                loginRequestPacket.setUserName(user.getNickname());
                sessionManager.getSession(wxMessage.getFromUser()).setAttribute("nickName", user.getNickname());
                nettyClient.getChannel().writeAndFlush(loginRequestPacket);
                log.info("发送登录请求");
                userChannelMap.put(wxMessage.getFromUser(), nettyClient.getChannel());
                SendMessage.channelUserMap.put(nettyClient.getChannel().id().asLongText(), wxMessage.getFromUser());
                sessionManager.getSession(wxMessage.getFromUser()).setAttribute("zjhing", true);
                return null;
            }

        }

        if (isUserAnswering(wxMessage)) {
            giveHint(wxMessage, wxMpService, sessionManager);
        }

        return null;

    }


    protected void giveHint(WxMpXmlMessage wxMessage, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        if ("房间列表".equals(wxMessage.getContent())) {
            WxMessageRequestPacket wxMessageRequestPacket = new WxMessageRequestPacket();
            wxMessageRequestPacket.setPacketId(PacketId.roomList);
            wxMessageRequestPacket.setMessage(wxMessage.getContent());
            userChannelMap.get(wxMessage.getFromUser()).writeAndFlush(wxMessageRequestPacket);
        } else if (wxMessage.getContent().contains("加入房间")) {
            String nickName = (String) sessionManager.getSession(wxMessage.getFromUser()).getAttribute("nickName");
            //进入房间
            JoinRoomRequestPacket joinRoomRequestPacket = new JoinRoomRequestPacket();
            joinRoomRequestPacket.setRoomName(nickName + "的房间");
            if (wxMessage.getContent().length() > 4) {
                String roomId = wxMessage.getContent().substring(4);
                joinRoomRequestPacket.setRoomId(roomId);
            }else{
                ClientMsgManager.putMsg(userChannelMap.get(wxMessage.getFromUser()).id().asLongText(), "系统自动创建房间");
            }
            userChannelMap.get(wxMessage.getFromUser()).writeAndFlush(joinRoomRequestPacket);
        }else if("开始游戏".equals(wxMessage.getContent())){
            WxMessageRequestPacket wxMessageRequestPacket = new WxMessageRequestPacket();
            wxMessageRequestPacket.setMessage(wxMessage.getContent());
            wxMessageRequestPacket.setPacketId(PacketId.startGame);
            userChannelMap.get(wxMessage.getFromUser()).writeAndFlush(wxMessageRequestPacket);
        }else {
            WxMessageRequestPacket wxMessageRequestPacket = new WxMessageRequestPacket();
            wxMessageRequestPacket.setMessage(wxMessage.getContent());
            wxMessageRequestPacket.setPacketId(PacketId.normal);
            userChannelMap.get(wxMessage.getFromUser()).writeAndFlush(wxMessageRequestPacket);
        }
    }
}
