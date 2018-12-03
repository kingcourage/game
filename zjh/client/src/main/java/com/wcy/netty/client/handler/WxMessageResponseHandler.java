package com.wcy.netty.client.handler;

import com.wcy.netty.client.msg.ClientMsgManager;
import com.wcy.netty.protocol.response.RoomUserResponsePacket;
import com.wcy.netty.protocol.response.WxMessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxMessageResponseHandler extends SimpleChannelInboundHandler<WxMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WxMessageResponsePacket wxMessageResponsePacket) throws Exception {
            log.info("服务器返回消息{}",wxMessageResponsePacket.getMessage());
            ClientMsgManager.putMsg(channelHandlerContext.channel().id().asLongText(),wxMessageResponsePacket.getMessage());
    }
}
