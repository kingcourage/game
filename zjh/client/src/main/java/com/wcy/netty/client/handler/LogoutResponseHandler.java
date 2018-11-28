package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.LogoutResponsePacket;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket msg) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
