package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.req.MemAddReq;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.req.MemEditStatusReq;
import com.indo.admin.modules.mem.req.MemEditReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.mem.vo.MemBaseDetailVO;
import com.indo.core.base.service.SuperService;
import com.indo.user.pojo.entity.MemBaseinfo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员基础信息表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
public interface IMemBaseinfoService extends SuperService<MemBaseinfo> {
    /**
     * 分页查询
     *
     * @param memBaseInfoPageReq
     * @return
     */
    Page<MemBaseInfoVo> queryList(MemBaseInfoPageReq memBaseInfoPageReq);

    /**
     * 后台新增会员
     *
     * @param memAddReq
     * @return
     */
    void addMemBaseInfo(MemAddReq memAddReq);


    /**
     * 后台编辑会员
     *
     * @param memEditReq
     * @return
     */
    boolean editMemBaseInfo(MemEditReq memEditReq);

    boolean updateMemLevel(Long memId, Integer memLevel);

    /**
     * 根据会员UID 查会员编辑详情
     *
     * @param uid
     * @return
     */
    MemBaseDetailVO getMemBaseInfo(Long uid);


    MemBaseDetailVO getMemBaseInfoByAccount(String account);


    /**
     * 根据会员UID 修改会员冻结状态
     *
     * @param frozenStatusReq
     * @return
     */
    boolean editStatus(MemEditStatusReq frozenStatusReq);


    boolean resetPassword(Long memId);


    List<Long> findIdListByCreateTime(Date addDay);

}
