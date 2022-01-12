package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.base.mapper.SuperMapper;
import com.indo.user.pojo.dto.WithdrawDto;
import com.indo.user.pojo.entity.Withdraw;
import com.indo.user.pojo.vo.WithdrawVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 充值Mapper接口
 *
 */
@Mapper
@Repository
public interface WithdrawMapper extends SuperMapper<Withdraw> {

    List<WithdrawVo> queryList(@Param("page") Page<WithdrawVo> page, @Param("dto") WithdrawDto dto);

    /**
     * 提现申请查询
     * @param page
     * @param dto
     * @return
     */
    List<WithdrawVo> queryApplyList(@Param("page") Page<WithdrawVo> page, @Param("dto") WithdrawDto dto);
}
