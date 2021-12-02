package com.indo.game.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.manage.IGameManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/gameManage")
@Slf4j
@AllArgsConstructor
@Api(tags = "后台游戏管理接口")
public class GameManageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IGameManageService iGameManageService;

    @ApiOperation(value = "游戏记录", httpMethod = "POST")
    @PostMapping(value = "/gameRecord")
    @AllowAccess
    public Result<?> queryGameRecord(){
        return null;
    }

    @ApiOperation(value = "游戏类别", httpMethod = "Get")
    @GetMapping(value = "/allGameCategory")
    @AllowAccess
    public Result<GameCategory> queryAllGameCategory(){
        return iGameManageService.queryAllGameCategory();
    }

    @ApiOperation(value = "新增游戏类别", httpMethod = "POST")
    @PostMapping(value = "/addGameCategory")
    @AllowAccess
    public Result<?> addGameCategory(GameCategory category){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.addGameCategory(category);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("ftagc"));
        }
        return result;
    }

    @ApiOperation(value = "修改游戏类别", httpMethod = "POST")
    @PostMapping(value = "/modifyGameCategory")
    @AllowAccess
    public Result<?> modifyGameCategory(GameCategory category){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.modifiyGameCategory(category);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("ftmgc"));
        }
        return result;
    }

    @ApiOperation(value = "删除游戏类别", httpMethod = "POST")
    @PostMapping(value = "/deleteGameCategory")
    @AllowAccess
    public Result<?> deleteGameCategory(String ids){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.deleteBatchGameCategory(getIdList(ids));
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("ftdgc"));
        }
        return result;
    }

    @ApiOperation(value = "查询所有平台", httpMethod = "Get")
    @GetMapping(value = "/allGamePlatform")
    @AllowAccess
    public Result<List<GamePlatform>> queryAllGamePlatform(){
        return iGameManageService.queryAllGamePlatform();
    }

    @ApiOperation(value = "查询热门平台", httpMethod = "Get")
    @GetMapping(value = "/allGamePlatform")
    @AllowAccess
    public Result<List<GamePlatform>> queryHotGamePlatform(){
        return iGameManageService.queryHotGamePlatform();
    }

    @ApiOperation(value = "新增平台名称", httpMethod = "POST")
    @GetMapping(value = "/addGamePlatform")
    @AllowAccess
    public Result<?> addGamePlatform(GamePlatform gamePlatform){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.addGamePlatform(gamePlatform);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("addplatform"));
        }
        return result;
    }

    @ApiOperation(value = "删除平台名称", httpMethod = "POST")
    @GetMapping(value = "/deleteGamePlatform")
    @AllowAccess
    public Result<?> deleteGamePlatform(String ids){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.deleteBatchGamePlatform(getIdList(ids));
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("deleteplatform"));
        }
        return result;
    }

    @ApiOperation(value = "修改平台名称", httpMethod = "POST")
    @GetMapping(value = "/modifyGamePlatform")
    @AllowAccess
    public Result<?> modifyGamePlatform(GamePlatform gamePlatform){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.modifiyGamePlatform(gamePlatform);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("modifyplatform"));
        }
        return result;
    }

    @ApiOperation(value = "下载地址", httpMethod = "Get")
    @GetMapping(value = "/allGameDownload")
    @AllowAccess
    public Result<GameDownload> allGameDownload(){
        return iGameManageService.queryAllGameDownload();
    }

    @ApiOperation(value = "新增下载地址", httpMethod = "POST")
    @GetMapping(value = "/addGameDownload")
    @AllowAccess
    public Result<?> addGameDownload(GameDownload gameDownload){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.addGameDownload(gameDownload);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("addgamedownload"));
        }
        return result;
    }

    @ApiOperation(value = "删除下载地址", httpMethod = "POST")
    @GetMapping(value = "/deleteGameDownload")
    @AllowAccess
    public Result<?> deleteGameDownload(String ids){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.deleteBatchGameDownload(getIdList(ids));
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("deletegamedownload"));
        }
        return result;
    }

    @ApiOperation(value = "修改下载地址", httpMethod = "POST")
    @GetMapping(value = "/modifyGameDownload")
    @AllowAccess
    public Result<?> modifyGameDownload(GameDownload gameDownload){
        Result<?> result = new Result<>();
        result.success();
        try {
            iGameManageService.modifiyGameDownload(gameDownload);
        }catch (Exception e){
            e.printStackTrace();
            result.failed(MessageUtils.get("modifygamedownload"));
        }
        return result;
    }

    public List<String> getIdList(String ids) {
        String idStrs[] = ids.split(",");
        return Arrays.asList(idStrs);
    }
}
