package com.indo.admin.modules.activity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.ActivityConfig;

/**
 * <p>
 * 活动配置表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-07-13
 */
public interface IActivityConfigService extends IService<ActivityConfig> {
    /**
     * 根据类型查询活动配置
     * @param types
     * @return
     */
    ActivityConfig findActivityConfigByType(Integer types);
}
