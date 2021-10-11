package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.common.mybatis.base.mapper.SuperMapper;
import com.live.user.pojo.dto.WithdrawDto;
import com.live.user.pojo.entity.Withdraw;
import com.live.user.pojo.vo.WithdrawVo;
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
