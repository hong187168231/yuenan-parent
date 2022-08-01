package com.indo.game.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.app.GameInfoAgentRecord;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GamePlatformRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;

import java.util.List;


public interface IGameManageService {

    List<GameCategory> queryAllGameCategory();

    List<GamePlatformRecord> queryAllGamePlatform();

    List<GamePlatformRecord> queryHotGamePlatform();

    List<GamePlatformRecord> queryGamePlatformByCategory(Long categoryId);

    IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);

    Result<List<GameInfoAgentRecord>> queryAllAgentGameInfo(LoginInfo loginUser,GameInfoPageReq req);

    public List<GameParentPlatform> queryAllGameParentPlatform();

    public List<GameParentPlatform> queryHotGameParentPlatform();
}
