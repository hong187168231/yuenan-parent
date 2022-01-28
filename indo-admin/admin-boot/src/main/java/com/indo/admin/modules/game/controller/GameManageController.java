package com.indo.admin.modules.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.entity.manage.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game/platform")
@Slf4j
@AllArgsConstructor
@Api(tags = "游戏管理-游戏平台")
public class GameManageController {


    @Autowired
    private IGameManageService iGameManageService;

    @ApiOperation(value = "查询所有平台记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfoCount")
    public Result<List<GameStatiRecord>> queryAllGameInfoCount(GameInfoPageReq req){
        log.info("查询所有平台记录allGameInfoCount {} req:{}", JSONObject.toJSONString(req));
        IPage<GameStatiRecord> result = iGameManageService.queryAllGameInfoCount(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfo")
    public Result<List<GameInfoRecord>> queryAllGameInfo(GameInfoPageReq req){
        log.info("查询所有平台记录allGameInfoCount {} req:{}", JSONObject.toJSONString(req));
        IPage<GameInfoRecord> result = iGameManageService.queryAllGameInfo(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "游戏类别", httpMethod = "GET")
    @GetMapping(value = "/allGameCategory")
    public Result<GameCategory> queryAllGameCategory() {
        return iGameManageService.queryAllGameCategory();
    }

    @ApiOperation(value = "新增游戏类别", httpMethod = "POST")
    @PostMapping(value = "/addGameCategory")
    public Result addGameCategory(GameCategory category) {
        return Result.judge(iGameManageService.addGameCategory(category));
    }

    @ApiOperation(value = "修改游戏类别", httpMethod = "POST")
    @PostMapping(value = "/modifyGameCategory")
    public Result modifyGameCategory(GameCategory category) {
        return Result.judge(iGameManageService.modifyGameCategory(category));
    }

    @ApiOperation(value = "删除游戏类别", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteGameCategory")
    public Result deleteGameCategory(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameManageService.deleteBatchGameCategory(list));
    }

    @ApiOperation(value = "查询所有平台", httpMethod = "POST")
    @PostMapping(value = "/queryAllGamePlatform")
    public Result<List<GamePlatform>> queryAllGamePlatform(GamePlatformPageReq req) {
        log.info("查询所有平台 queryAllGamePlatform {} req:{}", JSONObject.toJSONString(req));
        IPage<GamePlatform> result = iGameManageService.queryAllGamePlatform(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询热门平台", httpMethod = "GET")
    @GetMapping(value = "/queryHotGamePlatform")
    public Result<List<GamePlatform>> queryHotGamePlatform() {
        return iGameManageService.queryHotGamePlatform();
    }

    @ApiOperation(value = "新增平台名称", httpMethod = "POST")
    @GetMapping(value = "/addGamePlatform")
    public Result addGamePlatform(GamePlatform gamePlatform) {
        return Result.judge(iGameManageService.addGamePlatform(gamePlatform));
    }

    @ApiOperation(value = "删除平台名称", httpMethod = "POST")
    @GetMapping(value = "/deleteGamePlatform")
    public Result deleteGamePlatform(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameManageService.deleteBatchGamePlatform(list));
    }

    @ApiOperation(value = "修改平台名称", httpMethod = "POST")
    @GetMapping(value = "/modifyGamePlatform")
    public Result modifyGamePlatform(GamePlatform gamePlatform) {
        return Result.judge(iGameManageService.modifiyGamePlatform(gamePlatform));
    }

    @ApiOperation(value = "语言标准代码", httpMethod = "GET")
    @GetMapping(value = "/queryLanguageType")
    public Result<GameLanguageType> queryLanguageType() {
        return iGameManageService.queryLanguageType();
    }

    @ApiOperation(value = "币种代码", httpMethod = "GET")
    @GetMapping(value = "/queryCurrencyType")
    public Result<GameCurrencyType> queryGameCurrencyType() {
        return iGameManageService.queryGameCurrencyType();
    }
}
