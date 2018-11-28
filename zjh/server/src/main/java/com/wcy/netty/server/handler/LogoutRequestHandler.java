package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.LogoutRequestPacket;
import com.wcy.netty.protocol.response.LogoutResponsePacket;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {
    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    private LogoutRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
        LogoutResponsePacket logoutResponsePacke = new LogoutResponsePacket();
        logoutResponsePacke.setSuccess(true);
        ctx.channel().writeAndFlush(logoutResponsePacke);
    }
}
