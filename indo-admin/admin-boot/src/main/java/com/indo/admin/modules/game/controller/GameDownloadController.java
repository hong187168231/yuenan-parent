package com.indo.admin.modules.game.controller;

import com.indo.admin.modules.game.service.IGameDownloadService;
import com.indo.admin.pojo.dto.game.manage.GameDownloadAddDto;
import com.indo.admin.pojo.dto.game.manage.GameDownloadModifyDto;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.game.pojo.entity.manage.GameDownload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/game/download")
@Slf4j
@Api(tags = "游戏管理-下载地址")
public class GameDownloadController {

    @Autowired
    private IGameDownloadService iGameDownloadService;


    @ApiOperation(value = "查询下载地址", httpMethod = "GET")
    @GetMapping(value = "/allGameDownload")
    public Result<List<GameDownload>> allGameDownload() {
        return Result.success(iGameDownloadService.queryAllGameDownload());
    }

    @ApiOperation(value = "新增下载地址", httpMethod = "POST")
    @PostMapping(value = "/addGameDownload")
    public Result addGameDownload(GameDownloadAddDto gameDownloadDto) {
        GameDownload gameDownload = new GameDownload();

        BeanUtils.copyProperties(gameDownloadDto, gameDownload);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gameDownload.setCreateTime(dateStr);
        return Result.judge(iGameDownloadService.addGameDownload(gameDownload));
    }

    @ApiOperation(value = "删除下载地址", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteGameDownload")
    public Result deleteGameDownload(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameDownloadService.deleteBatchGameDownload(list));
    }

    @ApiOperation(value = "修改下载地址", httpMethod = "POST")
    @PostMapping(value = "/modifyGameDownload")
    public Result modifyGameDownload(GameDownloadModifyDto gameDownloadDto) {
        GameDownload gameDownload = new GameDownload();
        BeanUtils.copyProperties(gameDownloadDto, gameDownload);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gameDownload.setUpdateTime(dateStr);
        return Result.judge(iGameDownloadService.modifyGameDownload(gameDownload));
    }


}
