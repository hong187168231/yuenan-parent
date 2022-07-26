package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.MsgStatusRecord;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 消息状态表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-07-20
 */
@Mapper
public interface MsgStatusRecordMapper extends BaseMapper<MsgStatusRecord> {
    /**
     * 查询用户有效系统消息
     * @return
     */
   Page<MsgPushRecordVO> findSysMsgInfo(@Param("page") Page page,@Param("memId") Long memId,@Param("deviceType") Integer deviceType);

    /**
     * 询用户有效系统消息数量
     * @param memId
     * @param deviceType
     * @return
     */
   Integer findSysMsgInfoCount(@Param("memId") Long memId,@Param("deviceType") Integer deviceType);
}
