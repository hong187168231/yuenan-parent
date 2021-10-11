package com.live.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.service.IPayBankConfigService;
import com.live.admin.modules.pay.service.IPayCashConfigService;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayBankConfigDto;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.vo.PayBankConfigVO;
import com.live.pay.pojo.vo.PayCashConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 银行支付配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Api(tags = "银行支付配置")
@RestController
@RequestMapping("/api/v1/pay/bank")
public class PayBankConfigController {

    @Resource
    private IPayBankConfigService payBankConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayBankConfigVO>> getPayBankList(PayBankConfigDto dto) {
        Page<PayBankConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayBankConfigVO> list = payBankConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getPayBank(PayBankConfigDto dto) {
        if(Objects.isNull(dto.getPayBankId())){
            Result.failed("出款ID不能为空");
        }
        return Result.success(payBankConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增银行支付配置")
    @PostMapping(value = "/insert")
    public Result insertPayBank(PayBankConfigDto dto){

        return payBankConfigService.insertPayBank(dto);
    }

    @ApiOperation(value="编辑银行支付配置")
    @PutMapping(value = "/update")
    public Result updatePayBank(PayBankConfigDto dto){
        if(Objects.isNull(dto.getPayBankId())){
            Result.failed("银行支付ID不能为空");
        }
        return payBankConfigService.updatePayBank(dto);
    }

    @ApiOperation(value="删除银行支付配置")
    @DeleteMapping(value = "/delete")
    public Result deletePayBank(PayBankConfigDto dto){
        if(Objects.isNull(dto.getPayBankId())){
            Result.failed("银行支付ID不能为空");
        }
        return payBankConfigService.deletePayBank(dto);
    }

    @ApiOperation(value="停启银行支付配置")
    @PutMapping(value = "/stopstart")
    public Result stopStartPayBank(PayBankConfigDto dto){
        if(Objects.isNull(dto.getPayBankId())){
            Result.failed("银行支付ID不能为空");
        }
        return payBankConfigService.stopStartPayBank(dto);
    }

}
