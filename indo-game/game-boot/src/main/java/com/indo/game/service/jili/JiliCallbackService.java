package com.indo.game.service.jili;

import com.indo.game.pojo.dto.jili.JiliCallbackBetReq;
import com.indo.game.pojo.dto.jili.JiliCallbackSessionBetReq;

public interface JiliCallbackService {

    Object auth(String token, String ip);

    Object bet(JiliCallbackBetReq jiliCallbackBetReq, String ip);

    Object cancelBet(JiliCallbackBetReq jiliCallbackBetReq, String ip);

    Object sessionBet(JiliCallbackSessionBetReq jiliCallbackSessionBetReq, String ip);

    Object cancelSessionBet(JiliCallbackSessionBetReq jiliCallbackSessionBetReq, String ip);
}
