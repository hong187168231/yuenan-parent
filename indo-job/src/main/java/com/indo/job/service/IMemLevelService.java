package com.indo.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.MemLevel;

import java.math.BigDecimal;

/**
 * <p>
 * 会员等级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
public interface IMemLevelService extends IService<MemLevel> {

    Integer getLevelByCondition(BigDecimal totalDeposit, BigDecimal totalBet);

}
