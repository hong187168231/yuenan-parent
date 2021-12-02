package com.indo.game.service.manage;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;

public interface IGameManageService {

    public Result<GameCategory> queryAllGameCategory();

    public void addGameCategory(GameCategory category);

    public void deleteBatchGameCategory(List idList);

    public void modifiyGameCategory(GameCategory category);

    public Result<List<GamePlatform>> queryAllGamePlatform();

    public Result<List<GamePlatform>> queryHotGamePlatform();

    public void addGamePlatform(GamePlatform platform);

    public void deleteBatchGamePlatform(List idList);

    public void modifiyGamePlatform(GamePlatform platform);

    public Result<GameDownload> queryAllGameDownload();

    public void addGameDownload(GameDownload gameDownload);

    public void deleteBatchGameDownload(List idList);

    public void modifiyGameDownload(GameDownload gameDownload);
}
