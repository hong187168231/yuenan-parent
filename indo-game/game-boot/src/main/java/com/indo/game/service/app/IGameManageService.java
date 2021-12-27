package com.indo.game.service.app;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;

public interface IGameManageService {

    public Result queryAllGameCategory();

    public Result queryAllGamePlatform();

    public Result queryHotGamePlatform();

    public Result queryGamePlatformByCategory(Long categoryId);

}
