package com.indo.admin.modules.mem.service;

import com.indo.admin.modules.mem.entity.MemLevel;
import com.indo.admin.modules.mem.req.MemLevelAddReq;
import com.indo.admin.modules.mem.req.MemLevelPageReq;
import com.indo.admin.modules.mem.req.MemLevelUpdateReq;
import com.indo.admin.modules.mem.vo.MemLevelVo;
import com.indo.admin.pojo.criteria.MemLevelQueryCriteria;
import com.indo.common.mybatis.base.service.SuperService;
import com.indo.common.result.PageResult;

import java.util.List;

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
    PageResult<MemLevelVo> selectByPage(MemLevelPageReq page);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<YxSystemUserLevelDto>
     */
    List<MemLevel> queryAll(MemLevelQueryCriteria criteria);

    void saveOne(MemLevelAddReq req);

    void updateOne(MemLevelUpdateReq req);

}
