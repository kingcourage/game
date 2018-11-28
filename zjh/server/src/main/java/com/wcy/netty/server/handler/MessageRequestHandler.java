package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.MessageRequestPacket;
import com.wcy.netty.protocol.response.MessageResponsePacket;
import com.wcy.netty.session.Session;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        //1拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        //2.通过消息发送方的会话信息构建要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        //3拿到消息接收方的channel
        Channel toUserChannel  = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        //4将消息发送给消息接收方
        if(toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
            toUserChannel.writeAndFlush(messageResponsePacket);
        }else{
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }
        ctx.channel().writeAndFlush(messageResponsePacket);
    }


}
