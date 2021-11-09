package com.indo.admin.modules.stat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.stat.entity.StatUserRetention;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.common.mybatis.base.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
public interface IUserRetentionService extends IService<StatUserRetention> {

    PageResult<UserRetentionVo> queryList(UserRetentionPageReq req);
}
