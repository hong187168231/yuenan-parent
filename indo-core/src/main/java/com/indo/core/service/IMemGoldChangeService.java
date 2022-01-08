package com.indo.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.pojo.dto.MemGoldChangeDto;
import com.indo.core.pojo.entity.MemGoldChange;

import java.math.BigDecimal;

/**
 * <p>
 * 会员账变记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-12-07
 */
public interface IMemGoldChangeService extends IService<MemGoldChange> {


    MemBaseinfoBo findMemBaseInfoById(Long memId);

    /**
     * 修改用户账变信息
     *
     * @param change 账变信息
     * @return
     */
    boolean updateMemGoldChange(MemGoldChangeDto change) throws RuntimeException;


    int updateMemberAmount(BigDecimal amount, BigDecimal canAmount, Long userId);


}
