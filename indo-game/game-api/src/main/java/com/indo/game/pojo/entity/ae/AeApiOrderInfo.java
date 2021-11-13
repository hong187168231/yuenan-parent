package com.indo.game.pojo.entity.ae;

import java.math.BigDecimal;

/**
 * ae 订单记录信息
 */
public class AeApiOrderInfo {

    //订单号
    private String orderid;
    //玩家账号
    private String uname;
    //游戏ID
    private String gameid;
    //游戏局号
    private String battleid;
    //房间ID
    private int roomid;
    //下注
    private BigDecimal bet;
    //总下注
    private BigDecimal allbet;
    //输赢
    private BigDecimal profit;
    //抽水
    private BigDecimal revenue;
    //洗钱(炸金花拿到豹子时奖励)
    private BigDecimal xiqian;
    //游戏开始时间戳
    private int stime;
    //游戏结束时间戳
    private int etime;
    //同局玩家人数
    private int playernum;
    //游戏结果
    private String result;
    //玩家位置
    private int chairid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getBattleid() {
        return battleid;
    }

    public void setBattleid(String battleid) {
        this.battleid = battleid;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public BigDecimal getAllbet() {
        return allbet;
    }

    public void setAllbet(BigDecimal allbet) {
        this.allbet = allbet;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getXiqian() {
        return xiqian;
    }

    public void setXiqian(BigDecimal xiqian) {
        this.xiqian = xiqian;
    }

    public int getStime() {
        return stime;
    }

    public void setStime(int stime) {
        this.stime = stime;
    }

    public int getEtime() {
        return etime;
    }

    public void setEtime(int etime) {
        this.etime = etime;
    }

    public int getPlayernum() {
        return playernum;
    }

    public void setPlayernum(int playernum) {
        this.playernum = playernum;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getChairid() {
        return chairid;
    }

    public void setChairid(int chairid) {
        this.chairid = chairid;
    }

    @Override
    public String toString() {
        return "AeApiOrderInfo{" +
                "orderid='" + orderid + '\'' +
                ", uname='" + uname + '\'' +
                ", gameid='" + gameid + '\'' +
                ", battleid='" + battleid + '\'' +
                ", roomid=" + roomid +
                ", bet=" + bet +
                ", allbet=" + allbet +
                ", profit=" + profit +
                ", revenue=" + revenue +
                ", xiqian=" + xiqian +
                ", stime=" + stime +
                ", etime=" + etime +
                ", playernum=" + playernum +
                ", result='" + result + '\'' +
                ", chairid=" + chairid +
                '}';
    }
}
