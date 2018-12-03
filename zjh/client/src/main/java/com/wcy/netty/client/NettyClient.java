package com.wcy.netty.client;

import com.wcy.netty.client.console.ConsoleCommandManager;
import com.wcy.netty.client.console.LoginConsoleCommand;
import com.wcy.netty.client.handler.*;
import com.wcy.netty.client.msg.ClientMsgManager;
import com.wcy.netty.codec.PacketDecoder;
import com.wcy.netty.codec.PacketEncoder;
import com.wcy.netty.codec.Spliter;
import com.wcy.netty.handler.IMIdleStateHandler;
import com.wcy.netty.client.handler.HeartBeatTimerHandler;
import com.wcy.netty.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final Integer MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.
                //指定线程模型
                group(group).
                //指定IO类型为NIO
                channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                //IO逻辑处理
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        // 登录响应处理器
                        ch.pipeline().addLast(new LoginResponseHandler());
                        // 收消息处理器
                        ch.pipeline().addLast(new MessageResponseHandler());
                        // 创建群响应处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        // 加群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        // 退群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());
                        // 获取群成员响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());

                        ch.pipeline().addLast(new JoinRoomResponseHandler());
                        ch.pipeline().addLast(new ExitRoomResponseHandler());
                        ch.pipeline().addLast(new RoomListResponseHandler());
                        ch.pipeline().addLast(new RoomUserResponseHandler());
                        ch.pipeline().addLast(new WxMessageResponseHandler());

                        // 登出响应处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());
                        // 心跳定时器

                        ch.pipeline().addLast(new PacketEncoder());
                        ch.pipeline().addLast(new HeartBeatTimerHandler());

                    }
                });
       connect(bootstrap,HOST,PORT,MAX_RETRY);

    }



    private  void connect(Bootstrap bootstrap, String host, int port, int retry){
        bootstrap.connect(host,port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            }else if(retry == 0){
                System.err.println("重试次数已经用完，放弃连接！");
            }else{
                //第几次重连
                int order = (MAX_RETRY-retry)+1;

                //重连间隔
                int delay = 1 << order;
                System.err.println(new Date()+":连接失败，第"+order+"次重连。。。。");
                bootstrap.config().group().schedule(()->
                    connect(bootstrap,host,port,retry-1),delay, TimeUnit.SECONDS);
                }
            startConsoleThread(channel);
        });
    }

    private static void startConsoleThread(Channel channel){
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);
        new Thread(()->{
            while (!Thread.interrupted()) {
                if(!SessionUtil.hasLogin(channel)){
                    loginConsoleCommand.exec(scanner,channel);
                }else{
                    consoleCommandManager.exec(scanner,channel);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            new NettyClient().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
