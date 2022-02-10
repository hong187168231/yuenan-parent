package com.indo.admin.modules.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GameParentPlatformPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;

import java.util.List;

public interface IGameManageService {

    Result queryAllGameCategory();

    boolean addGameCategory(GameCategory category);

    boolean deleteBatchGameCategory(List<String> list);

    boolean modifyGameCategory(GameCategory category);

    IPage<GamePlatform> queryAllGamePlatform(GamePlatformPageReq req);

    Result<List<GamePlatform>> queryHotGamePlatform();

    boolean addGamePlatform(GamePlatform platform);

    boolean deleteBatchGamePlatform(List<String> list);

    boolean modifiyGamePlatform(GamePlatform platform);

    Result queryLanguageType();

    Result queryGameCurrencyType();

    IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);

    IPage<GameParentPlatform> queryAllGameParentPlatform(GameParentPlatformPageReq req);

    Result<List<GameParentPlatform>> queryHotGameParentPlatform();

    boolean addGameParentPlatform(GameParentPlatform gameParentPlatform);

    boolean deleteBatchGameParentPlatform(List<String> list);

    boolean modifiyGameParentPlatform(GameParentPlatform gameParentPlatform);
}
