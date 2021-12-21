package com.indo.game.pojo.entity.saba;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("game_saba_oper_info")
public class SabaOperInfo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String action;
    private String userId;
    private String operationId;// 交易纪录 id
    private int currency;//沙巴体育货币币别
    private int matchId;//比赛ID
    private int homeId;//主队ID
    private int awayId;//客队ID
    private String homeName;//依据玩家的语系传入值。 例如：Chile (V)
    private String awayName;//依据玩家的语系传入值。例如：France (V)
    private String kickOffTime;//赛事开始时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private String betTime;//下注时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private BigDecimal betAmount;//注单金额
    private BigDecimal actualAmount;//实际注单金额
    private int sportType;//运动类型。例如：1, 2, 3
    private String sportTypeName;//依据玩家的语系传入值。例如：Soccer
    private String betType;//例如：1, 3
    private String betTypeName;//依据玩家的语系传入值。 例如：Handicap
    private short oddsType;//赔率类型。
    private int oddsId;//
    private BigDecimal odds;//赔率.例如：-0.95, 0.75
    private String betChoice;//投注选择
    private String betChoice_en;//
    private String updateTime;// 更新时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4
    private int leagueId;//联赛ID
    private String leagueName;//依据玩家的语系传入值
    private String leagueName_en;//联赛名称的英文语系名称。
    private String sportTypeName_en;//体育类型的英文语系名称。 e.g. Soccer
    private String betTypeName_en;//投注类型的英文语系名称。
    private String homeName_en;//主队名称的英文语系名称
    private String awayName_en;//客队名称的英文语系名称。
    private String IP;//
    private boolean isLive;// 唯一 id.
    private String refId;//
    private String tsId;// 选填，用户登入会话 id，由厂商提供
    private String point;// 球头
    private String point2;//
    private String betTeam;//下注对象
    private int homeScore;//下注时主队得分。
    private int awayScore;//下注时客队得分。
    private String baStatus;//会员是否为 BA 状态。 false:是 / true:否
    private String excluding;//当 bet_team=aos 时,才返回此字段,返回的值代表会员投注的正确比分不为列出的这些
    private String betFrom;//下注平台。
    private BigDecimal creditAmount;//需增加在玩家的金额。
    private BigDecimal debitAmount;//需从玩家扣除的金额。
    private String oddsInfo;//适用于 bettype = 468,469 才会有值。
    private String matchDatetime;//开赛时间 (yyyy-MM-dd HH:mm:ss.SSS) GMT-4

    @ApiModelProperty(value = "操作状态")
    private String operStatus;

    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
