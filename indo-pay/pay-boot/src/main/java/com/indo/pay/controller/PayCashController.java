package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.service.IPayCashOrderService;
import com.indo.pay.service.IPayChannelConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/pay/channel")
public class PayCashController {

    @Resource
    private IPayCashOrderService iPayCashOrderService;


    @ApiOperation(value = "提现记录")
    @GetMapping(value = "/cashList")
    public Result<List<PayCashOrderVO>> cashList(@LoginUser LoginInfo loginInfo) {
        PayCashOrderDTO cashOrderDTO = new PayCashOrderDTO();
        cashOrderDTO.setUserId(loginInfo.getId());
        return iPayCashOrderService.cashRecordList(cashOrderDTO);
    }

}
