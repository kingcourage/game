package com.wcy.zjh.model;

import com.wcy.zjh.card.Card;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {
    private String userId;
    private String userName;
    private String coins;
    private List<Card> handCard;
    public Player(){
        handCard = new ArrayList<>();
    }
 }
