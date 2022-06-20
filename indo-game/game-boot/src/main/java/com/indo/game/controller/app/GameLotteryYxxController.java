package com.indo.game.controller.app;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.lottery.GameLotteryOrderDto;
import com.indo.game.service.lottery.YxxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/games/lotteryYxx")
@Slf4j
@Api(tags = "彩票-鱼虾蟹")
public class GameLotteryYxxController {
    @Autowired
    private YxxService yxxService;

    @ApiOperation(value = "鱼虾蟹下注", httpMethod = "POST")
    @PostMapping(value = "/lotteryYxx",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result lotteryYxx(@LoginUser LoginInfo loginUser, GameLotteryOrderDto gameLotteryOrderDto,
                           HttpServletRequest request) throws InterruptedException {
        return yxxService.yxxOrder(loginUser,gameLotteryOrderDto);
    }

}
