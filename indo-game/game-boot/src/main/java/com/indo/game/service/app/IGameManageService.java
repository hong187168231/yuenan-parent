package com.indo.game.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;


public interface IGameManageService {

    public Result queryAllGameCategory();

    public Result queryAllGamePlatform();

    public Result queryHotGamePlatform();

    public Result queryGamePlatformByCategory(Long categoryId);

    public IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    public IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);

}
