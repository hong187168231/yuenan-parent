package com.indo.admin.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameCurrencyType;
import com.indo.game.pojo.entity.manage.GameLanguageType;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;
import java.util.Set;

public interface IGameManageService {

    Result<GameCategory> queryAllGameCategory();

    boolean addGameCategory(GameCategory category);

    boolean deleteBatchGameCategory(Set<Long> ids);

    boolean modifiyGameCategory(GameCategory category);

    public IPage<GamePlatform> queryAllGamePlatform(GamePlatformPageReq req);

    Result<List<GamePlatform>> queryHotGamePlatform();

    boolean addGamePlatform(GamePlatform platform);

    boolean deleteBatchGamePlatform(Set<Long> ids);

    boolean modifiyGamePlatform(GamePlatform platform);

    Result<GameLanguageType> queryLanguageType();

    Result<GameCurrencyType> queryGameCurrencyType();

    public IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    public IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);
}
