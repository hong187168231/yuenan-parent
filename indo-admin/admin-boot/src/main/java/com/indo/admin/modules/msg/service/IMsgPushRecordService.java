package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.vo.MsgPushRecordVO;
import com.indo.common.result.PageResult;
import com.indo.user.pojo.dto.MsgPushRecordDTO;
import com.indo.user.pojo.dto.PushRecordAddDTO;

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
     * @param pushRecordDTO
     * @return
     */
    Page<MsgPushRecordVO> queryList(MsgPushRecordDTO pushRecordDTO);

    /**
     * 新增推送
     * @param pushRecordAddDTO
     * @return
     */
    void add(PushRecordAddDTO pushRecordAddDTO);


    PageResult<MsgPushRecord> getSysMsg(MsgDTO msgDTO);
}
