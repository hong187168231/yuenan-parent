package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemGoldChange;

/**
 * <p>
 * 会员账变记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-12-07
 */
public interface IMemGoldChangeService extends IService<MemGoldChange> {

    /**
     * 修改用户账变信息
     *
     * @param change 账变信息
     * @return
     */
    boolean updateMemGoldChange(MemGoldChangeDTO change) throws RuntimeException;


}
