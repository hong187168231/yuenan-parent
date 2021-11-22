package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.user.mapper.MemLevelMapper;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.service.IMemLevelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
public class MemLevelServiceImpl extends ServiceImpl<MemLevelMapper, MemLevel> implements IMemLevelService {

    @Override
    public List<MemLevel> findAllVips() {
        return baseMapper.selectList(new QueryWrapper<>());
    }
}
