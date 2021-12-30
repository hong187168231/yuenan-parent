//package com.indo.game.controller;
//
//import com.indo.common.annotation.AllowAccess;
//import com.indo.common.enums.GoldchangeEnum;
//import com.indo.common.enums.TradingEnum;
//import com.indo.common.result.Result;
//import com.indo.game.service.common.GameCommonService;
//import com.indo.user.pojo.entity.MemBaseinfo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//
//@RestController
//@RequestMapping("/test")
//@Slf4j
//@AllArgsConstructor
//@Api(tags = "后台游戏管理接口")
//public class TestController {
//    @Autowired
//    private GameCommonService iGameManageService;
//
//    @ApiOperation(value = "游戏记录", httpMethod = "POST")
//    @GetMapping(value = "/hello")
//    @AllowAccess
//    public Result<?> queryGameRecord() {
//        MemBaseinfo memBaseinfo = new MemBaseinfo();
//        memBaseinfo.setId(42L);
//        memBaseinfo.setAccount("swuserid");
//        iGameManageService.updateUserBalance(memBaseinfo, new BigDecimal("20.0"), GoldchangeEnum.REFUND, TradingEnum.INCOME);
//        return Result.success(memBaseinfo);
//    }
//
//
//}
