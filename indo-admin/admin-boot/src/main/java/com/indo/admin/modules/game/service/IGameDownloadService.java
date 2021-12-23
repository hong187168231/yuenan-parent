package com.indo.admin.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.*;

import java.util.List;
import java.util.Set;

public interface IGameDownloadService extends IService<GameDownload> {


     List<GameDownload> queryAllGameDownload();

    boolean addGameDownload(GameDownload gameDownload);

    boolean deleteBatchGameDownload(Set<Long> ids);

    boolean modifiyGameDownload(GameDownload gameDownload);
}
