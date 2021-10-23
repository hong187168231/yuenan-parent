package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.indo.user.pojo.req.MemBaseInfoPageReq;
import com.indo.user.pojo.vo.MemBaseInfoVo;
import com.indo.user.pojo.vo.MsgPlatformAnnouncementVO;

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
}
