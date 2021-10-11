package com.indo.admin.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.vo.MsgStationLetterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 站内信 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Mapper
public interface MsgStationLetterMapper extends BaseMapper<MsgStationLetter> {
    /**
     * 分页查询
     * @param
     * @return
     */
    List<MsgStationLetterVO> queryList(@Param("page") Page<MsgStationLetterVO> page, @Param("dto") MsgStationLetterDTO dto);
}
