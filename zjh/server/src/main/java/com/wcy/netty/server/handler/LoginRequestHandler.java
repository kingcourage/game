package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.LoginRequestPacket;
import com.wcy.netty.protocol.response.LoginResponsePacket;
import com.wcy.netty.session.Session;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {

        //登录响应
        ctx.channel().writeAndFlush(login(loginRequestPacket, ctx));
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private LoginResponsePacket login(LoginRequestPacket loginRequestPacket, ChannelHandlerContext ctx) {
        System.out.println(new Date() + ":收到客户端登录请求。。。");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            String userId = randomUserId();
            loginResponsePacket.setUserId(userId);
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ":登录失败");
        }
        return loginResponsePacket;
    }

    private String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
