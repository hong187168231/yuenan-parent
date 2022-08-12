package com.indo.admin.modules.game.service;

import com.indo.admin.pojo.dto.game.manage.GameLotteryWinningNumberQueryDto;
import com.indo.admin.pojo.dto.game.manage.GameLotteryWinningNumberReqDto;
import com.indo.core.pojo.entity.game.GameLotteryWinningNumber;

import java.util.List;

public interface ILotteryManageService {

    public List<GameLotteryWinningNumber> rgwn();

    public  boolean modifyRgwn(GameLotteryWinningNumberReqDto req);

    public List<GameLotteryWinningNumber> queryAllGameLotteryWinningNumber(GameLotteryWinningNumberQueryDto queryDto);
}
