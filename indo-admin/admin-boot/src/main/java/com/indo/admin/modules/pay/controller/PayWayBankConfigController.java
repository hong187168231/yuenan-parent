package com.indo.admin.modules.pay.controller;

import com.indo.admin.modules.pay.service.IPayWayBankConfigService;
import com.indo.admin.pojo.dto.PayWayBankQueryDTO;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.vo.pay.PayWayBankConfigVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 支付银行方式配置 前端控制器
 * </p>
 */
@Api(tags = "支付银行配置方式")
@RestController
@RequestMapping("/pay/payWayBankConfig")
public class PayWayBankConfigController {
	@Autowired
	private IPayWayBankConfigService payWayBankConfigService;

	@ApiOperation(value = "支付方式列表")
	@GetMapping(value = "/list")
	public Result<List<PayWayBankConfigVO>> list(PayWayBankQueryDTO payWayBankQueryDTO) {
		return payWayBankConfigService.queryAll(payWayBankQueryDTO);
	}

	@ApiOperation(value = "新增支付方式")
	@PostMapping(value = "/add")
	public Result add(@RequestBody PayWayDTO dto) {
		boolean flag = payWayBankConfigService.add(dto);
		return Result.judge(flag);
	}


}
