package com.indo.game.service.lottery;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.lottery.GameLotteryOrderDto;

public interface YxxService {

    public Result yxxOrder(LoginInfo loginUser, GameLotteryOrderDto gameLotteryOrderDto);

    public void yxxOrderSettle();
}
