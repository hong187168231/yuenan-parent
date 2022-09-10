package com.indo.game.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.service.common.GameCommonService;
import com.indo.core.pojo.bo.MemTradingBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/test")
@Slf4j
@AllArgsConstructor
@Api(tags = "后台游戏管理接口")
public class TestController {
//    @Autowired
//    private GameCommonService iGameManageService;
//
    @ApiOperation(value = "游戏记录", httpMethod = "GET")
    @GetMapping(value = "/hello")
    @AllowAccess
    public Result<?> queryGameRecord(HttpServletRequest request) {
        String countryCode = request.getHeader("countryCode");
        System.out.println("test18:");
        System.out.println("test1:"+countryCode);
        System.out.println("test2:"+MessageUtils.get("g000007",countryCode));
        return Result.failed(MessageUtils.get("g000007",countryCode));
    }


}
