package com.indo.game.service.mg;


import com.indo.game.pojo.dto.mg.MgCallBackReq;

public interface MgCallbackService {


    Object mgBalanceCallback(MgCallBackReq mgCallBackReq, String ip);

    Object mgVerifyCallback(MgCallBackReq mgCallBackReq, String ip);


    Object mgUpdatebalanceCallback(MgCallBackReq mgCallBackReq, String ip);
}
