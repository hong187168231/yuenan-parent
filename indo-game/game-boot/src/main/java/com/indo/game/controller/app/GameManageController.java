package com.indo.game.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.vo.game.app.GameInfoAgentRecord;
import com.indo.core.pojo.vo.game.app.GameInfoRecord;
import com.indo.core.pojo.vo.game.app.GamePlatformRecord;
import com.indo.core.pojo.vo.game.app.GameStatiRecord;
import com.indo.game.service.app.IGameManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frontEndGame")
@Slf4j
@Api(tags = "前端查询游戏相关接口")
public class GameManageController {


    @Autowired
    private IGameManageService iGameManageService;

    @ApiOperation(value = "查询所有游戏类别", httpMethod = "GET")
    @GetMapping(value = "/allGameCategory",produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Result<List<GameCategory>> queryAllGameCategory() {
        Result<List<GameCategory>> result = Result.success(iGameManageService.queryAllGameCategory());
        log.info("查询所有游戏类别queryAllGameCategory返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "查询所有平台游戏", httpMethod = "GET")
    @GetMapping(value = "/queryAllGamePlatform",produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryAllGamePlatform() {
        Result<List<GamePlatformRecord>> result = Result.success(iGameManageService.queryAllGamePlatform());
        log.info("查询所有平台游戏queryAllGamePlatform返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "查询热门游戏", httpMethod = "GET")
    @GetMapping(value = "/queryHotGamePlatform",produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryHotGamePlatform() {
        Result<List<GamePlatformRecord>> result = Result.success(iGameManageService.queryHotGamePlatform());
        log.info("查询热门游戏queryHotGamePlatform返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "依据类别查询平台游戏", httpMethod = "GET")
    @GetMapping(value = "/queryGamePlatformByCategory",produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "游戏类别ID ", paramType = "query", dataType = "int", required = true)
    })
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryGamePlatformByCategory(@RequestParam("categoryId") Long categoryId) {
        log.info("依据类别查询平台游戏queryGamePlatformByCategory 请求 categoryId:{}", JSONObject.toJSONString(categoryId));
        Result<List<GamePlatformRecord>> result = Result.success(iGameManageService.queryGamePlatformByCategory(categoryId));
        log.info("依据类别查询平台游戏queryGamePlatformByCategory返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "查询所有平台记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfoCount",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<List<GameStatiRecord>> queryAllGameInfoCount(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询所有平台记录allGameInfoCount 请求 req:{}", JSONObject.toJSONString(req));
        IPage<GameStatiRecord> result = iGameManageService.queryAllGameInfoCount(req);
        log.info("查询所有平台记录queryAllGameInfoCount返回 result:{}", result);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<List<GameInfoRecord>> queryAllGameInfo(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询所有游戏记录queryAllGameInfo请求 req:{}", JSONObject.toJSONString(req));
        IPage<GameInfoRecord> result = iGameManageService.queryAllGameInfo(req);
        log.info("查询所有游戏记录queryAllGameInfo返回 result:{}", result);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询代理游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allAgentGameInfo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<List<GameInfoAgentRecord>> queryAllAgentGameInfo(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询代理游戏记录queryAllAgentGameInfo请求 req:{}", JSONObject.toJSONString(req));
        Result<List<GameInfoAgentRecord>> result = iGameManageService.queryAllAgentGameInfo(loginUser,req);
        log.info("查询代理游戏记录queryAllAgentGameInfo返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "查询所有平台", httpMethod = "GET")
    @GetMapping(value = "/queryAllGameParentPlatform",produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Result<List<GameParentPlatform>> queryAllGameParentPlatform() {
        Result<List<GameParentPlatform>> result = Result.success(iGameManageService.queryAllGameParentPlatform());
        log.info("查询所有平台queryAllGameParentPlatform返回 result:{}", result);
        return result;
    }

    @ApiOperation(value = "查询热门平台", httpMethod = "GET")
    @GetMapping(value = "/queryHotGameParentPlatform",produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Result<List<GameParentPlatform>> queryHotGameParentPlatform() {
        Result<List<GameParentPlatform>> result = Result.success(iGameManageService.queryHotGameParentPlatform());
        log.info("查询热门平台queryHotGameParentPlatform返回 result:{}", result);
        return result;
    }

}
