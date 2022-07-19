package com.indo.game.service.pp;

import com.indo.game.pojo.dto.pp.*;

public interface PpCallbackService {

    /**
     * 权限验证
     * @param ppAuthenticateCallBackReq
     * @param ip
     * @return
     */
    Object authenticate(PpAuthenticateCallBackReq ppAuthenticateCallBackReq, String ip);

    /**
     * 查询余额
     * @param ppBalanceCallBackReq
     * @param ip
     * @return
     */
    Object getBalance(PpBalanceCallBackReq ppBalanceCallBackReq, String ip);

    /**
     * 下注
     * @param ppBetCallBackReq
     * @param ip
     * @return
     */
    Object bet(PpBetCallBackReq ppBetCallBackReq, String ip);
    /**
     * 下注开奖结果
     * @param ppResultCallBackReq
     * @param ip
     * @return
     */
    Object result(PpResultCallBackReq ppResultCallBackReq, String ip);
    /**
     * 玩家免费回合赢奖
     * @param ppBonusWinCallBackReq
     * @param ip
     * @return
     */
    Object bonusWin(PpBonusWinCallBackReq ppBonusWinCallBackReq, String ip);
    /**
     * 玩家累计奖金中奖
     * @param ppJackpotWinCallBackReq
     * @param ip
     * @return
     */
    Object jackpotWin(PpJackpotWinCallBackReq ppJackpotWinCallBackReq, String ip);
    /**
     * 玩家最终赢奖
     * @param ppPromoWinCallBackReq
     * @param ip
     * @return
     */
    Object promoWin(PpPromoWinCallBackReq ppPromoWinCallBackReq, String ip);

    /**
     * 每次一个游戏回合结束时，Pragmatic Play 系统都将调用 EndRound 方法，以便运营商能够在自己一侧实时结束游戏回合的交易
     * @param ppEndRoundCallBackReq
     * @param ip
     * @return
     */
    public Object endRound(PpEndRoundCallBackReq ppEndRoundCallBackReq, String ip);


    /**
     * 玩家退款
     * @param ppRefundWinCallBackReq
     * @param ip
     * @return
     */
    Object refund(PpRefundWinCallBackReq ppRefundWinCallBackReq, String ip);

    /**
     *系统将向娱乐场运营商发送玩家需要调整的余额金额
     * @param ppAdjustmentCallBackReq
     * @param ip
     * @return
     */
    public Object adjustment(PpAdjustmentCallBackReq ppAdjustmentCallBackReq, String ip);
}
