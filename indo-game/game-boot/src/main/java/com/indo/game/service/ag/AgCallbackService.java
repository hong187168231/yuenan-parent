package com.indo.game.service.ag;


import com.indo.game.pojo.dto.ag.AgCallBackTransfer;

public interface AgCallbackService {
    Object bet(AgCallBackTransfer agCallBackTransfer, String ip);

    Object win(AgCallBackTransfer agCallBackTransfer, String ip);
}
