package com.wcy.netty.serialize;

public interface Serializer {

    /**
     * json系列化
     */
    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java对象转化成二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二级制转化为java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}
