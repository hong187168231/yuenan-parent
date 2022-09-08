package com.indo.game.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.vo.game.app.GameInfoAgentRecord;
import com.indo.core.pojo.vo.game.app.GameInfoRecord;
import com.indo.core.pojo.vo.game.app.GamePlatformRecord;
import com.indo.core.pojo.vo.game.app.GameStatiRecord;

import java.util.List;


public interface IGameManageService {

    List<GameCategory> queryAllGameCategory();

    List<GamePlatformRecord> queryAllGamePlatform();

    List<GamePlatformRecord> queryHotGamePlatform();

    List<GamePlatformRecord> queryGamePlatformByCategory(Long categoryId);

    IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);

    Result<List<GameInfoAgentRecord>> queryAllAgentGameInfo(LoginInfo loginUser,GameInfoPageReq req,String countryCode);

    public List<GameParentPlatform> queryAllGameParentPlatform();

    public List<GameParentPlatform> queryHotGameParentPlatform();
}
