package com.indo.admin.api.fallback;

import com.indo.admin.api.HelpFeignClient;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.UserTutorial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class HelpFeignClientFallback implements HelpFeignClient {

	@Override
	public Result<List<AboutUs>> getAboutUsList() {
		log.error("feign getAboutUs 远程调用失败");
		return Result.failed(ResultCode.SYSTEM_EXECUTION_ERROR);
	}

	@Override
	public Result<List<UserTutorial>> getUserTutorialList() {
		log.error("feign getUserTutorial 远程调用失败");
		return Result.failed(ResultCode.SYSTEM_EXECUTION_ERROR);
	}
}
