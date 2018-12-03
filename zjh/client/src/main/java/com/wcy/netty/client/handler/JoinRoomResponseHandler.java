package com.wcy.netty.client.handler;

import com.wcy.netty.client.NettyClient;
import com.wcy.netty.client.msg.ClientMsgManager;
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
            String result = msg.getUserName()+"加入成功";
            log.info(result);
            ClientMsgManager.putMsg(ctx.channel().id().asLongText(),result);
        }else{
            log.info("加入失败，原因:{}",msg.getReason());
        }

    }
}
