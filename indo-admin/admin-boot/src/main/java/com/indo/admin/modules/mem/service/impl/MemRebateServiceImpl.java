package com.indo.admin.modules.mem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.BusinessRedisUtils;
import com.indo.admin.modules.mem.entity.MemRebate;
import com.indo.admin.modules.mem.mapper.MemRebateMapper;
import com.indo.admin.modules.mem.req.MemRebateAddReq;
import com.indo.admin.modules.mem.service.IMemRebateService;
import com.indo.admin.modules.mem.vo.MemBetVo;
import com.indo.admin.modules.mem.vo.MemRebateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 返点配置表 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
@Service
public class MemRebateServiceImpl extends ServiceImpl<MemRebateMapper, MemRebate> implements IMemRebateService {


    @Override
    public MemRebateVo queryMemRabate() {
        MemRebateVo memRebateVo = new MemRebateVo();
        MemRebate memRebate = baseMapper.selectById(1);
        List<MemBetVo> list = JSONArray.parseArray(memRebate.getRebateValue(), MemBetVo.class);
        memRebateVo.setBetList(list);
        return memRebateVo;
    }

    @Override
    @Transactional
    public boolean saveOne(MemRebateAddReq req) {
        MemRebate memRebate = new MemRebate();
        memRebate.setRebateValue(JSON.toJSONString(req.getBetList()));
        memRebate.setId(1);
        if (baseMapper.updateById(memRebate) > 0) {
            BusinessRedisUtils.addMemRebate(req);
        }
        return false;
    }

}
