package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.admin.modules.pay.service.IPayWithdrawConfigService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.dto.PayWithdrawDTO;
import com.indo.admin.pojo.dto.PayWithdrawQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.admin.pojo.vo.pay.PayWithdrawConfigVO;
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
@Api(tags = "出款渠道")
@RestController
@RequestMapping("/pay/payWithdraw")
public class PayWithdrawController {

    @Autowired
    private IPayWithdrawConfigService iPayWithdrawConfigService;

    @ApiOperation(value = "出款渠道列表")
    @GetMapping(value = "/list")
    public Result<List<PayWithdrawConfigVO>> applyList(PayWithdrawQueryDTO queryDTO) {
        Page<PayWithdrawConfigVO> result = iPayWithdrawConfigService.queryAll(queryDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "编辑出款渠道")
    @PostMapping(value = "/edit")
    public Result edit(@RequestBody PayWithdrawDTO dto) {
        boolean flag = iPayWithdrawConfigService.edit(dto);
        return Result.judge(flag);
    }
}
