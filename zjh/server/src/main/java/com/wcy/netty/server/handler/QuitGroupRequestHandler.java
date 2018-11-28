package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.QuitGroupRequestPacket;
import com.wcy.netty.protocol.response.QuitGroupResponsePacket;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    private QuitGroupRequestHandler() {

    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket msg) throws Exception {
        //1.获取群对应的channelGroup,然后将当前用户的channel移除
        String groupId = msg.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.remove(ctx.channel());

        //2.构造群响应发送给客户端
        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();
        quitGroupResponsePacket.setGroupId(groupId);
        quitGroupResponsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(quitGroupResponsePacket);
    }
}
