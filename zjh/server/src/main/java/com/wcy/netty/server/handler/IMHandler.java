package com.wcy.netty.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.wcy.netty.protocol.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.wcy.netty.protocol.command.Command.*;

@Slf4j
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {
    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();
        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
        handlerMap.put(JOIN_ROOM_REQUEST,JoinRoomRequestHandler.INSTANCE);
        handlerMap.put(EXIT_ROOM_REQUEST,ExitRoomRequestHandler.INSTANCE);
        handlerMap.put(ROOM_LIST_REQUEST,RoomListRequestHandler.INSTANCE);
        handlerMap.put(ROOM_USER_REQUEST,RoomUserRequestHandler.INSTANCE);
        handlerMap.put(WX_MESSAGE_REQUEST,WxMessageRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        log.info("收到消息", JSONObject.toJSONString(packet));
        handlerMap.get(packet.getCommand()).channelRead(ctx, packet);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开");
        super.channelInactive(ctx);
    }
}
