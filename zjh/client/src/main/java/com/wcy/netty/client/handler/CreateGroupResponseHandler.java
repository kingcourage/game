package com.wcy.netty.client.handler;

import com.wcy.netty.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) throws Exception {
        System.out.println("群创建成功，id为["+createGroupResponsePacket.getGroupId()+"],");
        System.out.println("群里面有："+createGroupResponsePacket.getUserNameList());
    }
}
