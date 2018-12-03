package com.wcy.netty.server.handler;

import com.wcy.netty.protocol.command.Command;
import com.wcy.netty.protocol.command.PacketId;
import com.wcy.netty.protocol.request.WxMessageRequestPacket;
import com.wcy.netty.protocol.response.WxMessageResponsePacket;
import com.wcy.netty.util.SessionUtil;
import com.wcy.zjh.manage.RoomManager;
import com.wcy.zjh.manage.UserManager;
import com.wcy.zjh.model.Game;
import com.wcy.zjh.model.Room;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.CollectionUtils;

import java.util.List;


public class WxMessageRequestHandler extends SimpleChannelInboundHandler<WxMessageRequestPacket> {
    public static WxMessageRequestHandler INSTANCE = new WxMessageRequestHandler();

    private WxMessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WxMessageRequestPacket wxMessageRequestPacket) throws Exception {
        WxMessageResponsePacket wxMessageResponsePacket = new WxMessageResponsePacket();
        switch (wxMessageRequestPacket.getPacketId()) {
            case PacketId.roomList:
                List<Room> roomList = RoomManager.INSTRANCE.getRoomList();
                if (!CollectionUtils.isEmpty(roomList)) {
                    StringBuffer stringBuffer = new StringBuffer();
                    roomList.stream().forEach(item -> {
                        stringBuffer.append(item.getRoomName() + ":" + item.getRoomId() + "\n");
                    });
                    wxMessageResponsePacket.setMessage(stringBuffer.toString());
                }
                break;
            case PacketId.startGame:{
                String userId = SessionUtil.getSession(channelHandlerContext.channel()).getUserId();
                String roomId = UserManager.getRoomId(userId);
                Game game = new Game(roomId);
                game.start();
            }
            default:
                wxMessageResponsePacket.setMessage(wxMessageRequestPacket.getMessage());
                break;
        }


        channelHandlerContext.channel().writeAndFlush(wxMessageResponsePacket);
    }
}
