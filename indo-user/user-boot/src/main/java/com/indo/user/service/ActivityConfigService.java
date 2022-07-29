package com.indo.user.service;

import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.ActivityConfig;

import java.util.List;

/**
 * 活动配置
 */
public interface ActivityConfigService extends SuperService<ActivityConfig> {
    /**
     * 查询所有活动配置
     * @return
     */
    List<ActivityConfig> findActivityConfList();
}
