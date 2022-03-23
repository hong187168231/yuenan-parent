package com.indo.game.service.dj;


import com.indo.game.pojo.dto.dj.DjCallBackParentReq;

public interface DjCallbackService {


    Object getBalance(DjCallBackParentReq djCallBackParentReq, String ip);

    Object djBetCallback(DjCallBackParentReq djCallBackParentReq, String ip);

    Object djRefundtCallback(DjCallBackParentReq djCallBackParentReq, String ip);
}
