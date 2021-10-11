package com.indo.admin.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.user.pojo.dto.MsgPushRecordDTO;
import com.indo.user.pojo.entity.MsgPushRecord;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台推送记录表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Mapper
public interface MsgPushRecordMapper extends BaseMapper<MsgPushRecord> {
    /**
     * 分页查询
     * @param
     * @return
     */
    List<MsgPushRecordVO> queryList(@Param("page") Page<MsgPushRecordVO> page, @Param("dto") MsgPushRecordDTO dto);
}
