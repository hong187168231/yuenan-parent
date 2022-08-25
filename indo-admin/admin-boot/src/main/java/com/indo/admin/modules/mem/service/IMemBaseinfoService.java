package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.MemBetInfoDTO;
import com.indo.admin.pojo.req.mem.MemAddReq;
import com.indo.admin.pojo.req.mem.MemBaseInfoReq;
import com.indo.admin.pojo.req.mem.MemEditStatusReq;
import com.indo.admin.pojo.req.mem.MemEditReq;
import com.indo.admin.pojo.vo.mem.MemBaseInfoVo;
import com.indo.admin.pojo.vo.mem.MemBaseDetailVO;
import com.indo.admin.pojo.vo.mem.MemBetInfoVo;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.entity.MemBaseinfo;

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
    Page<MemBaseInfoVo> queryList(MemBaseInfoReq memBaseInfoPageReq);

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
    MemBaseinfo getMemBaseInfo(Long uid);


    MemBaseDetailVO getMemBaseInfoByAccount(String account);


    /**
     * 根据会员UID 修改会员冻结状态
     *
     * @param frozenStatusReq
     * @return
     */
    boolean editStatus(MemEditStatusReq frozenStatusReq);


    boolean resetPassword(Long memId);


    void refreshMemBaseInfo(MemBaseInfoDTO memBaseInfoDTO, String account);

    /**
     * 查询重复IP用户信息
     * @param req
     * @return
     */
    Page findIpRepeatPage(MemBaseInfoReq req);

    /**
     * 邀请码
     * @param memBaseinfo
     */
    void saveMemInviteCode(MemBaseinfo memBaseinfo);

    /**
     * 会员打码量信息查询
     * @param memBetInfoDTO
     * @return
     */
    Page<MemBetInfoVo> findMemBetInfoPage(MemBetInfoDTO memBetInfoDTO);

}
