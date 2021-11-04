package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemRebate;
import com.indo.admin.modules.mem.mapper.MemRebateMapper;
import com.indo.admin.modules.mem.req.MemRebateAddReq;
import com.indo.admin.modules.mem.service.IMemRebateService;
import com.indo.admin.modules.mem.vo.MemRebateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    public List<MemRebateVo> queryAll() {
        QueryWrapper<MemRebate> wrapper = new QueryWrapper<>();
        List<MemRebate> memRebates = baseMapper.selectList(wrapper);
        ArrayList<MemRebateVo> vos = new ArrayList<>();
        for (MemRebate memRebate : memRebates) {
            MemRebateVo memRebateVo = new MemRebateVo();
            BeanUtils.copyProperties(memRebate, memRebateVo);
            vos.add(memRebateVo);
        }
        return vos;
    }

    @Override
    public void saveOne(MemRebateAddReq req) {
        MemRebate memRebate = new MemRebate();
        BeanUtils.copyProperties(req, memRebate);
        baseMapper.insert(memRebate);
        return;
    }

    @Override
    public void deleteOne(Integer id) {
        baseMapper.deleteById(id);
    }
}
