package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayGroupConfigService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayGroupConfigDto;
import com.indo.pay.pojo.vo.PayGroupConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 支付层级配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Api(tags = "支付层级配置")
@RestController
@RequestMapping("/api/v1/pay/group")
public class PayGroupConfigController {

    @Resource
    private IPayGroupConfigService payGroupConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayGroupConfigVO>> getPayBankList(PayGroupConfigDto dto) {
        Page<PayGroupConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayGroupConfigVO> list = payGroupConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getPayBank(PayGroupConfigDto dto) {
        if(Objects.isNull(dto.getPayGroupId())){
            Result.failed("支付层级ID不能为空");
        }
        return Result.success(payGroupConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增支付层级配置")
    @PostMapping(value = "/insert")
    public Result insertPayGroup(PayGroupConfigDto dto){

        return payGroupConfigService.insertPayGroup(dto);
    }

    @ApiOperation(value="编辑支付层级配置")
    @PutMapping(value = "/update")
    public Result updatePayGroup(PayGroupConfigDto dto){
        if(Objects.isNull(dto.getPayGroupId())){
            Result.failed("支付层级ID不能为空");
        }
        return payGroupConfigService.updatePayGroup(dto);
    }

    @ApiOperation(value="删除支付层级配置")
    @DeleteMapping(value = "/delete")
    public Result deletePayGroup(PayGroupConfigDto dto){
        if(Objects.isNull(dto.getPayGroupId())){
            Result.failed("支付层级ID不能为空");
        }
        return payGroupConfigService.deletePayGroup(dto);
    }

    @ApiOperation(value="停启支付层级配置")
    @PutMapping(value = "/stopstart")
    public Result stopStartPayGroup(PayGroupConfigDto dto){
        if(Objects.isNull(dto.getPayGroupId())){
            Result.failed("支付层级ID不能为空");
        }
        return payGroupConfigService.stopStartPayGroup(dto);
    }

}
