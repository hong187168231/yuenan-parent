package com.indo.core.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.SuperService;
import com.indo.core.mapper.MemGoldChangeMapper;
import com.indo.core.mapper.MemTradingMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.util.BusinessRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author puff
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {

    protected Long getMemIdByAccount(String account) {
        return getMemCacheBaseInfo(account).getId();
    }

    protected MemBaseInfoBO getMemCacheBaseInfo(String account) {
        MemBaseInfoBO memBaseinfoBo = BusinessRedisUtils.getMemBaseInfoByAccount(account);
        return memBaseinfoBo;
    }




}
