package com.wcy.zjh.model;

import com.alibaba.fastjson.JSONObject;
import com.wcy.netty.protocol.response.WxMessageResponsePacket;
import com.wcy.netty.util.SessionUtil;
import com.wcy.zjh.card.Card;
import com.wcy.zjh.card.CardSuit;
import com.wcy.zjh.card.CardWeight;
import com.wcy.zjh.manage.RoomManager;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Game {
    private String status;
    private String roomId;
    private List<Card> cardList = new ArrayList<>(52);
    public Game(String roomId){
        //获得牌
        this.roomId = roomId;
        for(CardSuit cardSuit : CardSuit.values()){
            for(CardWeight cardWeight : CardWeight.values()){
                Card card = new Card();
                card.setSuit(cardSuit.getValue());
                card.setWeight(cardWeight.getValue());
                cardList.add(card);
            }
        }
        clearCard();
    }
    public void clearCard(){
        //洗牌
        for(int i = 0;i<52;i++){
            //随机交换牌
            int index = new Random().nextInt(52);
            Card temp = cardList.get(i);
            cardList.set(i,cardList.get(index));
            cardList.set(index,temp);
        }
    }

    public void start(){
        //获取玩家列表
        List<Player> playerList = RoomManager.INSTRANCE.getPlayerList(roomId);
        int count = playerList.size();
        for(int i =0;i<count;i++){
            playerList.get(i).getHandCard().add(cardList.get(0+i));
            playerList.get(i).getHandCard().add(cardList.get(count+i));
            playerList.get(i).getHandCard().add(cardList.get(count*2+i));

        }

        //给玩家发送消息
        for(Player player : playerList){
            player.getHandCard();
            WxMessageResponsePacket wxMessageResponsePacket = new WxMessageResponsePacket();
            wxMessageResponsePacket.setMessage(JSONObject.toJSONString(player.getHandCard()));
            SessionUtil.getChannel(player.getUserId()).writeAndFlush(wxMessageResponsePacket);
        }
    }


}
