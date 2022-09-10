package com.indo.admin.modules.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.admin.api.GameFeignClient;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GameParentPlatformPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.IPAddressUtil;
import com.indo.core.pojo.entity.game.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private GameFeignClient gameFeignClient;

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
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        category.setCreateTime(dateStr);
        return Result.judge(iGameManageService.addGameCategory(category));
    }

    @ApiOperation(value = "修改游戏类别", httpMethod = "POST")
    @PostMapping(value = "/modifyGameCategory")
    public Result modifyGameCategory(GameCategory category) {
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        category.setUpdateTime(dateStr);
        return Result.judge(iGameManageService.modifyGameCategory(category));
    }

    @ApiOperation(value = "删除游戏类别", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteGameCategory")
    public Result deleteGameCategory(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameManageService.deleteBatchGameCategory(list));
    }

    @ApiOperation(value = "查询所有平台游戏", httpMethod = "POST")
    @PostMapping(value = "/queryAllGamePlatform")
    public Result<List<GamePlatform>> queryAllGamePlatform(GamePlatformPageReq req) {
        log.info("查询所有平台游戏 queryAllGamePlatform {} req:{}", JSONObject.toJSONString(req));
        IPage<GamePlatform> result = iGameManageService.queryAllGamePlatform(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有热门游戏", httpMethod = "GET")
    @GetMapping(value = "/queryHotGamePlatform")
    public Result<List<GamePlatform>> queryHotGamePlatform() {
        return iGameManageService.queryHotGamePlatform();
    }

    @ApiOperation(value = "新增平台游戏", httpMethod = "POST")
    @PostMapping(value = "/addGamePlatform")
    public Result addGamePlatform(GamePlatform gamePlatform) {
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gamePlatform.setCreateTime(dateStr);
        return Result.judge(iGameManageService.addGamePlatform(gamePlatform));
    }

    @ApiOperation(value = "删除平台游戏", httpMethod = "POST")
    @PostMapping(value = "/deleteGamePlatform")
    public Result deleteGamePlatform(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameManageService.deleteBatchGamePlatform(list));
    }

    @ApiOperation(value = "修改平台游戏", httpMethod = "POST")
    @PostMapping(value = "/modifyGamePlatform")
    public Result modifyGamePlatform(GamePlatform gamePlatform) {
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gamePlatform.setUpdateTime(dateStr);
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


    @ApiOperation(value = "查询所有平台", httpMethod = "POST")
    @PostMapping(value = "/queryAllGameParentPlatform")
    public Result<List<GameParentPlatform>> queryAllGameParentPlatform(GameParentPlatformPageReq req) {
        log.info("查询所有平台游戏 queryAllGameParentPlatform {} req:{}", JSONObject.toJSONString(req));
        IPage<GameParentPlatform> result = iGameManageService.queryAllGameParentPlatform(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有热门平台", httpMethod = "GET")
    @GetMapping(value = "/queryHotGameParentPlatform")
    public Result<List<GameParentPlatform>> queryHotGameParentPlatform() {
        return iGameManageService.queryHotGameParentPlatform();
    }

    @ApiOperation(value = "新增平台", httpMethod = "POST")
    @PostMapping(value = "/addGameParentPlatform")
    public Result addGameParentPlatform(GameParentPlatform gameParentPlatform) {
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gameParentPlatform.setCreateTime(dateStr);
        return Result.judge(iGameManageService.addGameParentPlatform(gameParentPlatform));
    }

    @ApiOperation(value = "删除平台", httpMethod = "POST")
    @PostMapping(value = "/deleteGameParentPlatform")
    public Result deleteGameParentPlatform(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return Result.judge(iGameManageService.deleteBatchGameParentPlatform(list));
    }

    @ApiOperation(value = "修改平台", httpMethod = "POST")
    @PostMapping(value = "/modifyGameParentPlatform")
    public Result modifyGameParentPlatform(GameParentPlatform gameParentPlatform) {
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        gameParentPlatform.setUpdateTime(dateStr);
        return Result.judge(iGameManageService.modifiyGameParentPlatform(gameParentPlatform));
    }

    @ApiOperation(value = "强迫登出游戏", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户账号 ", paramType = "query", dataType = "string", required = true)
    })
    @PostMapping(value ="/gameLogout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result gameLogout(String account, HttpServletRequest request){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        String ip = IPAddressUtil.getIpAddress(request);
        log.info("强迫登出游戏gameLogout loginUser:{}, ip:{}", account,ip);
        return gameFeignClient.gamelogout( account);
    }
}
