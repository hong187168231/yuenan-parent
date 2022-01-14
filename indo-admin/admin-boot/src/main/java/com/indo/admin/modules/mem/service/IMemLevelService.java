package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.mem.MemLevelAddReq;
import com.indo.admin.pojo.req.mem.MemLevelPageReq;
import com.indo.admin.pojo.req.mem.MemLevelUpdateReq;
import com.indo.admin.pojo.vo.mem.MemLevelVo;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.MemLevel;

/**
 * <p>
 * 用户等级表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
public interface IMemLevelService extends SuperService<MemLevel> {

    /**
     * 查询数据分页
     *
     * @return PageResult
     */
    Page<MemLevelVo> selectByPage(MemLevelPageReq page);

    boolean saveOne(MemLevelAddReq req);

    boolean updateOne(MemLevelUpdateReq req);

    boolean delMemLevel(Long id);

}
