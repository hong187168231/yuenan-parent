package com.indo.user.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.gift.GiftReceiveReq;
import com.indo.user.service.IMemGiftReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 活动 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */
@Api(tags = "app礼金接口")
@RestController
@RequestMapping("/api/v1/users/gift")
public class GiftController {

    @Resource
    private IMemGiftReceiveService iMemGiftReceiveService;

    @ApiOperation(value = "礼金领取")
    @PostMapping(value = "/receive")
    public Result add(GiftReceiveReq giftReceiveReq, @LoginUser LoginInfo loginInfo) {
        return Result.judge(iMemGiftReceiveService.saveMemGiftReceive(giftReceiveReq, loginInfo));
    }


}
