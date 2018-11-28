package com.wcy.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+":客户端写出数据");

        //获取数据
        ByteBuf buffer = getByteBuf(ctx);

        //写数据
        ctx.channel().writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        //获取二进制抽象ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //准备数据，指定字符串的字符集为utf-8
        byte[] bytes = "你好".getBytes(Charset.forName("utf-8"));

        //填充数据到 ByteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.printf(new Date()+":客户端收到数据->"+buf.toString(Charset.forName("utf-8")));
    }
}
