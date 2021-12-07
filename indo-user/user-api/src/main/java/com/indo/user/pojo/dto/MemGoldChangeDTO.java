package com.indo.user.pojo.dto;


import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author puff
 * @Description: 账变参数DTO
 * @date 2021/5/22
 */
@Data
public class MemGoldChangeDTO {

    /**
     * 会员 id
     */
    private Long userId;

    /**
     * 具体操作记录id
     */
    private Long refId;

    /**
     * 流水号
     * 传入时已传入的流水号作为账变流水号
     * 为空账变接口自己生成
     */
    private String serialNo;

    /**
     * 操作员账号
     */
    private String updateUser;

    /**
     * 变动金额
     */
    private BigDecimal changeAmount;

    /**
     * 收入类型是否需要打码
     * 正常情况下不需要传,如果设置为false，会不做账变类型判断
     * 直接增加可提现金额
     */
    private Boolean incomeCodeFlag = true;

    /**
     * 账变枚举
     */
    private GoldchangeEnum goldchangeEnum;

    /**
     * 交易类型 枚举
     */
    public TradingEnum tradingEnum;

}
