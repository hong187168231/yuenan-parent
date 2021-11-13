package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.service.IPayChannelConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@RestController
@RequestMapping("/pay/channel")
public class PayChannelController {

    @Resource
    private IPayChannelConfigService channelConfigService;

    /**
     * 支付渠道列表
     * @return
     */
    @GetMapping("/list")
    public Result getPayBankList(@LoginUser LoginInfo loginInfo){
        return Result.success(channelConfigService.channelList(loginInfo));
    }

}
