package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.modules.pay.service.IPayBankService;
import com.indo.admin.pojo.vo.MsgStationLetterVO;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankDTO;
import com.indo.pay.pojo.vo.PayBankVO;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.dto.StationLetterAddDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 支付银行表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@RestController
@RequestMapping("/pay/bank")
public class PayBankController {


    @Autowired
    private IPayBankService iPayBankService;

    @ApiOperation(value = "支付银行管理列表")
    @GetMapping(value = "/list")
    public Result<List<PayBankVO>> list(PayBankDTO bankDTO) {
        return iPayBankService.bankList(bankDTO);
    }

    @ApiOperation(value = "增加支付银行")
    @PostMapping(value = "/add")
    public Result add(PayBankDTO bankDTO) {
        boolean flag = iPayBankService.addBank(bankDTO);
        return Result.judge(flag);
    }


    @ApiOperation(value = "删除支付银行")
    @ApiImplicitParam(name = "ids", value = "主键ID集合，以,分割拼接字符串", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")


    public Result delete(@PathVariable String ids) {
        boolean status = iPayBankService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.judge(status);
    }

}