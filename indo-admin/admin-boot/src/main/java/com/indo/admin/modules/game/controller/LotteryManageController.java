package com.indo.admin.modules.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.admin.modules.game.service.ILotteryManageService;
import com.indo.admin.pojo.dto.game.manage.*;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.game.GameLotteryWinningNumber;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/lotteryYxx")
@Slf4j
@AllArgsConstructor
@Api(tags = "彩票管理-鱼虾蟹开奖管理")
public class LotteryManageController {


    @Autowired
    private ILotteryManageService iLotteryManageService;

    @ApiOperation(value = "随机生成开奖结果", httpMethod = "POST")
    @PostMapping(value = "/rgwn")
    public Result<List<GameLotteryWinningNumber>> rgwn(GameInfoPageReq req){
        log.info("随机生成开奖结果（Randomly generate winning numbers）rgwn {} req:{}", JSONObject.toJSONString(req));
        List<GameLotteryWinningNumber> list = iLotteryManageService.rgwn();
        return Result.success(list);
    }

    @ApiOperation(value = "查询开奖结果", httpMethod = "POST")
    @GetMapping(value = "/queryRgwn")
    public Result<List<GameLotteryWinningNumber>> queryRgwn(GameLotteryWinningNumberQueryDto queryDto) {
        List<GameLotteryWinningNumber> list = iLotteryManageService.queryAllGameLotteryWinningNumber(queryDto);
        return Result.success(list);
    }

    @ApiOperation(value = "修改开奖结果", httpMethod = "POST")
    @PostMapping(value = "/modifyRgwn")
    public Result modifyRgwn(GameLotteryWinningNumberReqDto req){
        log.info("modifyRgwn {} req:{}", JSONObject.toJSONString(req));
        return Result.judge(iLotteryManageService.modifyRgwn(req));
    }
}
