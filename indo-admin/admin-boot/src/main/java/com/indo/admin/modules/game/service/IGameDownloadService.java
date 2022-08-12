package com.indo.admin.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.game.GameDownload;

import java.util.List;

public interface IGameDownloadService extends IService<GameDownload> {


     List<GameDownload> queryAllGameDownload();

    boolean addGameDownload(GameDownload gameDownload);

    boolean deleteBatchGameDownload(List<String> list);

    boolean modifyGameDownload(GameDownload gameDownload);
}
