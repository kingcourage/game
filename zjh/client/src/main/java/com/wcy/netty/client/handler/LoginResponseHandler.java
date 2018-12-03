package com.wcy.netty.client.handler;

import com.wcy.netty.client.msg.ClientMsgManager;
import com.wcy.netty.protocol.response.LoginResponsePacket;
import com.wcy.netty.session.Session;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();
        String result = "";
        if (loginResponsePacket.isSuccess()) {
            result = "[" + userName + "]登录成功，userId 为: " + loginResponsePacket.getUserId();
            System.out.println(result);
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            result = "[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason();
            System.out.println(result);
        }

        ClientMsgManager.putMsg(ctx.channel().id().asLongText(),result);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }
}
