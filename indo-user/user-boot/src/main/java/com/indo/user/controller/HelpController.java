package com.indo.user.controller;

import com.indo.admin.api.HelpFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgTotalVO;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.UserTutorial;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "帮助接口")
@RestController
@RequestMapping("/api/v1/user/help")
public class HelpController {

	@Resource
	HelpFeignClient helpFeignClient;

	@GetMapping(value = "/user/tutorial")
	public Result<List<UserTutorial>> getUserTutorialList() {
		return helpFeignClient.getUserTutorialList();
	}

	@GetMapping(value = "/about/us")
	public Result<List<AboutUs>> getAboutUs() {
		return helpFeignClient.getAboutUsList();
	}
}
