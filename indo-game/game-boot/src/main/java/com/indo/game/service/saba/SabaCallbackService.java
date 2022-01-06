package com.indo.game.service.saba;

import com.indo.game.pojo.dto.saba.*;

public interface SabaCallbackService {

    public Object getBalance(SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq);
    //扣除投注金额
    public Object placeBet(SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq);
    public Object confirmBet(SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq);
    //结算投注
    public Object settle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq);
    //重新结算投注
    public Object resettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq);
    //撤销结算投注
    public Object unsettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq<ParlayDetailReq<SystemParlayDetailReq>>>> sabaCallBackReq);

    //取消投注
    public Object cancelBet(SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq);

    //投注-仅支援欧洲盘
    public Object placeBetParlay(SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq);
    //当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
    public Object confirmBetParlay(SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq);
    //厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商。本方法支持快乐彩、彩票、桌面游戏产品
    public Object placeBet3rd(SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq);
    public Object confirmBet3rd(SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq);
    //当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
    public Object cashOut(SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq);
    //因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
    public Object updateBet(SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq);
    //检查沙巴体育与厂商之间的连结是否有效。
    public Object healthcheck(SabaCallBackReq<HealthCheckReq> sabaCallBackReq);

}
