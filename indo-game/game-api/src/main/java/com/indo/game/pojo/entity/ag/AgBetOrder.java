package com.indo.game.pojo.entity.ag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AgBetOrder implements Serializable {
    /**
     * 字段: ag_bet_order.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 字段: ag_bet_order.data_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 数据类型：BR下注记录，EBR电子游戏下注记录，TR户口转账记录，GR游戏结果，LBR彩票下注记录，LGR彩票结果
     *
     * @mbggenerated
     */
    private String dataType;

    /**
     * 字段: ag_bet_order.bill_no<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 50<br/>
     * 说明: billNo: 注单流水号(满足平台的唯一
约束条件)
     *
     * @mbggenerated
     */
    private String billNo;

    /**
     * 字段: ag_bet_order.player_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 玩家账号
     *
     * @mbggenerated
     */
    private String playerName;

    /**
     * 字段: ag_bet_order.agent_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 代理商编号
     *
     * @mbggenerated
     */
    private String agentCode;

    /**
     * 字段: ag_bet_order.game_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 游戏局号 
[AG 体育]AG 体育的副单号
     *
     * @mbggenerated
     */
    private String gameCode;

    /**
     * 字段: ag_bet_order.net_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 玩家输赢额度
     *
     * @mbggenerated
     */
    private BigDecimal netAmount;

    /**
     * 字段: ag_bet_order.bet_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 投注时间
     *
     * @mbggenerated
     */
    private Date betTime;

    /**
     * 字段: ag_bet_order.game_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 游戏类型
     *
     * @mbggenerated
     */
    private String gameType;

    /**
     * 字段: ag_bet_order.bet_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 投注金额
     *
     * @mbggenerated
     */
    private BigDecimal betAmount;

    /**
     * 字段: ag_bet_order.valid_bet_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 有效投注额度
     *
     * @mbggenerated
     */
    private BigDecimal validBetAmount;

    /**
     * 字段: ag_bet_order.flag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 结算状态
     *
     * @mbggenerated
     */
    private Integer flag;

    /**
     * 字段: ag_bet_order.play_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 游戏玩法
     *
     * @mbggenerated
     */
    private Integer playType;

    /**
     * 字段: ag_bet_order.currency<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 货币类型
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * 字段: ag_bet_order.table_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 桌子编号
     *
     * @mbggenerated
     */
    private String tableCode;

    /**
     * 字段: ag_bet_order.login_ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明:  玩家 IP
     *
     * @mbggenerated
     */
    private String loginIp;

    /**
     * 字段: ag_bet_order.recalcu_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 注单重新派彩时间
     *
     * @mbggenerated
     */
    private Date recalcuTime;

    /**
     * 字段: ag_bet_order.platform_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台编号(通常为空)
     *
     * @mbggenerated
     */
    private String platformId;

    /**
     * 字段: ag_bet_order.platform_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台类型
     *
     * @mbggenerated
     */
    private String platformType;

    /**
     * 字段: ag_bet_order.stringex<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 产品附注(通常为空)
     *
     * @mbggenerated
     */
    private String stringex;

    /**
     * 字段: ag_bet_order.remark<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 额外信息
BBIN 平台的数据类型 
1=BB 体育
3=视讯
5=机率
12=彩票
15=3D 厅
30=BB 捕鱼机
99=小费
备注: 如 AGIN 平台 
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 字段: ag_bet_order.round<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台内的大厅类型
     *
     * @mbggenerated
     */
    private String round;

    /**
     * 字段: ag_bet_order.result<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 结果
     *
     * @mbggenerated
     */
    private String result;

    /**
     * 字段: ag_bet_order.before_credit<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 玩家下注前的剩余额
度
     *
     * @mbggenerated
     */
    private BigDecimal beforeCredit;

    /**
     * 字段: ag_bet_order.device_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 设备类型
     *
     * @mbggenerated
     */
    private Integer deviceType;

    /**
     * 字段: ag_bet_order.bet_amount_bonus<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明:  Jackpot 下注额度
     *
     * @mbggenerated
     */
    private BigDecimal betAmountBonus;

    /**
     * 字段: ag_bet_order.net_amount_bonus<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: Jackpot 派彩
     *
     * @mbggenerated
     */
    private BigDecimal netAmountBonus;

    /**
     * 字段: ag_bet_order.game_category<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 1 为电子桌面游戏
0 为非电子桌面游戏
备注:适合用 XIN / BG/ ENDO / PT / MG / 
PNG
     *
     * @mbggenerated
     */
    private Integer gameCategory;

    /**
     * 字段: ag_bet_order.cancel_reason<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 400<br/>
     * 说明: 取消原因
AG 体育注单取消的原因,长度为 120
字符
     *
     * @mbggenerated
     */
    private String cancelReason;

    /**
     * 字段: ag_bet_order.create_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 字段: ag_bet_order.update_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * 字段: ag_bet_order.is_handle<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 打码量：0未处理，1已处理
     *
     * @mbggenerated
     */
    private Integer isHandle;

    /**
     * 字段: ag_bet_order.game_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 游戏名称
     *
     * @mbggenerated
     */
    private String gameName;

    /**
     * 字段: ag_bet_order.pay_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 玩法名称
     *
     * @mbggenerated
     */
    private String payName;

    /**
     * 字段: ag_bet_order.platform_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 平台名称
     *
     * @mbggenerated
     */
    private String platformName;

    /**
     * 字段: ag_bet_order.round_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 平台内的大厅名称
     *
     * @mbggenerated
     */
    private String roundName;

    /**
     * 字段: ag_bet_order.user_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 用户ID
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ag_bet_order
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return ag_bet_order.id: 
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * 字段: ag_bet_order.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return ag_bet_order.data_type: 数据类型：BR下注记录，EBR电子游戏下注记录，TR户口转账记录，GR游戏结果，LBR彩票下注记录，LGR彩票结果
     *
     * @mbggenerated
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 字段: ag_bet_order.data_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 数据类型：BR下注记录，EBR电子游戏下注记录，TR户口转账记录，GR游戏结果，LBR彩票下注记录，LGR彩票结果
     *
     * @mbggenerated
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return ag_bet_order.bill_no: billNo: 注单流水号(满足平台的唯一
约束条件)
     *
     * @mbggenerated
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 字段: ag_bet_order.bill_no<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 50<br/>
     * 说明: billNo: 注单流水号(满足平台的唯一
约束条件)
     *
     * @mbggenerated
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * @return ag_bet_order.player_name: 玩家账号
     *
     * @mbggenerated
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * 字段: ag_bet_order.player_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 玩家账号
     *
     * @mbggenerated
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return ag_bet_order.agent_code: 代理商编号
     *
     * @mbggenerated
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * 字段: ag_bet_order.agent_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 代理商编号
     *
     * @mbggenerated
     */
    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    /**
     * @return ag_bet_order.game_code: 游戏局号 
[AG 体育]AG 体育的副单号
     *
     * @mbggenerated
     */
    public String getGameCode() {
        return gameCode;
    }

    /**
     * 字段: ag_bet_order.game_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 32<br/>
     * 说明: 游戏局号 
[AG 体育]AG 体育的副单号
     *
     * @mbggenerated
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * @return ag_bet_order.net_amount: 玩家输赢额度
     *
     * @mbggenerated
     */
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /**
     * 字段: ag_bet_order.net_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 玩家输赢额度
     *
     * @mbggenerated
     */
    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    /**
     * @return ag_bet_order.bet_time: 投注时间
     *
     * @mbggenerated
     */
    public Date getBetTime() {
        return betTime;
    }

    /**
     * 字段: ag_bet_order.bet_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 投注时间
     *
     * @mbggenerated
     */
    public void setBetTime(Date betTime) {
        this.betTime = betTime;
    }

    /**
     * @return ag_bet_order.game_type: 游戏类型
     *
     * @mbggenerated
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * 字段: ag_bet_order.game_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 游戏类型
     *
     * @mbggenerated
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * @return ag_bet_order.bet_amount: 投注金额
     *
     * @mbggenerated
     */
    public BigDecimal getBetAmount() {
        return betAmount;
    }

    /**
     * 字段: ag_bet_order.bet_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 投注金额
     *
     * @mbggenerated
     */
    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    /**
     * @return ag_bet_order.valid_bet_amount: 有效投注额度
     *
     * @mbggenerated
     */
    public BigDecimal getValidBetAmount() {
        return validBetAmount;
    }

    /**
     * 字段: ag_bet_order.valid_bet_amount<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 有效投注额度
     *
     * @mbggenerated
     */
    public void setValidBetAmount(BigDecimal validBetAmount) {
        this.validBetAmount = validBetAmount;
    }

    /**
     * @return ag_bet_order.flag: 结算状态
     *
     * @mbggenerated
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 字段: ag_bet_order.flag<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 结算状态
     *
     * @mbggenerated
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return ag_bet_order.play_type: 游戏玩法
     *
     * @mbggenerated
     */
    public Integer getPlayType() {
        return playType;
    }

    /**
     * 字段: ag_bet_order.play_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 游戏玩法
     *
     * @mbggenerated
     */
    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    /**
     * @return ag_bet_order.currency: 货币类型
     *
     * @mbggenerated
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 字段: ag_bet_order.currency<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 货币类型
     *
     * @mbggenerated
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return ag_bet_order.table_code: 桌子编号
     *
     * @mbggenerated
     */
    public String getTableCode() {
        return tableCode;
    }

    /**
     * 字段: ag_bet_order.table_code<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 桌子编号
     *
     * @mbggenerated
     */
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    /**
     * @return ag_bet_order.login_ip:  玩家 IP
     *
     * @mbggenerated
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 字段: ag_bet_order.login_ip<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明:  玩家 IP
     *
     * @mbggenerated
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * @return ag_bet_order.recalcu_time: 注单重新派彩时间
     *
     * @mbggenerated
     */
    public Date getRecalcuTime() {
        return recalcuTime;
    }

    /**
     * 字段: ag_bet_order.recalcu_time<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 19<br/>
     * 说明: 注单重新派彩时间
     *
     * @mbggenerated
     */
    public void setRecalcuTime(Date recalcuTime) {
        this.recalcuTime = recalcuTime;
    }

    /**
     * @return ag_bet_order.platform_id: 平台编号(通常为空)
     *
     * @mbggenerated
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * 字段: ag_bet_order.platform_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台编号(通常为空)
     *
     * @mbggenerated
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    /**
     * @return ag_bet_order.platform_type: 平台类型
     *
     * @mbggenerated
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * 字段: ag_bet_order.platform_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台类型
     *
     * @mbggenerated
     */
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    /**
     * @return ag_bet_order.stringex: 产品附注(通常为空)
     *
     * @mbggenerated
     */
    public String getStringex() {
        return stringex;
    }

    /**
     * 字段: ag_bet_order.stringex<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 产品附注(通常为空)
     *
     * @mbggenerated
     */
    public void setStringex(String stringex) {
        this.stringex = stringex;
    }

    /**
     * @return ag_bet_order.remark: 额外信息
BBIN 平台的数据类型 
1=BB 体育
3=视讯
5=机率
12=彩票
15=3D 厅
30=BB 捕鱼机
99=小费
备注: 如 AGIN 平台 
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 字段: ag_bet_order.remark<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 100<br/>
     * 说明: 额外信息
BBIN 平台的数据类型 
1=BB 体育
3=视讯
5=机率
12=彩票
15=3D 厅
30=BB 捕鱼机
99=小费
备注: 如 AGIN 平台 
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return ag_bet_order.round: 平台内的大厅类型
     *
     * @mbggenerated
     */
    public String getRound() {
        return round;
    }

    /**
     * 字段: ag_bet_order.round<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 平台内的大厅类型
     *
     * @mbggenerated
     */
    public void setRound(String round) {
        this.round = round;
    }

    /**
     * @return ag_bet_order.result: 结果
     *
     * @mbggenerated
     */
    public String getResult() {
        return result;
    }

    /**
     * 字段: ag_bet_order.result<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 20<br/>
     * 说明: 结果
     *
     * @mbggenerated
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return ag_bet_order.before_credit: 玩家下注前的剩余额
度
     *
     * @mbggenerated
     */
    public BigDecimal getBeforeCredit() {
        return beforeCredit;
    }

    /**
     * 字段: ag_bet_order.before_credit<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 玩家下注前的剩余额
度
     *
     * @mbggenerated
     */
    public void setBeforeCredit(BigDecimal beforeCredit) {
        this.beforeCredit = beforeCredit;
    }

    /**
     * @return ag_bet_order.device_type: 设备类型
     *
     * @mbggenerated
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * 字段: ag_bet_order.device_type<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 设备类型
     *
     * @mbggenerated
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return ag_bet_order.bet_amount_bonus:  Jackpot 下注额度
     *
     * @mbggenerated
     */
    public BigDecimal getBetAmountBonus() {
        return betAmountBonus;
    }

    /**
     * 字段: ag_bet_order.bet_amount_bonus<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明:  Jackpot 下注额度
     *
     * @mbggenerated
     */
    public void setBetAmountBonus(BigDecimal betAmountBonus) {
        this.betAmountBonus = betAmountBonus;
    }

    /**
     * @return ag_bet_order.net_amount_bonus: Jackpot 派彩
     *
     * @mbggenerated
     */
    public BigDecimal getNetAmountBonus() {
        return netAmountBonus;
    }

    /**
     * 字段: ag_bet_order.net_amount_bonus<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: Jackpot 派彩
     *
     * @mbggenerated
     */
    public void setNetAmountBonus(BigDecimal netAmountBonus) {
        this.netAmountBonus = netAmountBonus;
    }

    /**
     * @return ag_bet_order.game_category: 1 为电子桌面游戏
0 为非电子桌面游戏
备注:适合用 XIN / BG/ ENDO / PT / MG / 
PNG
     *
     * @mbggenerated
     */
    public Integer getGameCategory() {
        return gameCategory;
    }

    /**
     * 字段: ag_bet_order.game_category<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 1 为电子桌面游戏
0 为非电子桌面游戏
备注:适合用 XIN / BG/ ENDO / PT / MG / 
PNG
     *
     * @mbggenerated
     */
    public void setGameCategory(Integer gameCategory) {
        this.gameCategory = gameCategory;
    }

    /**
     * @return ag_bet_order.cancel_reason: 取消原因
AG 体育注单取消的原因,长度为 120
字符
     *
     * @mbggenerated
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * 字段: ag_bet_order.cancel_reason<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 400<br/>
     * 说明: 取消原因
AG 体育注单取消的原因,长度为 120
字符
     *
     * @mbggenerated
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return ag_bet_order.create_time: 创建时间
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 字段: ag_bet_order.create_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 创建时间
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return ag_bet_order.update_time: 更新时间
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 字段: ag_bet_order.update_time<br/>
     * 必填: true<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 更新时间
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return ag_bet_order.is_handle: 打码量：0未处理，1已处理
     *
     * @mbggenerated
     */
    public Integer getIsHandle() {
        return isHandle;
    }

    /**
     * 字段: ag_bet_order.is_handle<br/>
     * 必填: true<br/>
     * 缺省: 0<br/>
     * 长度: 10<br/>
     * 说明: 打码量：0未处理，1已处理
     *
     * @mbggenerated
     */
    public void setIsHandle(Integer isHandle) {
        this.isHandle = isHandle;
    }

    /**
     * @return ag_bet_order.game_name: 游戏名称
     *
     * @mbggenerated
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * 字段: ag_bet_order.game_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 游戏名称
     *
     * @mbggenerated
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * @return ag_bet_order.pay_name: 玩法名称
     *
     * @mbggenerated
     */
    public String getPayName() {
        return payName;
    }

    /**
     * 字段: ag_bet_order.pay_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 玩法名称
     *
     * @mbggenerated
     */
    public void setPayName(String payName) {
        this.payName = payName;
    }

    /**
     * @return ag_bet_order.platform_name: 平台名称
     *
     * @mbggenerated
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * 字段: ag_bet_order.platform_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 平台名称
     *
     * @mbggenerated
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * @return ag_bet_order.round_name: 平台内的大厅名称
     *
     * @mbggenerated
     */
    public String getRoundName() {
        return roundName;
    }

    /**
     * 字段: ag_bet_order.round_name<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 平台内的大厅名称
     *
     * @mbggenerated
     */
    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    /**
     * @return ag_bet_order.user_id: 用户ID
     *
     * @mbggenerated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 字段: ag_bet_order.user_id<br/>
     * 必填: false<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 用户ID
     *
     * @mbggenerated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_bet_order
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
        AgBetOrder other = (AgBetOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
            && (this.getBillNo() == null ? other.getBillNo() == null : this.getBillNo().equals(other.getBillNo()))
            && (this.getPlayerName() == null ? other.getPlayerName() == null : this.getPlayerName().equals(other.getPlayerName()))
            && (this.getAgentCode() == null ? other.getAgentCode() == null : this.getAgentCode().equals(other.getAgentCode()))
            && (this.getGameCode() == null ? other.getGameCode() == null : this.getGameCode().equals(other.getGameCode()))
            && (this.getNetAmount() == null ? other.getNetAmount() == null : this.getNetAmount().equals(other.getNetAmount()))
            && (this.getBetTime() == null ? other.getBetTime() == null : this.getBetTime().equals(other.getBetTime()))
            && (this.getGameType() == null ? other.getGameType() == null : this.getGameType().equals(other.getGameType()))
            && (this.getBetAmount() == null ? other.getBetAmount() == null : this.getBetAmount().equals(other.getBetAmount()))
            && (this.getValidBetAmount() == null ? other.getValidBetAmount() == null : this.getValidBetAmount().equals(other.getValidBetAmount()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getPlayType() == null ? other.getPlayType() == null : this.getPlayType().equals(other.getPlayType()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getTableCode() == null ? other.getTableCode() == null : this.getTableCode().equals(other.getTableCode()))
            && (this.getLoginIp() == null ? other.getLoginIp() == null : this.getLoginIp().equals(other.getLoginIp()))
            && (this.getRecalcuTime() == null ? other.getRecalcuTime() == null : this.getRecalcuTime().equals(other.getRecalcuTime()))
            && (this.getPlatformId() == null ? other.getPlatformId() == null : this.getPlatformId().equals(other.getPlatformId()))
            && (this.getPlatformType() == null ? other.getPlatformType() == null : this.getPlatformType().equals(other.getPlatformType()))
            && (this.getStringex() == null ? other.getStringex() == null : this.getStringex().equals(other.getStringex()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRound() == null ? other.getRound() == null : this.getRound().equals(other.getRound()))
            && (this.getResult() == null ? other.getResult() == null : this.getResult().equals(other.getResult()))
            && (this.getBeforeCredit() == null ? other.getBeforeCredit() == null : this.getBeforeCredit().equals(other.getBeforeCredit()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getBetAmountBonus() == null ? other.getBetAmountBonus() == null : this.getBetAmountBonus().equals(other.getBetAmountBonus()))
            && (this.getNetAmountBonus() == null ? other.getNetAmountBonus() == null : this.getNetAmountBonus().equals(other.getNetAmountBonus()))
            && (this.getGameCategory() == null ? other.getGameCategory() == null : this.getGameCategory().equals(other.getGameCategory()))
            && (this.getCancelReason() == null ? other.getCancelReason() == null : this.getCancelReason().equals(other.getCancelReason()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsHandle() == null ? other.getIsHandle() == null : this.getIsHandle().equals(other.getIsHandle()))
            && (this.getGameName() == null ? other.getGameName() == null : this.getGameName().equals(other.getGameName()))
            && (this.getPayName() == null ? other.getPayName() == null : this.getPayName().equals(other.getPayName()))
            && (this.getPlatformName() == null ? other.getPlatformName() == null : this.getPlatformName().equals(other.getPlatformName()))
            && (this.getRoundName() == null ? other.getRoundName() == null : this.getRoundName().equals(other.getRoundName()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_bet_order
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getBillNo() == null) ? 0 : getBillNo().hashCode());
        result = prime * result + ((getPlayerName() == null) ? 0 : getPlayerName().hashCode());
        result = prime * result + ((getAgentCode() == null) ? 0 : getAgentCode().hashCode());
        result = prime * result + ((getGameCode() == null) ? 0 : getGameCode().hashCode());
        result = prime * result + ((getNetAmount() == null) ? 0 : getNetAmount().hashCode());
        result = prime * result + ((getBetTime() == null) ? 0 : getBetTime().hashCode());
        result = prime * result + ((getGameType() == null) ? 0 : getGameType().hashCode());
        result = prime * result + ((getBetAmount() == null) ? 0 : getBetAmount().hashCode());
        result = prime * result + ((getValidBetAmount() == null) ? 0 : getValidBetAmount().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        result = prime * result + ((getPlayType() == null) ? 0 : getPlayType().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getTableCode() == null) ? 0 : getTableCode().hashCode());
        result = prime * result + ((getLoginIp() == null) ? 0 : getLoginIp().hashCode());
        result = prime * result + ((getRecalcuTime() == null) ? 0 : getRecalcuTime().hashCode());
        result = prime * result + ((getPlatformId() == null) ? 0 : getPlatformId().hashCode());
        result = prime * result + ((getPlatformType() == null) ? 0 : getPlatformType().hashCode());
        result = prime * result + ((getStringex() == null) ? 0 : getStringex().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRound() == null) ? 0 : getRound().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getBeforeCredit() == null) ? 0 : getBeforeCredit().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getBetAmountBonus() == null) ? 0 : getBetAmountBonus().hashCode());
        result = prime * result + ((getNetAmountBonus() == null) ? 0 : getNetAmountBonus().hashCode());
        result = prime * result + ((getGameCategory() == null) ? 0 : getGameCategory().hashCode());
        result = prime * result + ((getCancelReason() == null) ? 0 : getCancelReason().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsHandle() == null) ? 0 : getIsHandle().hashCode());
        result = prime * result + ((getGameName() == null) ? 0 : getGameName().hashCode());
        result = prime * result + ((getPayName() == null) ? 0 : getPayName().hashCode());
        result = prime * result + ((getPlatformName() == null) ? 0 : getPlatformName().hashCode());
        result = prime * result + ((getRoundName() == null) ? 0 : getRoundName().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ag_bet_order
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
        sb.append(", billNo=").append(billNo);
        sb.append(", playerName=").append(playerName);
        sb.append(", agentCode=").append(agentCode);
        sb.append(", gameCode=").append(gameCode);
        sb.append(", netAmount=").append(netAmount);
        sb.append(", betTime=").append(betTime);
        sb.append(", gameType=").append(gameType);
        sb.append(", betAmount=").append(betAmount);
        sb.append(", validBetAmount=").append(validBetAmount);
        sb.append(", flag=").append(flag);
        sb.append(", playType=").append(playType);
        sb.append(", currency=").append(currency);
        sb.append(", tableCode=").append(tableCode);
        sb.append(", loginIp=").append(loginIp);
        sb.append(", recalcuTime=").append(recalcuTime);
        sb.append(", platformId=").append(platformId);
        sb.append(", platformType=").append(platformType);
        sb.append(", stringex=").append(stringex);
        sb.append(", remark=").append(remark);
        sb.append(", round=").append(round);
        sb.append(", result=").append(result);
        sb.append(", beforeCredit=").append(beforeCredit);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", betAmountBonus=").append(betAmountBonus);
        sb.append(", netAmountBonus=").append(netAmountBonus);
        sb.append(", gameCategory=").append(gameCategory);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isHandle=").append(isHandle);
        sb.append(", gameName=").append(gameName);
        sb.append(", payName=").append(payName);
        sb.append(", platformName=").append(platformName);
        sb.append(", roundName=").append(roundName);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}