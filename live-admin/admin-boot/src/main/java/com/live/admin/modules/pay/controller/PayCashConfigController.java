package com.live.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.service.IPayCashConfigService;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.entity.PayCashConfig;
import com.live.pay.pojo.vo.PayCashConfigVO;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 提款配置 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-08
 */
@Api(tags = "提款配置")
@RestController
@RequestMapping("/api/v1/pay/cash")
public class PayCashConfigController {

    @Resource
    private IPayCashConfigService payCashConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<PayCashConfigVO>> getCashList(PayCashConfigDto dto) {
        Page<PayCashConfigVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<PayCashConfigVO> list = payCashConfigService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "查询单个")
    @GetMapping(value = "/single")
    public Result getCash(PayCashConfigDto dto) {
        if(Objects.isNull(dto.getPayCashId())){
            Result.failed("出款ID不能为空");
        }
        return Result.success(payCashConfigService.querySingle(dto));
    }

    @ApiOperation(value="新增出款配置")
    @PostMapping(value = "/insert")
    public Result insertCash(PayCashConfigDto dto){

        return payCashConfigService.insert(dto);
    }

    @ApiOperation(value="编辑出款配置")
    @PutMapping(value = "/update")
    public Result updateCash(PayCashConfigDto dto){
        if(Objects.isNull(dto.getPayCashId())){
            Result.failed("出款ID不能为空");
        }
        return payCashConfigService.updateCash(dto);
    }

    @ApiOperation(value="删除出款配置")
    @DeleteMapping(value = "/delete")
    public Result deleteCash(PayCashConfigDto dto){
        if(Objects.isNull(dto.getPayCashId())){
            Result.failed("出款ID不能为空");
        }
        return payCashConfigService.deleteCash(dto);
    }

}
