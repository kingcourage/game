package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.RoomUserResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WxMessageResponseHandler extends SimpleChannelInboundHandler<RoomUserResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RoomUserResponsePacket roomUserResponsePacket) throws Exception {
        if(roomUserResponsePacket.isSuccess()){
            System.out.println("房间的玩家列表："+roomUserResponsePacket.getPlayerList());
        }
    }
}
