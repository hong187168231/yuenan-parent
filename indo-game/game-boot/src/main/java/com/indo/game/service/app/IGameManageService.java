package com.indo.game.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.entity.manage.GameInfoPageReq;

import java.util.List;

public interface IGameManageService {

    public Result queryAllGameCategory();

    public Result queryAllGamePlatform();

    public Result queryHotGamePlatform();

    public Result queryGamePlatformByCategory(Long categoryId);

    public IPage<Txns> queryAllGameInfo(GameInfoPageReq req);

}
