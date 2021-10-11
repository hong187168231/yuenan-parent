package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayOnlineConfigService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayOnlineConfigDto;
import com.indo.pay.pojo.vo.PayOnlineConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 在线支付配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Api(tags = "在线支付配置")
@RestController
@RequestMapping("/api/v1/pay/online")
public class PayOnlineConfigController {

    @Resource
    private IPayOnlineConfigService payOnlineConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayOnlineConfigVO>> getPayBankList(PayOnlineConfigDto dto) {
        Page<PayOnlineConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayOnlineConfigVO> list = payOnlineConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getPayBank(PayOnlineConfigDto dto) {
        if(Objects.isNull(dto.getPayOnlineId())){
            Result.failed("在线支付ID不能为空");
        }
        return Result.success(payOnlineConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增支付方式配置")
    @PostMapping(value = "/insert")
    public Result insertPayOnline(PayOnlineConfigDto dto){

        return payOnlineConfigService.insertPayOnline(dto);
    }

    @ApiOperation(value="编辑支付方式配置")
    @PutMapping(value = "/update")
    public Result updatePayOnline(PayOnlineConfigDto dto){
        if(Objects.isNull(dto.getPayOnlineId())){
            Result.failed("在线支付ID不能为空");
        }
        return payOnlineConfigService.updatePayOnline(dto);
    }

    @ApiOperation(value="复制支付方式配置")
    @PatchMapping(value = "/copy")
    public Result copyPayOnline(PayOnlineConfigDto dto){
        if(Objects.isNull(dto.getPayOnlineId())){
            Result.failed("在线支付ID不能为空");
        }
        return payOnlineConfigService.copyPayOnline(dto);
    }

    @ApiOperation(value="删除支付方式配置")
    @DeleteMapping(value = "/delete")
    public Result deletePayOnline(PayOnlineConfigDto dto){
        if(Objects.isNull(dto.getPayOnlineId())){
            Result.failed("在线支付ID不能为空");
        }
        return payOnlineConfigService.deletePayOnline(dto);
    }

    @ApiOperation(value="停启支付方式配置")
    @PutMapping(value = "/stopstart")
    public Result stopStartPayOnline(PayOnlineConfigDto dto){
        if(Objects.isNull(dto.getPayOnlineId())){
            Result.failed("在线支付ID不能为空");
        }
        return payOnlineConfigService.stopStartPayOnline(dto);
    }

}
