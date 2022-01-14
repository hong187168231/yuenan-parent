package com.indo.admin.modules.mem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.mem.mapper.MemRebateMapper;
import com.indo.admin.pojo.req.mem.MemRebateAddReq;
import com.indo.admin.modules.mem.service.IMemRebateService;
import com.indo.admin.pojo.vo.mem.MemBetVo;
import com.indo.admin.pojo.vo.mem.MemRebateVo;
import com.indo.core.pojo.entity.AgentRebateConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MemRebateServiceImpl extends ServiceImpl<MemRebateMapper, AgentRebateConfig> implements IMemRebateService {


    @Override
    public MemRebateVo queryMemRabate() {
        MemRebateVo memRebateVo = new MemRebateVo();
        AgentRebateConfig memRebate = baseMapper.selectById(1);
        List<MemBetVo> list = JSONArray.parseArray(memRebate.getRebateValue(), MemBetVo.class);
        memRebateVo.setBetList(list);
        return memRebateVo;
    }

    @Override
    @Transactional
    public boolean saveOne(MemRebateAddReq req) {
        AgentRebateConfig memRebate = new AgentRebateConfig();
        memRebate.setRebateValue(JSON.toJSONString(req.getBetList()));
        memRebate.setId(1);
        if (baseMapper.updateById(memRebate) > 0) {
            AdminBusinessRedisUtils.addMemRebate(req);
        }
        return false;
    }

}
