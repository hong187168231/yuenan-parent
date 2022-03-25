package com.indo.game.service.fc;

import com.indo.game.pojo.dto.fc.FCBalanceCallbackReq;
import com.indo.game.pojo.dto.fc.FCBetCallbackReq;
import com.indo.game.pojo.dto.fc.FCCancelCallbackReq;

public interface FCCallbackService {

    Object balance(FCBalanceCallbackReq fcBalanceCallbackReq, String ip);

    Object bet(FCBetCallbackReq fcBetCallbackReq, String ip);

    Object cancel(FCCancelCallbackReq fcCancelCallbackReq, String ip);
}
