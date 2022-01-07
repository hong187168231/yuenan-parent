package com.indo.game.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.*;
import com.indo.game.pojo.vo.app.GameInfoRecord;
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
@AllArgsConstructor
@Api(tags = "前端查询游戏相关接口")
public class GameManageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IGameManageService iFrontEndGameManageService;

    @ApiOperation(value = "查询所有游戏类别", httpMethod = "GET")
    @GetMapping(value = "/allGameCategory")
    @AllowAccess
    public Result<GameCategory> queryAllGameCategory(){
        return iFrontEndGameManageService.queryAllGameCategory();
    }

    @ApiOperation(value = "查询所有游戏平台", httpMethod = "GET")
    @GetMapping(value = "/queryAllGamePlatform")
    @AllowAccess
    public Result<List<GamePlatform>> queryAllGamePlatform(){
        return iFrontEndGameManageService.queryAllGamePlatform();
    }

    @ApiOperation(value = "查询热门游戏平台", httpMethod = "GET")
    @GetMapping(value = "/queryHotGamePlatform")
    @AllowAccess
    public Result<List<GamePlatform>> queryHotGamePlatform(){
        return iFrontEndGameManageService.queryHotGamePlatform();
    }

    @ApiOperation(value = "依据类别查询游戏平台", httpMethod = "GET")
    @GetMapping(value = "/queryGamePlatformByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "游戏类别ID ", paramType = "query", dataType = "int", required = true)
    })
    @AllowAccess
    public Result<List<GamePlatform>> queryGamePlatformByCategory(@RequestParam("categoryId") Long categoryId){
        return iFrontEndGameManageService.queryGamePlatformByCategory(categoryId);
    }

    @ApiOperation(value = "查询所有平台记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfoCount")
    @ResponseBody
    public Result<List<GameStatiRecord>> queryAllGameInfoCount(@LoginUser LoginInfo loginUser, GameInfoPageReq req){
        req.setUserAcct(loginUser.getAccount());
        IPage<GameStatiRecord> result = iFrontEndGameManageService.queryAllGameInfoCount(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "查询所有游戏记录", httpMethod = "POST")
    @PostMapping(value = "/allGameInfo")
    @ResponseBody
    public Result<List<GameInfoRecord>> queryAllGameInfo(@LoginUser LoginInfo loginUser, GameInfoPageReq req){
        req.setUserAcct(loginUser.getAccount());
        IPage<GameInfoRecord> result = iFrontEndGameManageService.queryAllGameInfo(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
