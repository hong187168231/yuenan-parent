package com.indo.game.service.frontend;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;

public interface IFrontEndGameManageService {

    public Result queryAllGameCategory();

    public Result queryAllGamePlatform();

    public Result queryHotGamePlatform();

    public Result queryGamePlatformByCategory(Long categoryId);

}
