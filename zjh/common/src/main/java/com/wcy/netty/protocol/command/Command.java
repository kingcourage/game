package com.wcy.netty.protocol.command;

public interface Command {
    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Byte LOGOUT_REQUEST = 5;

    Byte LOGOUT_RESPONSE = 6;

    Byte CREATE_GROUP_REQUEST = 7;

    Byte CREATE_GROUP_RESPONSE = 8;

    Byte LIST_GROUP_MEMBERS_REQUEST = 9;

    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    Byte JOIN_GROUP_REQUEST = 11;

    Byte JOIN_GROUP_RESPONSE = 12;

    Byte QUIT_GROUP_REQUEST = 13;

    Byte QUIT_GROUP_RESPONSE = 14;

    Byte GROUP_MESSAGE_REQUEST = 15;

    Byte GROUP_MESSAGE_RESPONSE = 16;

    Byte HEARTBEAT_REQUEST = 17;

    Byte HEARTBEAT_RESPONSE = 18;

    Byte JOIN_ROOM_REQUEST = 19;

    Byte JOIN_ROOM_RESPONSE = 20;

    Byte EXIT_ROOM_REQUEST = 21;

    Byte EXIT_ROOM_RESPONSE = 22;

    Byte ROOM_LIST_REQUEST = 23;

    Byte ROOM_LIST_RESPONSE = 24;

    Byte ROOM_USER_REQUEST = 25;

    Byte ROOM_USER_RESPONSE = 26;

    Byte WX_MESSAGE_REQUEST = 27;

    Byte WX_MESSAGE_RESPONSE = 28;
}
