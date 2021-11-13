package com.indo.game.services.ky;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.KYResultInfoDTO;

import java.text.ParseException;

public interface KYService {


    /**
     * ky跳转
     *
     * @param
     * @param
     * @param KindId 游戏ID
     */
    Result<String> kyGame(LoginInfo loginUser, String KindId, String ip);

    /**
     * 初始化游戏账号信息
     *
     * @param KindId
     * @param ip
     */
    void initAccountInfo(LoginInfo loginUser, String ip, String KindId);


    /**
     * 退出--下分
     */
    Result<String> exit(LoginInfo loginUser, String ip);

    /**
     * 开元游戏玩家下线主动通知
     *
     * @param timestamp
     * @param agent
     * @param param
     * @param key
     * @return
     */
    // String activeExitNotify(String timestamp, String agent, String param, String key);

    /**
     * 定时拉去bet_order
     */

    void pullKYBetOrder();


    /**
     * 开元统一请求
     *
     * @param url
     * @param uid
     * @param method
     * @param ip
     * @param isLog
     * @param type
     * @return
     */
    KYResultInfoDTO get(String url, Integer uid, String method, String ip, boolean isLog, String type);

    /**
     * 定时拉去bet_order 补偿机制
     */
    void pullKYBetOrderOfMissing() throws ParseException;

    /**
     * 可提余额变动 第三方游戏共用
     */
    // BigDecimal changeNoWithdrawalAmount(CptOpenMember cptOpenMember, String type, Integer userId);

}
