package com.wcy.wechat.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
@Component
public class TextHandler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                  WxMpService wxMpService, WxSessionManager sessionManager) {
    WxMpXmlOutTextMessage m
      = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUser())
      .toUser(wxMessage.getFromUser()).build();
    return m;
  }

}
