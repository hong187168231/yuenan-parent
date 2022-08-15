package com.indo.game.service.app;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.game.GameDownload;

import java.util.List;

public interface IGameDownloadService extends IService<GameDownload> {


     List<GameDownload> queryAllGameDownload();

}
