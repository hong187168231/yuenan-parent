package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MsgPushRecordDTO;
import com.indo.user.pojo.entity.MsgPushRecord;
import com.indo.user.pojo.vo.MsgPushRecordVO;

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
    PageResult<MsgPushRecordVO> queryList(MsgPushRecordDTO pushRecordDTO);

    /**
     * 新增推送
     * @param pushRecord
     * @return
     */
    void add(MsgPushRecord pushRecord);

}
