package com.live.common.utils;

import java.math.BigDecimal;

/**
 * 用于页面展示相关的工具类
 */
public class ViewUtil {



    /**
     * 保留三位小数，3位之后的全舍
     *
     * @param money 金额
     * @return
     */
    public static BigDecimal getTradeOffAmount(BigDecimal money) {
        if (null == money || money.compareTo(BigDecimal.ZERO) == 0) {
            return new BigDecimal("0.000");
        }
        return money.setScale(3, BigDecimal.ROUND_DOWN);
    }

    /**
     * 保留三位小数，2位之后的全舍
     *
     * @param money 金额
     * @return
     */
    public static BigDecimal getTwoAmount(BigDecimal money) {
        if (null == money || money.compareTo(BigDecimal.ZERO) == 0) {
            return new BigDecimal("0.000");
        }
        return money.setScale(2, BigDecimal.ROUND_DOWN);
    }
}
