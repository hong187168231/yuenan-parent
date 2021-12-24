package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.mybatis.base.mapper.SuperMapper;
import com.indo.user.pojo.dto.MemSubordinateDto;
import com.indo.user.pojo.entity.MemSubordinate;
import com.indo.user.pojo.vo.MemSubordinateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 * 用户邀请表 Mapper 接口
 * </p>
 *
 * @author sss
 * @since 2021-08-26
 */
@Mapper
@Repository
public interface MemSubordinateMapper extends SuperMapper<MemSubordinate> {

    List<MemSubordinateVo> selectMemSubordinate(@Param("page") Page<MemSubordinateVo> page, @Param("dto") MemSubordinateDto memSubordinateDTO);

}
