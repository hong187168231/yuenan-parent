package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.pojo.criteria.MemLevelQueryCriteria;
import com.live.common.mybatis.base.service.SuperService;
import com.live.user.pojo.entity.MemLevel;
import com.live.user.pojo.vo.MemLevelVo;

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
    List<MemLevelVo> selectByPage(Page<MemLevelVo> page);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<YxSystemUserLevelDto>
     */
    List<MemLevel> queryAll(MemLevelQueryCriteria criteria);
}
