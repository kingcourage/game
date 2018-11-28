package com.wcy.netty.attribute;

import com.wcy.netty.session.Session;
import io.netty.util.AttributeKey;

public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
