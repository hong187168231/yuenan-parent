package com.indo.game.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.manage.IGameManageService;
import com.indo.user.pojo.entity.MemBaseinfo;
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
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
@Api(tags = "后台游戏管理接口")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GameCommonService iGameManageService;

    @ApiOperation(value = "游戏记录", httpMethod = "POST")
    @GetMapping(value = "/hello")
    @AllowAccess
    public Result<?> queryGameRecord(){
        MemBaseinfo memBaseinfo = iGameManageService.getByAccountNo("1");
        return  Result.success(memBaseinfo);
    }

}
