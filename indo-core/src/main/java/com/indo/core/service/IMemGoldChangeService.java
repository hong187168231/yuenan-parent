package com.indo.core.service;

import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemGoldChange;

/**
 * <p>
 * 会员账变记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-12-07
 */
public interface IMemGoldChangeService extends SuperService<MemGoldChange> {


    /**
     * 修改用户账变信息
     *
     * @param change 账变信息
     * @return
     */
    boolean updateMemGoldChange(MemGoldChangeDTO change) throws RuntimeException;



}
