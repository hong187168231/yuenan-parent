package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.vo.AgentVo;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.common.result.PageResult;
import com.indo.admin.modules.mem.req.SubordinateReq;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IMemAgentService extends IService<MemAgent> {

    Page<AgentVo> getPage(MemAgentPageReq req);

    Page<MemBaseInfoVo> subordinatePage(SubordinateReq req);

}
