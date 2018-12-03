package com.wcy.wechat.handler;

import com.wcy.wechat.common.Constants;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutVideoMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VideoHandler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                  WxMpService wxMpService, WxSessionManager sessionManager) {
    try {
      WxMediaUploadResult wxMediaUploadResult = wxMpService.getMaterialService()
        .mediaUpload(WxConsts.MediaFileType.VIDEO, Constants.FILE_MP4, ClassLoader.getSystemResourceAsStream("file/mm.mp4"));
      WxMpXmlOutVideoMessage m
        = WxMpXmlOutVideoMessage
        .VIDEO()
        .mediaId(wxMediaUploadResult.getMediaId())
        .fromUser(wxMessage.getToUser())
        .toUser(wxMessage.getFromUser())
        .build();
      return m;
    } catch (WxErrorException e) {
      e.printStackTrace();
    }

    return null;
  }
}
