package com.wcy.wechat.controller;

import com.wcy.wechat.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class WechatController {
    private  WxMpService wxMpService;
    private WxMpMessageRouter wxMpMessageRouter;
    private WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage;

    {
        WxMpMessageHandler logHandler = new DemoLogHandler();
        WxMpMessageHandler textHandler = new DemoTextHandler();
        WxMpMessageHandler imageHandler = new DemoImageHandler();
        WxMpMessageHandler oauth2handler = new DemoOAuth2Handler();
        DemoGuessNumberHandler guessNumberHandler = new DemoGuessNumberHandler();
        DemoMusicHandler musicHandler = new DemoMusicHandler();
        DemoVideoHandler videoHandler = new DemoVideoHandler();
        ZjhHandler zjhHandler = new ZjhHandler();
        wxMpService = new WxMpServiceHttpClientImpl();
        wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setToken("wcy");
        wxMpInMemoryConfigStorage.setAppId("wx56adbc0f853aa66b");
        wxMpInMemoryConfigStorage.setSecret("f6a9f2f8802cc9e2148ff9bfda80bc74");
        wxMpService.setWxMpConfigStorage(wxMpInMemoryConfigStorage);
        wxMpMessageRouter.rule().handler(logHandler).next()
                .rule().msgType(WxConsts.XmlMsgType.TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
                .rule().msgType(WxConsts.XmlMsgType.TEXT).matcher(zjhHandler).handler(zjhHandler).end()
                .rule().async(false).content("哈哈").handler(textHandler).end()
                .rule().async(false).content("图片").handler(imageHandler).end()
                .rule().async(false).content("oauth").handler(oauth2handler).end()
                .rule().async(false).content("音乐").handler(musicHandler).end()
                .rule().async(false).content("视频").handler(videoHandler).end();
    }

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "/wechat")
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toXml());
            }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpInMemoryConfigStorage, timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            response.getWriter().write(outMessage.toEncryptedXml(wxMpInMemoryConfigStorage));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;

    }
}
