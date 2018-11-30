package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.WxMessageRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WxMessageRequestHandler extends SimpleChannelInboundHandler<WxMessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WxMessageRequestPacket wxMessageRequestPacket) throws Exception {

    }
}
