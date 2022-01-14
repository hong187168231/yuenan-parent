package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.vo.MemBankVO;
import com.indo.pay.pojo.vo.PayBankVO;
import com.indo.pay.service.IPayBankService;
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
@Api(tags = "银行卡")
@RestController
@RequestMapping("/pay/memBank")
public class PayBankController {

    @Resource
    private IMemBankService iMemBankService;

    @Resource
    private IPayBankService iPayBankService;

    /**
     * 支付渠道列表
     *
     * @return
     */
    @ApiOperation(value = "会员已绑定银行卡")
    @GetMapping("/bindlist")
    public Result<List<MemBankVO>> bindlist(@LoginUser LoginInfo loginInfo) {
        return Result.success(iMemBankService.memBankList(loginInfo));
    }


    @ApiOperation(value = "会员可绑定银行列表")
    @GetMapping("/bankList")
    public Result<List<PayBankVO>> bankList() {
        return Result.success(iPayBankService.bankList());
    }

}
