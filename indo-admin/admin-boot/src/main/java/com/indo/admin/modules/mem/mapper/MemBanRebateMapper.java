package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.mybatis.base.mapper.SuperMapper;
import com.indo.user.pojo.dto.MemBanRebateDto;
import com.indo.user.pojo.entity.MemBanRebate;
import com.indo.user.pojo.vo.MemBanRebateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 * 用户等级表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Mapper
@Repository
public interface MemBanRebateMapper extends SuperMapper<MemBanRebate> {

    List<MemBanRebateVo> selectMemBanRebate(@Param("page") Page<MemBanRebateVo> page, @Param("dto") MemBanRebateDto dto);

}
