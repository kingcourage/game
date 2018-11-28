package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.ExitRoomRequestPacket;
import com.wcy.netty.protocol.response.ExitRoomResponsePacket;
import com.wcy.netty.util.SessionUtil;
import com.wcy.zjh.manage.RoomManager;
import com.wcy.zjh.manage.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ExitRoomRequestHandler extends SimpleChannelInboundHandler<ExitRoomRequestPacket> {
    public static final ExitRoomRequestHandler INSTANCE = new ExitRoomRequestHandler();
    private ExitRoomRequestHandler() {

    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitRoomRequestPacket exitRoomRequestPacket) throws Exception {
        String userId = SessionUtil.getSession(ctx.channel()).getUserId();
        ExitRoomResponsePacket exitRoomResponsePacket = new ExitRoomResponsePacket();
        UserManager.exitRoom(userId);
        exitRoomResponsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(exitRoomResponsePacket);
    }
}
