package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.PayWayConfigVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 支付方式配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-12-20
 */
@Api(tags = "支付方式")
@RestController
@RequestMapping("/pay/payWayConfig")
public class PayWayConfigController {

    @Autowired
    private IPayWayConfigService iPayWayConfigService;

    @ApiOperation(value = "支付方式列表")
    @GetMapping(value = "/list")
    public Result<List<PayWayConfigVO>> applyList(PayWayQueryDTO payWayQueryDTO) {
        Page<PayWayConfigVO> result = iPayWayConfigService.queryAll(payWayQueryDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "新增支付方式")
    @PostMapping(value = "/add")
    public Result add(@RequestBody PayWayDTO dto) {
        boolean flag = iPayWayConfigService.add(dto);
        return Result.judge(flag);
    }


    @ApiOperation(value = "编辑支付方式")
    @PostMapping(value = "/edit")
    public Result edit(@RequestBody PayWayDTO dto) {
        boolean flag = iPayWayConfigService.edit(dto);
        return Result.judge(flag);
    }
}
