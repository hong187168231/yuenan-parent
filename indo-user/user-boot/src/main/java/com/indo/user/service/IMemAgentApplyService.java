package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.user.pojo.entity.MemAgentApply;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
public interface IMemAgentApplyService extends IService<MemAgentApply> {

    void add(MemAgentApplyReq req, LoginInfo loginInfo);
}
