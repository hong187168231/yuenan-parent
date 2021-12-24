package com.indo.game.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.pojo.entity.MemBaseinfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public Result<?> queryGameRecord() {
        MemBaseinfo memBaseinfo = iGameManageService.getByAccountNo("HansMark");
        return Result.success(memBaseinfo);
    }

    @ApiOperation(value = "游戏记录", httpMethod = "GET")
    @GetMapping(value = "/hello2")
    @AllowAccess
    public Result<?> queryGameRecord2() {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(1L);
        memBaseinfo.setAccountNo("1");
        iGameManageService.updateUserBalance(memBaseinfo, new BigDecimal(22), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
        return Result.success(memBaseinfo);
    }

}
