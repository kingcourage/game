package com.wcy.zjh.card;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 牌
 */
@Data
@NoArgsConstructor
public class Card{
    public int suit;
    public int weight;

    @Override
    public String toString() {
        CardSuit cs = CardSuit.values()[suit];
        CardWeight cw = CardWeight.values()[weight];
        return "[" + cs + "," + cw + "]";
    }
}
