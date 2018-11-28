package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.RoomUserRequestPacket;
import com.wcy.netty.protocol.response.RoomUserResponsePacket;
import com.wcy.zjh.manage.RoomManager;
import com.wcy.zjh.model.Player;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

public class RoomUserRequestHandler extends SimpleChannelInboundHandler<RoomUserRequestPacket> {
    public static final RoomUserRequestHandler INSTANCE = new RoomUserRequestHandler();
    public RoomUserRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RoomUserRequestPacket roomUserRequestPacket) throws Exception {
        String roomId = roomUserRequestPacket.getRoomId();
        Set<Player> playerList =  RoomManager.getPlayerList(roomId);
        RoomUserResponsePacket roomUserResponsePacket = new RoomUserResponsePacket();
        roomUserResponsePacket.setPlayerList(playerList);
        roomUserResponsePacket.setSuccess(true);
        channelHandlerContext.channel().writeAndFlush(roomUserResponsePacket);
    }
}
