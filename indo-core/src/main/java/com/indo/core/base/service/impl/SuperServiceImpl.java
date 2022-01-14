package com.indo.core.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.indo.common.pojo.param.OrderQueryParam;
import com.indo.common.pojo.param.QueryParam;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.SuperService;
import com.indo.core.mapper.MemGoldChangeMapper;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.util.BusinessRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

/**
 * @author puff
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {
//
//    @Autowired
//    private MemTradingMapper memTradingMapper;

    protected Long getMemIdByAccount(String account) {
        return getMemCacheBaseInfo(account).getId();
    }

    protected MemBaseinfoBo getMemCacheBaseInfo(String account) {
        MemBaseinfoBo memBaseinfoBo = BusinessRedisUtils.getMemBaseInfoByAccount(account);
        if (memBaseinfoBo == null) {
            memBaseinfoBo = findMemBaseInfoByAccount(account);
            BusinessRedisUtils.saveMemBaseInfo(memBaseinfoBo);
        }
        return memBaseinfoBo;
    }

    private MemBaseinfoBo findMemBaseInfoByAccount(String account) {
        MemBaseinfoBo memBaseinfoBo =null; // memTradingMapper.findMemBaseInfoByAccount(account);
        if (null == memBaseinfoBo) {
            throw new BizException("用户不存在");
        }
        return memBaseinfoBo;
    }


}
