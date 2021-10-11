package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.vo.PayChannelConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Api(tags = "支付渠道配置")
@RestController
@RequestMapping("/api/v1/pay/channel")
public class PayChannelConfigController {

    @Resource
    private IPayChannelConfigService payChannelConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayChannelConfigVO>> getPayBankList(PayChannelConfigDto dto) {
        Page<PayChannelConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayChannelConfigVO> list = payChannelConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getPayBank(PayChannelConfigDto dto) {
        if(Objects.isNull(dto.getPayChannelId())){
            Result.failed("支付渠道ID不能为空");
        }
        return Result.success(payChannelConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增支付渠道配置")
    @PostMapping(value = "/insert")
    public Result insertPayChannel(PayChannelConfigDto dto){

        return payChannelConfigService.insertPayChannel(dto);
    }

    @ApiOperation(value="编辑支付渠道配置")
    @PutMapping(value = "/update")
    public Result updatePayChannel(PayChannelConfigDto dto){
        if(Objects.isNull(dto.getPayChannelId())){
            Result.failed("支付渠道ID不能为空");
        }
        return payChannelConfigService.updatePayChannel(dto);
    }

    @ApiOperation(value="删除支付渠道配置")
    @DeleteMapping(value = "/delete")
    public Result deletePayChannel(PayChannelConfigDto dto){
        if(Objects.isNull(dto.getPayChannelId())){
            Result.failed("支付渠道ID不能为空");
        }
        return payChannelConfigService.deletePayChannel(dto);
    }

    @ApiOperation(value="停启支付渠道配置")
    @PutMapping(value = "/stopstart")
    public Result stopStartPayChannel(PayChannelConfigDto dto){
        if(Objects.isNull(dto.getPayChannelId())){
            Result.failed("支付渠道ID不能为空");
        }
        return payChannelConfigService.stopStartPayChannel(dto);
    }

}
