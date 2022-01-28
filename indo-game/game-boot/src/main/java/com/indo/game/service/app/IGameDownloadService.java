package com.indo.game.service.app;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IGameDownloadService extends IService<GameDownload> {


     List<GameDownload> queryAllGameDownload();

}
