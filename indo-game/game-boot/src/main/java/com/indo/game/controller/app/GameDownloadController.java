package com.indo.game.controller.app;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.service.app.IGameDownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/download")
@Slf4j
@Api(tags = "游戏管理-下载地址")
public class GameDownloadController {

    @Autowired
    private IGameDownloadService iGameDownloadService;


    @ApiOperation(value = "下载地址", httpMethod = "GET")
    @GetMapping(value = "/allGameDownload")
    @AllowAccess
    public Result<List<GameDownload>> allGameDownload() {
        return Result.success(iGameDownloadService.queryAllGameDownload());
    }

}
