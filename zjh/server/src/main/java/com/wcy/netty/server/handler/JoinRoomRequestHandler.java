package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.request.JoinRoomRequestPacket;
import com.wcy.netty.protocol.response.JoinRoomResponsePacket;
import com.wcy.netty.protocol.response.WxMessageResponsePacket;
import com.wcy.netty.session.Session;
import com.wcy.netty.util.IDUtil;
import com.wcy.netty.util.SessionUtil;
import com.wcy.zjh.manage.RoomManager;
import com.wcy.zjh.manage.UserManager;
import com.wcy.zjh.model.Player;
import com.wcy.zjh.model.Room;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.apache.commons.lang3.StringUtils;

@ChannelHandler.Sharable
public class JoinRoomRequestHandler extends SimpleChannelInboundHandler<JoinRoomRequestPacket> {

    public static final JoinRoomRequestHandler INSTANCE = new JoinRoomRequestHandler();

    protected JoinRoomRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinRoomRequestPacket joinRoomRequestPacket) throws Exception {
        //加入房间
        JoinRoomResponsePacket joinRoomResponsePacket = new JoinRoomResponsePacket();
        Session session = SessionUtil.getSession(ctx.channel());
        Player player = new Player();
        player.setUserId(session.getUserId());
        player.setUserName(session.getUserName());
        String roomId = joinRoomRequestPacket.getRoomId();
        try {
            if (StringUtils.isBlank(roomId)) {
                //如果没有房间号，则直接新建一个房间
                roomId = IDUtil.randomId();
                String roomName = joinRoomRequestPacket.getRoomName();
                Room room = new Room(roomId);
                room.setRoomName(roomName);
                RoomManager.INSTRANCE.addRoom(room);
                //初始化房间的channelGroup
                ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
                RoomManager.INSTRANCE.addChannelGroup(roomId,channelGroup);
            }
            if(RoomManager.INSTRANCE.getChannelGroup(roomId) == null){
                WxMessageResponsePacket wxMessageResponsePacket = new WxMessageResponsePacket();
                wxMessageResponsePacket.setMessage("房间不存在");
                ctx.channel().writeAndFlush(wxMessageResponsePacket);
            }
                //选择房间 直接加入
            RoomManager.INSTRANCE.getChannelGroup(roomId).add(ctx.channel());
            UserManager.joinRoom(player,roomId);
            joinRoomResponsePacket.setSuccess(true);
            joinRoomResponsePacket.setUserName(player.getUserName());
        } catch (Exception e) {
            joinRoomResponsePacket.setSuccess(false);
            joinRoomResponsePacket.setReason(e.getMessage());
            e.printStackTrace();
        }

        RoomManager.INSTRANCE.getChannelGroup(roomId).writeAndFlush(joinRoomResponsePacket);
    }


}
