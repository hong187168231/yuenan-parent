package com.indo.game.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.*;
import com.indo.game.pojo.vo.app.GameInfoAgentRecord;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GamePlatformRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;
import com.indo.game.service.app.IGameManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return Result.success(iGameManageService.queryAllGameCategory());
    }

    @ApiOperation(value = "查询所有平台游戏", httpMethod = "GET")
    @GetMapping(value = "/queryAllGamePlatform")
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryAllGamePlatform() {
        return Result.success(iGameManageService.queryAllGamePlatform());
    }

    @ApiOperation(value = "查询热门游戏", httpMethod = "GET")
    @GetMapping(value = "/queryHotGamePlatform")
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryHotGamePlatform() {
        return Result.success(iGameManageService.queryHotGamePlatform());
    }

    @ApiOperation(value = "依据类别查询平台游戏", httpMethod = "GET")
    @GetMapping(value = "/queryGamePlatformByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "游戏类别ID ", paramType = "query", dataType = "int", required = true)
    })
    @AllowAccess
    @ResponseBody
    public Result<List<GamePlatformRecord>> queryGamePlatformByCategory(@RequestParam("categoryId") Long categoryId) {
        return Result.success(iGameManageService.queryGamePlatformByCategory(categoryId));
    }

    @ApiOperation(value = "查询所有平台记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfoCount")
    @ResponseBody
    public Result<List<GameStatiRecord>> queryAllGameInfoCount(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询所有平台记录allGameInfoCount {} req:{}", JSONObject.toJSONString(req));
        IPage<GameStatiRecord> result = iGameManageService.queryAllGameInfoCount(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfo")
    @ResponseBody
    public Result<List<GameInfoRecord>> queryAllGameInfo(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询所有平台记录allGameInfoCount {} req:{}", JSONObject.toJSONString(req));
        IPage<GameInfoRecord> result = iGameManageService.queryAllGameInfo(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询代理游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allAgentGameInfo")
    @ResponseBody
    public Result<List<GameInfoAgentRecord>> queryAllAgentGameInfo(@LoginUser LoginInfo loginUser, GameInfoPageReq req) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        req.setUserAcct(loginUser.getAccount());
        log.info("查询所有平台记录allGameInfoCount {} req:{}", JSONObject.toJSONString(req));
        IPage<GameInfoAgentRecord> result = iGameManageService.queryAllAgentGameInfo(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有平台", httpMethod = "GET")
    @GetMapping(value = "/queryAllGameParentPlatform")
    @AllowAccess
    @ResponseBody
    public Result<List<GameParentPlatform>> queryAllGameParentPlatform() {
        return Result.success(iGameManageService.queryAllGameParentPlatform());
    }

    @ApiOperation(value = "查询热门平台", httpMethod = "GET")
    @GetMapping(value = "/queryHotGameParentPlatform")
    @AllowAccess
    @ResponseBody
    public Result<List<GameParentPlatform>> queryHotGameParentPlatform() {
        return Result.success(iGameManageService.queryHotGameParentPlatform());
    }

}
