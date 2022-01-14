package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.MemRebateAddReq;
import com.indo.admin.pojo.vo.MemRebateVo;
import com.indo.core.pojo.entity.AgentRebateConfig;

/**
 * <p>
 * 返点配置表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
public interface IMemRebateService extends IService<AgentRebateConfig> {

    MemRebateVo queryMemRabate();

    boolean saveOne(MemRebateAddReq req);

}
