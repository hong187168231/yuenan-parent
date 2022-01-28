package com.indo.admin.modules.game.controller;

import com.indo.admin.modules.game.service.IGameDownloadService;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/game/download")
@Slf4j
@Api(tags = "游戏管理-下载地址")
public class GameDownloadController {

    @Autowired
    private IGameDownloadService iGameDownloadService;


    @ApiOperation(value = "下载地址", httpMethod = "GET")
    @GetMapping(value = "/allGameDownload")
    public Result<List<GameDownload>> allGameDownload() {
        return Result.success(iGameDownloadService.queryAllGameDownload());
    }

    @ApiOperation(value = "新增下载地址", httpMethod = "POST")
    @PostMapping(value = "/addGameDownload")
    public Result addGameDownload(GameDownload gameDownload) {
        return Result.judge(iGameDownloadService.addGameDownload(gameDownload));
    }

    @ApiOperation(value = "删除下载地址", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteGameDownload")
    public Result deleteGameDownload(Set<Long> ids) {
        return Result.judge(iGameDownloadService.deleteBatchGameDownload(ids));
    }

    @ApiOperation(value = "修改下载地址", httpMethod = "POST")
    @GetMapping(value = "/modifyGameDownload")
    public Result modifyGameDownload(GameDownload gameDownload) {
        return Result.judge(iGameDownloadService.modifyGameDownload(gameDownload));
    }


}
