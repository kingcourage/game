package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.RoomListRequestPacket;
import com.wcy.netty.protocol.response.RoomListResponsePacket;
import com.wcy.zjh.manage.RoomManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RoomListRequestHandler extends SimpleChannelInboundHandler<RoomListRequestPacket> {
    public static final  RoomListRequestHandler INSTANCE = new RoomListRequestHandler();
    private RoomListRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoomListRequestPacket roomListRequestPacket) throws Exception {
        RoomListResponsePacket roomListResponsePacket = new RoomListResponsePacket();
        roomListResponsePacket.setRoomList(RoomManager.INSTRANCE.getRoomList());
        roomListResponsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(roomListResponsePacket);
    }
}
