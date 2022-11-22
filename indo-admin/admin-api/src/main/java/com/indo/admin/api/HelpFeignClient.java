package com.indo.admin.api;

import com.indo.admin.api.fallback.HelpFeignClientFallback;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.UserTutorial;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 帮助
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = HelpFeignClientFallback.class)
public interface HelpFeignClient {

	@GetMapping("/api/v1/help/user/tutorial")
	Result<List<AboutUs>> getAboutUsList();

	@GetMapping("/api/v1/help/user/tutorial")
	Result<List<UserTutorial>> getUserTutorialList();
}
