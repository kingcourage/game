package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.ExitRoomResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExitRoomResponseHandler extends SimpleChannelInboundHandler<ExitRoomResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ExitRoomResponsePacket exitRoomResponsePacket) throws Exception {
        if(exitRoomResponsePacket.isSuccess()){
            log.info("退出房间成功");
        }else{
            log.info("退出房间失败，原因:{}",exitRoomResponsePacket.getReason());
        }
    }
}
