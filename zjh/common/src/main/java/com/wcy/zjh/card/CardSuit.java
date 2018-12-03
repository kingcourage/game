package com.wcy.zjh.card;

/**
 * 牌的类型
 */
public enum  CardSuit {
    /**
     * 方块
     */
    Diamonds(1,"方块"),
    /**
     * 梅花
     */
    Clubs(2,"梅花"),
    /**
     * 红桃
     */
    Hearts(3,"红桃"),
    /**
     * 黑桃
     */
    Spades(4,"黑桃");
    private Integer value;
    private String name;
    CardSuit(Integer value,String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }}
