package com.indo.game.services;


import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.GameMoneyRecord;

import java.util.Map;

/**
 * 第三方通用查询用户信息服务接口
 *
 * @author dlucky
 */
public interface ExternalService {

    /**
     * 根据id 类型 查询第三方用户信息
     *
     * @param userId
     * @param type
     * @return
     */
    CptOpenMember getCptOpenMember(Integer userId, String type);

    void saveCptOpenMember(CptOpenMember cptOpenMember);

    void updateCptOpenMember(CptOpenMember cptOpenMember);

    /**
     * 获取上下分记录表中最后一条上分/下分记录
     *
     * @param type     游戏类型（1：ag；2：开元；3：电竞；4：ae；5：体育；6：足球）
     * @param memberId 用户id
     * @return
     */
    GameMoneyRecord getGameMoneyRecord(int type, Integer memberId);

    Map<String, Object> getRecordBoolean(Integer value, Integer userId);

}
