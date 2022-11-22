package com.indo.admin.modules.help.controller;

import com.indo.admin.modules.help.service.IAboutUsService;
import com.indo.admin.modules.help.service.IUserTutorialService;
import com.indo.admin.pojo.req.help.AboutUsReq;
import com.indo.admin.pojo.req.help.UserTutorialReq;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.UserTutorial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 帮助
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/help")
@Slf4j
@Api(tags = "帮助")
public class HelpController {

	@Autowired
	private IUserTutorialService userTutorialService;

	@Autowired
	private IAboutUsService aboutUsService;

	@ApiOperation(value = "查询用戶教程", httpMethod = "GET")
	@GetMapping(value = "/user/tutorial")
	public Result<List<UserTutorial>> getUserTutorial() {
		return Result.success(userTutorialService.list());
	}

	@ApiOperation(value = "查询关于我们", httpMethod = "GET")
	@GetMapping(value = "/about/us")
	public Result<List<AboutUs>> getAboutUs() {
		return Result.success(aboutUsService.list());
	}

	@ApiOperation(value = "新增用户教程", httpMethod = "POST")
	@PostMapping(value = "/addUserTutorial")
	public Result addUserTutorial(UserTutorialReq userTutorialReq) {
		UserTutorial userTutorial = new UserTutorial();
		BeanUtils.copyProperties(userTutorialReq, userTutorial);
		userTutorial.setCreateTime(new Date());
		return Result.judge(userTutorialService.save(userTutorial));
	}

	@ApiOperation(value = "新增关于我们", httpMethod = "POST")
	@PostMapping(value = "/addAboutUs")
	public Result addAboutUs(AboutUsReq aboutUsReq) {
		AboutUs aboutUs = new AboutUs();
		BeanUtils.copyProperties(aboutUsReq, aboutUs);
		aboutUs.setCreateTime(new Date());
		return Result.judge(aboutUsService.save(aboutUs));
	}
}
