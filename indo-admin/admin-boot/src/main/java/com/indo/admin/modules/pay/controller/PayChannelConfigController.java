package com.indo.admin.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.admin.pojo.dto.PayChannelDTO;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.pay.PayChannelConfigVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-12-20
 */
@Api(tags = "支付渠道")
@RestController
@RequestMapping("/pay/channelConfig")
public class PayChannelConfigController {

    @Autowired
    private IPayChannelConfigService iPayChannelConfigService;


    @ApiOperation(value = "支付渠道列表")
    @GetMapping(value = "/list")
    public Result<List<PayChannelConfigVO>> applyList(PayChannelQueryDTO queryDTO) {
        Page<PayChannelConfigVO> result = iPayChannelConfigService.queryAll(queryDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "新增支付渠道")
    @PostMapping(value = "/add")
    public Result add(@RequestBody PayChannelDTO dto) {
        boolean flag = iPayChannelConfigService.add(dto);
        return Result.judge(flag);
    }


    @ApiOperation(value = "编辑支付渠道")
    @PostMapping(value = "/edit")
    public Result edit(@RequestBody PayChannelDTO dto) {
        boolean flag = iPayChannelConfigService.edit(dto);
        return Result.judge(flag);
    }


}
