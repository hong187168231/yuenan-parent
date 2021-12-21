package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.pojo.entity.ActivityType;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.user.mapper.MemLevelMapper;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.MemLevelVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import com.indo.user.service.IMemLevelService;
import com.indo.user.service.MemBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemBaseInfoService memBaseInfoService;

    @Override
    public MemLevelVo findInfo(LoginInfo loginInfo) {
        MemLevelVo memLevelVo = new MemLevelVo();
        MemTradingVo memTradingVo = memBaseInfoService.tradingInfo(loginInfo.getId());
        memLevelVo.setTradingVo(memTradingVo);

        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper<>();
        List list = this.baseMapper.selectList(wrapper);
        memLevelVo.setLevelList(list);
        return memLevelVo;
    }
}
