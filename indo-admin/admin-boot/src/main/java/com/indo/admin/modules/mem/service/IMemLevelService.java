package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.req.MemLevelAddReq;
import com.indo.admin.modules.mem.req.MemLevelPageReq;
import com.indo.admin.modules.mem.req.MemLevelUpdateReq;
import com.indo.admin.modules.mem.vo.MemLevelVo;
import com.indo.common.result.PageResult;
import com.indo.core.base.service.SuperService;
import com.indo.user.pojo.entity.MemLevel;

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
