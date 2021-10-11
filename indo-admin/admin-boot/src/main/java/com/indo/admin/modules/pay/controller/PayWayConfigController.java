package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.vo.PayWayConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 支付方式配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Api(tags = "支付配置")
@RestController
@RequestMapping("/api/v1/pay/way")
public class PayWayConfigController {

    @Resource
    private IPayWayConfigService payWayConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayWayConfigVO>> getPayBankList(PayWayConfigDto dto) {
        Page<PayWayConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayWayConfigVO> list = payWayConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getPayBank(PayWayConfigDto dto) {
        if(Objects.isNull(dto.getPayWayId())){
            Result.failed("支付方式ID不能为空");
        }
        return Result.success(payWayConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增支付方式配置")
    @PostMapping(value = "/insert")
    public Result insertPayWay(PayWayConfigDto dto){

        return payWayConfigService.insertPayWay(dto);
    }

    @ApiOperation(value="编辑支付方式配置")
    @PutMapping(value = "/update")
    public Result updatePayWay(PayWayConfigDto dto){
        if(Objects.isNull(dto.getPayWayId())){
            Result.failed("支付方式ID不能为空");
        }
        return payWayConfigService.updatePayWay(dto);
    }

    @ApiOperation(value="删除支付方式配置")
    @DeleteMapping(value = "/delete")
    public Result deletePayWay(PayWayConfigDto dto){
        if(Objects.isNull(dto.getPayWayId())){
            Result.failed("支付方式ID不能为空");
        }
        return payWayConfigService.deletePayWay(dto);
    }

    @ApiOperation(value="停启支付方式配置")
    @PutMapping(value = "/stopstart")
    public Result stopStartPayWay(PayWayConfigDto dto){
        if(Objects.isNull(dto.getPayWayId())){
            Result.failed("支付方式ID不能为空");
        }
        return payWayConfigService.stopStartPayWay(dto);
    }

}
