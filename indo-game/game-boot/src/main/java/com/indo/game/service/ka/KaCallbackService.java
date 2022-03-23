package com.indo.game.service.ka;

import com.indo.game.pojo.dto.ka.KACallbackCommonReq;
import com.indo.game.pojo.dto.ka.KACallbackCreditReq;
import com.indo.game.pojo.dto.ka.KACallbackPlayReq;
import com.indo.game.pojo.dto.ka.KACallbackRevokeReq;

public interface KaCallbackService {
    /**
     * 启动游戏验证玩家信息
     * @param commonReq
     * @param ip
     * @return
     */
    Object startGame(KACallbackCommonReq commonReq, String ip);
    /**
     * 交易下注
     * @param kaCallbackPlayReq
     * @param ip
     * @return
     */
    Object palyGame(KACallbackPlayReq kaCallbackPlayReq, String ip);
    /**
     * 游戏派彩
     * @param kaCallbackCreditReq
     * @param ip
     * @return
     */
    Object credit(KACallbackCreditReq kaCallbackCreditReq, String ip);
    /**
     * 取消交易
     * @param kaCallbackRevokeReq
     * @param ip
     * @return
     */
    Object revoke(KACallbackRevokeReq kaCallbackRevokeReq, String ip);
    /**
     * 查询玩家平台余额
     * @param kaCallbackCommonReq
     * @param ip
     * @return
     */
    Object balance(KACallbackCommonReq kaCallbackCommonReq, String ip);
    /**
     * 玩家退出游戏
     * @param kaCallbackCommonReq
     * @param ip
     * @return
     */
    Object end(KACallbackCommonReq kaCallbackCommonReq, String ip);

}
