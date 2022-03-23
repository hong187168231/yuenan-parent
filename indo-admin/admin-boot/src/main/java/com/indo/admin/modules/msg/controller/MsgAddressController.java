package com.indo.admin.modules.msg.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.msg.service.IMsgAddressService;
import com.indo.admin.pojo.vo.msg.MsgAddressVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.msg.MsgAddressQueryReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息官方地址
 * @author justin
 */
@RestController
@RequestMapping("/api/v1/address")
public class MsgAddressController {

	@Autowired
	private IMsgAddressService msgAddressService;

	@ApiOperation(value = "分页查询官方地址")
	@GetMapping(value = "/list")
	public Result<List<MsgAddressVO>> list(MsgAddressQueryReq queryDto) {
		Page<MsgAddressVO> result = msgAddressService.queryList(queryDto);
		return Result.success(result.getRecords(), result.getTotal());
	}

}
