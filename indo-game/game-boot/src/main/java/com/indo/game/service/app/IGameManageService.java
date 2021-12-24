package com.indo.game.service.app;

import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameCurrencyType;
import com.indo.game.pojo.entity.manage.GameLanguageType;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;
import java.util.Set;

public interface IGameManageService {

    Result<GameCategory> queryAllGameCategory();

    Result<List<GamePlatform>> queryAllGamePlatform();

    Result<List<GamePlatform>> queryHotGamePlatform();

    Result<GameCurrencyType> queryGameCurrencyType();

    Result queryGamePlatformByCategory(Long categoryId);
}
