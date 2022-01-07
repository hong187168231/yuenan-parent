package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.pojo.vo.PayWayVO;
import com.indo.pay.service.IPayWayConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付方式配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Api(tags = "支付方式")
@RestController
@RequestMapping("/pay/way")
public class PayWayController {


    @Resource
    private IPayWayConfigService iPayWayConfigService;

    /**
     * 支付渠道列表
     *
     * @return
     */
    @ApiOperation(value = "支付方式列表")
    @GetMapping("/list")
    public Result<List<PayWayVO>> getPayWayList(@LoginUser LoginInfo loginInfo) {
        return Result.success(iPayWayConfigService.wayList(loginInfo));
    }

}
