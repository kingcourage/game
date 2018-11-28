package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.ListGroupMembersRequestPacket;
import com.wcy.netty.protocol.response.ListGroupMembersResponsePacket;
import com.wcy.netty.session.Session;
import com.wcy.netty.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {
    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    private ListGroupMembersRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket msg) throws Exception {
        //1.获取群的ChannelGroup
        String groupId = msg.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        //2.遍历群成员channel 对应的session,构造群成员信息
        List<Session> sessionList = new ArrayList<>();
        for(Channel channel : channelGroup){
            Session session = SessionUtil.getSession(channel);
            sessionList.add(session);
        }

        //3.构建获取成员列表响应写回到客户端
        ListGroupMembersResponsePacket listGroupMembersResponsePacket = new ListGroupMembersResponsePacket();
        listGroupMembersResponsePacket.setGroupId(groupId);
        listGroupMembersResponsePacket.setSessionList(sessionList);
        ctx.channel().writeAndFlush(listGroupMembersResponsePacket);
    }
}
