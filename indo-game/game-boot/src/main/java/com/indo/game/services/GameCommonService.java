package com.indo.game.services;

import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.user.pojo.entity.MemBaseinfo;

import java.math.BigDecimal;

public interface GameCommonService {


    /**
     * 第三方棋牌转入转出公共处理方法
     *
     * @param dto
     */
    //void changeMoney(MemberBalanceChangeDTO dto);

    /**
     * 判断游戏（彩种）是否开启
     *
     * @param lotteryId
     * @return
     */
    boolean isGameEnabled(Integer lotteryId);


    /**
     * 第三方公共转入转出方法
     */
    Boolean inOrOutBalanceCommon(int type, BigDecimal changeAmount, MemBaseinfo xiazhuren, String remark, CptOpenMember cptOpenMember, String iotType);

    /**
     * 同步打码量
     *
     * @param cptOpenMember
     * @param aeAccountType
     * @param content
     * @param changeType
     * @return
     */
    boolean syncCodeSize(CptOpenMember cptOpenMember, String aeAccountType, String content, int changeType);
}
