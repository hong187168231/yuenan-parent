package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgPushRecordVO;
import com.indo.core.pojo.entity.MsgPushRecord;
import com.indo.user.pojo.req.msg.PushRecordAddReq;
import com.indo.user.pojo.req.msg.PushRecordQueryReq;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 后台推送记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
public interface IMsgPushRecordService extends IService<MsgPushRecord> {
    /**
     * 分页查询
     * @param queryDTO
     * @return
     */
    Page<MsgPushRecordVO> queryList(PushRecordQueryReq queryDTO);

    /**
     * 新增推送
     * @param pushRecordAddDTO
     * @return
     */
    void add(PushRecordAddReq pushRecordAddDTO);


    List<MsgPushRecordVO> getSysMsg(MsgDTO msgDTO);


    int sysMsgTotal(MsgDTO msgDTO);

    void deleteMsg(MsgDTO msgDTO, HttpServletRequest request);
}
