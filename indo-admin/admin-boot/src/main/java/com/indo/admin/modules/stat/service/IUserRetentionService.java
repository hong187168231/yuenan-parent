package com.indo.admin.modules.stat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.admin.pojo.entity.StatUserRetention;
import com.indo.common.result.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
public interface IUserRetentionService extends IService<StatUserRetention> {

    Page<UserRetentionVo> queryList(UserRetentionPageReq req);
}
