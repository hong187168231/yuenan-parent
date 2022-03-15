package com.indo.game.service.pp;

import com.indo.game.pojo.dto.pp.PpAuthenticateCallBackReq;
import com.indo.game.pojo.dto.pp.PpBalanceCallBackReq;
import com.indo.game.pojo.dto.pp.PpBetCallBackReq;
import com.indo.game.pojo.dto.pp.PpBonusWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpJackpotWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpPromoWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpRefundWinCallBackReq;
import com.indo.game.pojo.dto.pp.PpResultCallBackReq;

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
     * 玩家退款
     * @param ppRefundWinCallBackReq
     * @param ip
     * @return
     */
    Object refund(PpRefundWinCallBackReq ppRefundWinCallBackReq, String ip);
}
