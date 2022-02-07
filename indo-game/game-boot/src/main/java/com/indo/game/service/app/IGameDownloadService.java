package com.indo.game.service.app;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.game.pojo.entity.manage.GameDownload;

import java.util.List;

public interface IGameDownloadService extends IService<GameDownload> {


     List<GameDownload> queryAllGameDownload();

}
