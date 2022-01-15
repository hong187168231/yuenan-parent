package com.indo.game.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.pojo.bo.MemTradingBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private GameCommonService iGameManageService;

    @ApiOperation(value = "游戏记录", httpMethod = "GET")
    @GetMapping(value = "/hello")
    @AllowAccess
    public Result<?> queryGameRecord() {
        System.out.println(OpenAPIProperties.PROXY_HOST_NAME);
        MemTradingBO memBaseinfo = new MemTradingBO();
        memBaseinfo.setId(42L);
        memBaseinfo.setAccount("swuserid");
        MemTradingBO memTradingBO = iGameManageService.getMemTradingInfo("puff");
        iGameManageService.updateUserBalance(memBaseinfo, new BigDecimal("20.0"), GoldchangeEnum.REFUND, TradingEnum.INCOME);
        return Result.success(memBaseinfo);
    }


}
