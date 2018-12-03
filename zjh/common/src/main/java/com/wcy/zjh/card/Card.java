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
        CardSuit cs = CardSuit.values()[suit-1];
        CardWeight cw = CardWeight.values()[weight-1];
        return "[" + cs.getName() + cw.getName() + "]";
    }
}
