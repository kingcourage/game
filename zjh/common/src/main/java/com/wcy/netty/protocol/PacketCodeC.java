package com.wcy.netty.protocol;

import com.wcy.netty.protocol.request.*;
import com.wcy.netty.protocol.response.*;
import com.wcy.netty.serialize.JSONSerializer;
import com.wcy.netty.serialize.Serializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import static com.wcy.netty.protocol.command.Command.*;

public class PacketCodeC {
    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private static final Map<Byte,Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte,Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        packetTypeMap.put(JOIN_ROOM_REQUEST, JoinRoomRequestPacket.class);
        packetTypeMap.put(JOIN_ROOM_RESPONSE, JoinRoomResponsePacket.class);
        packetTypeMap.put(EXIT_ROOM_REQUEST,ExitRoomRequestPacket.class);
        packetTypeMap.put(EXIT_ROOM_RESPONSE, ExitRoomResponsePacket.class);
        packetTypeMap.put(ROOM_LIST_REQUEST, RoomListRequestPacket.class);
        packetTypeMap.put(ROOM_LIST_RESPONSE, RoomListResponsePacket.class);
        packetTypeMap.put(ROOM_USER_REQUEST, RoomUserRequestPacket.class);
        packetTypeMap.put(ROOM_USER_RESPONSE, RoomUserResponsePacket.class);
        packetTypeMap.put(WX_MESSAGE_REQUEST, WxMessageRequestPacket.class);
        packetTypeMap.put(WX_MESSAGE_RESPONSE, WxMessageResponsePacket.class);


        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(),serializer);
    }

    public ByteBuf encode(ByteBuf byteBuf,Packet packet){

        //序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }


    public Packet decode(ByteBuf byteBuf){
        //跳过magic number
        byteBuf.skipBytes(4);

        //跳过版本号
        byteBuf.skipBytes(1);

        //序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        //指令
        byte commond = byteBuf.readByte();

        //数据包长度
        int lenth = byteBuf.readInt();

        byte[] bytes = new byte[lenth];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(commond);

        Serializer serializer = getSerializer(serializeAlgorithm);

        if(requestType != null && serializer != null){
            return serializer.deserialize(requestType,bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializerAlgorithm){
        return  serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte commond){
        return packetTypeMap.get(commond);
    }
}
