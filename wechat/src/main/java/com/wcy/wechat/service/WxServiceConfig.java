package com.wcy.wechat.service;

import com.wcy.wechat.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class WxServiceConfig {
    @Autowired
    LogHandler logHandler;
    @Autowired
    TextHandler textHandler;
    @Autowired
    ImageHandler imageHandler;
    @Autowired
    OAuth2Handler oauth2handler;
    @Autowired
    GuessNumberHandler guessNumberHandler;
    @Autowired
    MusicHandler musicHandler;
    @Autowired
    VideoHandler videoHandler;
    @Autowired
    ZjhHandler zjhHandler;
    @Autowired
    @Lazy
    WxMpServiceHttpClientImpl wxMpServiceHttpClient;
    @Autowired
    @Lazy
    WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage;

    @Bean
    @Order(1)
    public WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setToken("wcy");
        wxMpInMemoryConfigStorage.setAppId("wx56adbc0f853aa66b");
        wxMpInMemoryConfigStorage.setSecret("f6a9f2f8802cc9e2148ff9bfda80bc74");
        return wxMpInMemoryConfigStorage;
    }

    @Bean
    @Order(2)
    public WxMpServiceHttpClientImpl wxMpServiceHttpClientImpl(){
        WxMpServiceHttpClientImpl wxMpServiceHttpClient = new WxMpServiceHttpClientImpl();
        wxMpServiceHttpClient.setWxMpConfigStorage(wxMpInMemoryConfigStorage);
        return  wxMpServiceHttpClient;
    }

    @Bean
    @Order(3)
    public WxMpMessageRouter wxMpMessageRouter(){
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpServiceHttpClient);
        wxMpMessageRouter.rule().handler(logHandler).next()
                .rule().msgType(WxConsts.XmlMsgType.TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
                .rule().msgType(WxConsts.XmlMsgType.TEXT).handler(zjhHandler).end()
                .rule().async(false).content("哈哈").handler(textHandler).end()
                .rule().async(false).content("图片").handler(imageHandler).end()
                .rule().async(false).content("oauth").handler(oauth2handler).end()
                .rule().async(false).content("音乐").handler(musicHandler).end()
                .rule().async(false).content("视频").handler(videoHandler).end();
        return  wxMpMessageRouter;
    }

    @Bean
    @Order(4)
    public ExecutorService cachedThreadPool(){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        return cachedThreadPool;
    }


}
