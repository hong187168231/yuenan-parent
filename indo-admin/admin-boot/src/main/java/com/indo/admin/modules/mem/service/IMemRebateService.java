package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemRebate;
import com.indo.admin.modules.mem.req.MemRebateAddReq;
import com.indo.admin.modules.mem.vo.MemRebateVo;

import java.util.List;

/**
 * <p>
 * 返点配置表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
public interface IMemRebateService extends IService<MemRebate> {

    MemRebateVo queryMemRabate();

    boolean saveOne(MemRebateAddReq req);

}
