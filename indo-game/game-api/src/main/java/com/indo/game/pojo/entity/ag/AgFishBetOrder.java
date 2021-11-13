package com.indo.game.pojo.entity.ag;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AgFishBetOrder implements Serializable {
    /**
     * 字段: ag_fish_bet_order.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 字段: ag_fish_bet_order.data_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 场景
     *
     * @mbggenerated
     */
    private String dataType;

    /**
     * 字段: ag_fish_bet_order.hsr_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 项目编号
     *
     * @mbggenerated
     */
    private String hsrId;

    /**
     * 字段: ag_fish_bet_order.trade_no<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 交易编号
     *
     * @mbggenerated
     */
    private String tradeNo;

    /**
     * 字段: ag_fish_bet_order.platform_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 平台类型
     *
     * @mbggenerated
     */
    private String platformType;

    /**
     * 字段: ag_fish_bet_order.scene_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 场景号
     *
     * @mbggenerated
     */
    private String sceneId;

    /**
     * 字段: ag_fish_bet_order.player_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 账户名称
     *
     * @mbggenerated
     */
    private String playerName;

    /**
     * 字段: ag_fish_bet_order.type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 转账类别
     *
     * @mbggenerated
     */
    private Integer type;

    /**
     * 字段: ag_fish_bet_order.scene_start_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 场景开始时间
     *
     * @mbggenerated
     */
    private Date sceneStartTime;

    /**
     * 字段: ag_fish_bet_order.scene_end_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 场景结束时间
     *
     * @mbggenerated
     */
    private Date sceneEndTime;

    /**
     * 字段: ag_fish_bet_order.room_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 房间号
     *
     * @mbggenerated
     */
    private String roomId;

    /**
     * 字段: ag_fish_bet_order.roombet<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 房间倍率
     *
     * @mbggenerated
     */
    private String roombet;

    /**
     * 字段: ag_fish_bet_order.cost<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 投注额度
     *
     * @mbggenerated
     */
    private String cost;

    /**
     * 字段: ag_fish_bet_order.earn<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 派彩
     *
     * @mbggenerated
     */
    private String earn;

    /**
     * 字段: ag_fish_bet_order.jack_pot_comm<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 场景彩池投注
     *
     * @mbggenerated
     */
    private String jackPotComm;

    /**
     * 字段: ag_fish_bet_order.transfer_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 转账额度
     *
     * @mbggenerated
     */
    private BigDecimal transferAmount;

    /**
     * 字段: ag_fish_bet_order.previous_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 转账前额度
     *
     * @mbggenerated
     */
    private BigDecimal previousAmount;

    /**
     * 字段: ag_fish_bet_order.current_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 当前额度
     *
     * @mbggenerated
     */
    private BigDecimal currentAmount;

    /**
     * 字段: ag_fish_bet_order.currency<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明:  货币类型
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * 字段: ag_fish_bet_order.exchange_rate<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 汇率
     *
     * @mbggenerated
     */
    private String exchangeRate;

    /**
     * 字段: ag_fish_bet_order.ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 玩家 IP
     *
     * @mbggenerated
     */
    private String ip;

    /**
     * 字段: ag_fish_bet_order.flag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 结算状态 (0=成功)
     *
     * @mbggenerated
     */
    private Integer flag;

    /**
     * 字段: ag_fish_bet_order.creation_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 纪录时间
     *
     * @mbggenerated
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date creationTime;

    /**
     * 字段: ag_fish_bet_order.game_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 游戏名称
     *
     * @mbggenerated
     */
    private String gameName;

    /**
     * 字段: ag_fish_bet_order.is_hald<br/>
     * 必填: false<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 是否同步
     *
     * @mbggenerated
     */
    private Integer isHald;

    /**
     * 字段: ag_fish_bet_order.game_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 游戏类型
     *
     * @mbggenerated
     */
    private String gameCode;

    /**
     * 字段: ag_fish_bet_order.device_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 设备类型
     *
     * @mbggenerated
     */
    private String deviceType;

    /**
     * 字段: ag_fish_bet_order.member_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 用户ID
     *
     * @mbggenerated
     */
    private Long memberId;

    /**
     * 字段: ag_fish_bet_order.create_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: CURRENT_TIMESTAMP
     *
     * @mbggenerated
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ag_fish_bet_order
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return ag_fish_bet_order.id: 
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * 字段: ag_fish_bet_order.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return ag_fish_bet_order.data_type: 场景
     *
     * @mbggenerated
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 字段: ag_fish_bet_order.data_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 场景
     *
     * @mbggenerated
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return ag_fish_bet_order.hsr_id: 项目编号
     *
     * @mbggenerated
     */
    public String getHsrId() {
        return hsrId;
    }

    /**
     * 字段: ag_fish_bet_order.hsr_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 项目编号
     *
     * @mbggenerated
     */
    public void setHsrId(String hsrId) {
        this.hsrId = hsrId;
    }

    /**
     * @return ag_fish_bet_order.trade_no: 交易编号
     *
     * @mbggenerated
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 字段: ag_fish_bet_order.trade_no<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 交易编号
     *
     * @mbggenerated
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * @return ag_fish_bet_order.platform_type: 平台类型
     *
     * @mbggenerated
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * 字段: ag_fish_bet_order.platform_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 平台类型
     *
     * @mbggenerated
     */
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    /**
     * @return ag_fish_bet_order.scene_id: 场景号
     *
     * @mbggenerated
     */
    public String getSceneId() {
        return sceneId;
    }

    /**
     * 字段: ag_fish_bet_order.scene_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 场景号
     *
     * @mbggenerated
     */
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    /**
     * @return ag_fish_bet_order.player_name: 账户名称
     *
     * @mbggenerated
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * 字段: ag_fish_bet_order.player_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 账户名称
     *
     * @mbggenerated
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return ag_fish_bet_order.type: 转账类别
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * 字段: ag_fish_bet_order.type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 转账类别
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return ag_fish_bet_order.scene_start_time: 场景开始时间
     *
     * @mbggenerated
     */
    public Date getSceneStartTime() {
        return sceneStartTime;
    }

    /**
     * 字段: ag_fish_bet_order.scene_start_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 场景开始时间
     *
     * @mbggenerated
     */
    public void setSceneStartTime(Date sceneStartTime) {
        this.sceneStartTime = sceneStartTime;
    }

    /**
     * @return ag_fish_bet_order.scene_end_time: 场景结束时间
     *
     * @mbggenerated
     */
    public Date getSceneEndTime() {
        return sceneEndTime;
    }

    /**
     * 字段: ag_fish_bet_order.scene_end_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 场景结束时间
     *
     * @mbggenerated
     */
    public void setSceneEndTime(Date sceneEndTime) {
        this.sceneEndTime = sceneEndTime;
    }

    /**
     * @return ag_fish_bet_order.room_id: 房间号
     *
     * @mbggenerated
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * 字段: ag_fish_bet_order.room_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 房间号
     *
     * @mbggenerated
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * @return ag_fish_bet_order.roombet: 房间倍率
     *
     * @mbggenerated
     */
    public String getRoombet() {
        return roombet;
    }

    /**
     * 字段: ag_fish_bet_order.roombet<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 房间倍率
     *
     * @mbggenerated
     */
    public void setRoombet(String roombet) {
        this.roombet = roombet;
    }

    /**
     * @return ag_fish_bet_order.cost: 投注额度
     *
     * @mbggenerated
     */
    public String getCost() {
        return cost;
    }

    /**
     * 字段: ag_fish_bet_order.cost<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 投注额度
     *
     * @mbggenerated
     */
    public void setCost(String cost) {
        this.cost = cost;
    }

    /**
     * @return ag_fish_bet_order.earn: 派彩
     *
     * @mbggenerated
     */
    public String getEarn() {
        return earn;
    }

    /**
     * 字段: ag_fish_bet_order.earn<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 派彩
     *
     * @mbggenerated
     */
    public void setEarn(String earn) {
        this.earn = earn;
    }

    /**
     * @return ag_fish_bet_order.jack_pot_comm: 场景彩池投注
     *
     * @mbggenerated
     */
    public String getJackPotComm() {
        return jackPotComm;
    }

    /**
     * 字段: ag_fish_bet_order.jack_pot_comm<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 场景彩池投注
     *
     * @mbggenerated
     */
    public void setJackPotComm(String jackPotComm) {
        this.jackPotComm = jackPotComm;
    }

    /**
     * @return ag_fish_bet_order.transfer_amount: 转账额度
     *
     * @mbggenerated
     */
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    /**
     * 字段: ag_fish_bet_order.transfer_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 转账额度
     *
     * @mbggenerated
     */
    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    /**
     * @return ag_fish_bet_order.previous_amount: 转账前额度
     *
     * @mbggenerated
     */
    public BigDecimal getPreviousAmount() {
        return previousAmount;
    }

    /**
     * 字段: ag_fish_bet_order.previous_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 转账前额度
     *
     * @mbggenerated
     */
    public void setPreviousAmount(BigDecimal previousAmount) {
        this.previousAmount = previousAmount;
    }

    /**
     * @return ag_fish_bet_order.current_amount: 当前额度
     *
     * @mbggenerated
     */
    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    /**
     * 字段: ag_fish_bet_order.current_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 当前额度
     *
     * @mbggenerated
     */
    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     * @return ag_fish_bet_order.currency:  货币类型
     *
     * @mbggenerated
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 字段: ag_fish_bet_order.currency<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明:  货币类型
     *
     * @mbggenerated
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return ag_fish_bet_order.exchange_rate: 汇率
     *
     * @mbggenerated
     */
    public String getExchangeRate() {
        return exchangeRate;
    }

    /**
     * 字段: ag_fish_bet_order.exchange_rate<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 汇率
     *
     * @mbggenerated
     */
    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return ag_fish_bet_order.ip: 玩家 IP
     *
     * @mbggenerated
     */
    public String getIp() {
        return ip;
    }

    /**
     * 字段: ag_fish_bet_order.ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 玩家 IP
     *
     * @mbggenerated
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return ag_fish_bet_order.flag: 结算状态 (0=成功)
     *
     * @mbggenerated
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 字段: ag_fish_bet_order.flag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 结算状态 (0=成功)
     *
     * @mbggenerated
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return ag_fish_bet_order.creation_time: 纪录时间
     *
     * @mbggenerated
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * 字段: ag_fish_bet_order.creation_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 纪录时间
     *
     * @mbggenerated
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return ag_fish_bet_order.game_name: 游戏名称
     *
     * @mbggenerated
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * 字段: ag_fish_bet_order.game_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 游戏名称
     *
     * @mbggenerated
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * @return ag_fish_bet_order.is_hald: 是否同步
     *
     * @mbggenerated
     */
    public Integer getIsHald() {
        return isHald;
    }

    /**
     * 字段: ag_fish_bet_order.is_hald<br/>
     * 必填: false<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 是否同步
     *
     * @mbggenerated
     */
    public void setIsHald(Integer isHald) {
        this.isHald = isHald;
    }

    /**
     * @return ag_fish_bet_order.game_code: 游戏类型
     *
     * @mbggenerated
     */
    public String getGameCode() {
        return gameCode;
    }

    /**
     * 字段: ag_fish_bet_order.game_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 游戏类型
     *
     * @mbggenerated
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * @return ag_fish_bet_order.device_type: 设备类型
     *
     * @mbggenerated
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 字段: ag_fish_bet_order.device_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 设备类型
     *
     * @mbggenerated
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return ag_fish_bet_order.member_id: 用户ID
     *
     * @mbggenerated
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 字段: ag_fish_bet_order.member_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 用户ID
     *
     * @mbggenerated
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return ag_fish_bet_order.create_time: CURRENT_TIMESTAMP
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 字段: ag_fish_bet_order.create_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: CURRENT_TIMESTAMP
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_fish_bet_order
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AgFishBetOrder other = (AgFishBetOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
            && (this.getHsrId() == null ? other.getHsrId() == null : this.getHsrId().equals(other.getHsrId()))
            && (this.getTradeNo() == null ? other.getTradeNo() == null : this.getTradeNo().equals(other.getTradeNo()))
            && (this.getPlatformType() == null ? other.getPlatformType() == null : this.getPlatformType().equals(other.getPlatformType()))
            && (this.getSceneId() == null ? other.getSceneId() == null : this.getSceneId().equals(other.getSceneId()))
            && (this.getPlayerName() == null ? other.getPlayerName() == null : this.getPlayerName().equals(other.getPlayerName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getSceneStartTime() == null ? other.getSceneStartTime() == null : this.getSceneStartTime().equals(other.getSceneStartTime()))
            && (this.getSceneEndTime() == null ? other.getSceneEndTime() == null : this.getSceneEndTime().equals(other.getSceneEndTime()))
            && (this.getRoomId() == null ? other.getRoomId() == null : this.getRoomId().equals(other.getRoomId()))
            && (this.getRoombet() == null ? other.getRoombet() == null : this.getRoombet().equals(other.getRoombet()))
            && (this.getCost() == null ? other.getCost() == null : this.getCost().equals(other.getCost()))
            && (this.getEarn() == null ? other.getEarn() == null : this.getEarn().equals(other.getEarn()))
            && (this.getJackPotComm() == null ? other.getJackPotComm() == null : this.getJackPotComm().equals(other.getJackPotComm()))
            && (this.getTransferAmount() == null ? other.getTransferAmount() == null : this.getTransferAmount().equals(other.getTransferAmount()))
            && (this.getPreviousAmount() == null ? other.getPreviousAmount() == null : this.getPreviousAmount().equals(other.getPreviousAmount()))
            && (this.getCurrentAmount() == null ? other.getCurrentAmount() == null : this.getCurrentAmount().equals(other.getCurrentAmount()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getExchangeRate() == null ? other.getExchangeRate() == null : this.getExchangeRate().equals(other.getExchangeRate()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getCreationTime() == null ? other.getCreationTime() == null : this.getCreationTime().equals(other.getCreationTime()))
            && (this.getGameName() == null ? other.getGameName() == null : this.getGameName().equals(other.getGameName()))
            && (this.getIsHald() == null ? other.getIsHald() == null : this.getIsHald().equals(other.getIsHald()))
            && (this.getGameCode() == null ? other.getGameCode() == null : this.getGameCode().equals(other.getGameCode()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_fish_bet_order
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getHsrId() == null) ? 0 : getHsrId().hashCode());
        result = prime * result + ((getTradeNo() == null) ? 0 : getTradeNo().hashCode());
        result = prime * result + ((getPlatformType() == null) ? 0 : getPlatformType().hashCode());
        result = prime * result + ((getSceneId() == null) ? 0 : getSceneId().hashCode());
        result = prime * result + ((getPlayerName() == null) ? 0 : getPlayerName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getSceneStartTime() == null) ? 0 : getSceneStartTime().hashCode());
        result = prime * result + ((getSceneEndTime() == null) ? 0 : getSceneEndTime().hashCode());
        result = prime * result + ((getRoomId() == null) ? 0 : getRoomId().hashCode());
        result = prime * result + ((getRoombet() == null) ? 0 : getRoombet().hashCode());
        result = prime * result + ((getCost() == null) ? 0 : getCost().hashCode());
        result = prime * result + ((getEarn() == null) ? 0 : getEarn().hashCode());
        result = prime * result + ((getJackPotComm() == null) ? 0 : getJackPotComm().hashCode());
        result = prime * result + ((getTransferAmount() == null) ? 0 : getTransferAmount().hashCode());
        result = prime * result + ((getPreviousAmount() == null) ? 0 : getPreviousAmount().hashCode());
        result = prime * result + ((getCurrentAmount() == null) ? 0 : getCurrentAmount().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getExchangeRate() == null) ? 0 : getExchangeRate().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        result = prime * result + ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
        result = prime * result + ((getGameName() == null) ? 0 : getGameName().hashCode());
        result = prime * result + ((getIsHald() == null) ? 0 : getIsHald().hashCode());
        result = prime * result + ((getGameCode() == null) ? 0 : getGameCode().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_fish_bet_order
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dataType=").append(dataType);
        sb.append(", hsrId=").append(hsrId);
        sb.append(", tradeNo=").append(tradeNo);
        sb.append(", platformType=").append(platformType);
        sb.append(", sceneId=").append(sceneId);
        sb.append(", playerName=").append(playerName);
        sb.append(", type=").append(type);
        sb.append(", sceneStartTime=").append(sceneStartTime);
        sb.append(", sceneEndTime=").append(sceneEndTime);
        sb.append(", roomId=").append(roomId);
        sb.append(", roombet=").append(roombet);
        sb.append(", cost=").append(cost);
        sb.append(", earn=").append(earn);
        sb.append(", jackPotComm=").append(jackPotComm);
        sb.append(", transferAmount=").append(transferAmount);
        sb.append(", previousAmount=").append(previousAmount);
        sb.append(", currentAmount=").append(currentAmount);
        sb.append(", currency=").append(currency);
        sb.append(", exchangeRate=").append(exchangeRate);
        sb.append(", ip=").append(ip);
        sb.append(", flag=").append(flag);
        sb.append(", creationTime=").append(creationTime);
        sb.append(", gameName=").append(gameName);
        sb.append(", isHald=").append(isHald);
        sb.append(", gameCode=").append(gameCode);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", memberId=").append(memberId);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}