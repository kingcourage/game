package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.JoinRoomResponsePacket;
import com.wcy.netty.protocol.response.LogoutResponsePacket;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinRoomResponseHandler extends SimpleChannelInboundHandler<JoinRoomResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinRoomResponsePacket msg) throws Exception {
        if(msg.isSuccess()){
            log.info("{}加入成功",msg.getUserName());
        }else{
            log.info("加入失败，原因:{}",msg.getReason());
        }
    }
}
