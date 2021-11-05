package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.admin.modules.mem.req.MemAddReq;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.req.MemEditStatusReq;
import com.indo.admin.modules.mem.req.MemEditReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.mem.vo.MemBaseDetailVO;

/**
 * <p>
 * 会员基础信息表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
public interface IMemBaseinfoService extends IService<MemBaseinfo> {
    /**
     * 分页查询
     * @param memBaseInfoPageReq
     * @return
     */
    PageResult<MemBaseInfoVo> queryList(MemBaseInfoPageReq memBaseInfoPageReq);

    /**
     * 后台新增会员
     * @param memAddReq
     * @return
     */
    int addMemBaseInfo(MemAddReq memAddReq);


    /**
     * 后台编辑会员
     * @param memEditReq
     * @return
     */
    int editMemBaseInfo(MemEditReq memEditReq);

    /**
     * 根据会员UID 查会员编辑详情
     * @param uid
     * @return
     */
    MemBaseDetailVO getMemBaseInfo(Long uid);

    /**
     * 根据会员UID 修改会员冻结状态
     * @param frozenStatusReq
     * @return
     */
    int editStatus(MemEditStatusReq frozenStatusReq);
}
