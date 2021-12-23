package com.indo.game.service.frontend;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;

public interface IFrontEndGameManageService {

    public Result queryAllGameCategory();

    public void addGameCategory(GameCategory category);

    public void deleteBatchGameCategory(List idList);

    public void modifiyGameCategory(GameCategory category);

    public Result queryAllGamePlatform();

    public Result queryHotGamePlatform();

    public Result queryGamePlatformByCategory(Long categoryId);

    public void addGamePlatform(GamePlatform platform);

    public void deleteBatchGamePlatform(List idList);

    public void modifiyGamePlatform(GamePlatform platform);

    public Result queryAllGameDownload();

    public void addGameDownload(GameDownload gameDownload);

    public void deleteBatchGameDownload(List idList);

    public void modifiyGameDownload(GameDownload gameDownload);

    public Result queryLanguageType();

    public Result queryGameCurrencyType();
}
