package com.indo.admin.modules.game.service;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.*;

import java.util.List;
import java.util.Set;

public interface IGameManageService {

    Result<GameCategory> queryAllGameCategory();

    boolean addGameCategory(GameCategory category);

    boolean deleteBatchGameCategory(Set<Long> ids);

    boolean modifiyGameCategory(GameCategory category);

    Result<List<GamePlatform>> queryAllGamePlatform();

    Result<List<GamePlatform>> queryHotGamePlatform();

    boolean addGamePlatform(GamePlatform platform);

    boolean deleteBatchGamePlatform(Set<Long> ids);

    boolean modifiyGamePlatform(GamePlatform platform);

    Result<GameLanguageType> queryLanguageType();

    Result<GameCurrencyType> queryGameCurrencyType();
}
