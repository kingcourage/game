package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.RoomListResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RoomListResponseHandler extends SimpleChannelInboundHandler<RoomListResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RoomListResponsePacket roomListResponsePacket) throws Exception {
        if(roomListResponsePacket.isSuccess()){
            System.out.println(roomListResponsePacket.getRoomList());
        }
    }
}
