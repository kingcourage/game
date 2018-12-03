package com.wcy.zjh.card;

/**
 * 牌值
 */
public enum CardWeight {
    A(1,"A"),
    Two(2,"2"),
    Three(3,"3"),
    Four(4,"4"),
    Five(5,"5"),
    Six(6,"6"),
    Seven(7,"7"),
    Eight(8,"8"),
    Nine(9,"9"),
    Ten(10,"10"),
    J(11,"J"),
    Q(12,"Q"),
    K(13,"K");
    private Integer value;
    private String name;
    CardWeight(Integer value,String name) {
        this.value = value;
        this.name = name;
    }
    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }}