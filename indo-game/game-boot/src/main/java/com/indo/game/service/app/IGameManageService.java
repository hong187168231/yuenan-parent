package com.indo.game.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.vo.app.GameInfoAgentRecord;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;

import java.util.List;


public interface IGameManageService {

    List<GameCategory> queryAllGameCategory();

    List<GamePlatform> queryAllGamePlatform();

    List<GamePlatform> queryHotGamePlatform();

    List<GamePlatform> queryGamePlatformByCategory(Long categoryId);

    IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req);

    IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req);

    IPage<GameInfoAgentRecord> queryAllAgentGameInfo(GameInfoPageReq req);

}
