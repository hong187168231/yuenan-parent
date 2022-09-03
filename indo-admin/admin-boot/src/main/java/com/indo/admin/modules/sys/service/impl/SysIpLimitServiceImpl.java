package com.indo.admin.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.sys.mapper.SysIpLimitMapper;
import com.indo.admin.modules.sys.service.ISysIpLimitService;
import com.indo.admin.pojo.dto.SysIpLimitDTO;
import com.indo.common.result.ResultCode;
import com.indo.core.pojo.entity.SysIpLimit;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 黑白名单IP限制表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-04-02
 */
@Service
public class SysIpLimitServiceImpl extends ServiceImpl<SysIpLimitMapper, SysIpLimit> implements ISysIpLimitService {
    /**
     * 根据类型查询对应IP列表
     *
     * @param types
     * @return
     */
    @Override
    public List<SysIpLimit> findSysIpLimitByType(Integer types) {
        if(types==null||types<=0){
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<SysIpLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysIpLimit::getTypes,types);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 查询黑白名单列表分页及筛选
     * @param sysIpLimit
     * @return
     */
    @Override
    public Page<SysIpLimit> findSysIpLimitPage(SysIpLimitDTO sysIpLimit) {
        if(sysIpLimit==null||sysIpLimit.getTypes()==null||sysIpLimit.getTypes()<=0){
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        Page<SysIpLimit> page = new Page<>(sysIpLimit.getPage(), sysIpLimit.getLimit());
        LambdaQueryWrapper<SysIpLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysIpLimit::getTypes,sysIpLimit.getTypes());
        if(StringUtils.isNotEmpty(sysIpLimit.getIp())){
            wrapper.eq(SysIpLimit::getIp,sysIpLimit.getIp());
        }
        return baseMapper.selectPage(page,wrapper);
    }

    /**
     * 新增黑白名单
     *
     * @param sysIpLimit
     */
    @Override
    public void insertSysIpLimit(SysIpLimit sysIpLimit) {
        if (sysIpLimit == null || StringUtils.isEmpty(sysIpLimit.getIp())) {
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        if(sysIpLimit.getTypes()==null||sysIpLimit.getTypes()<=0){
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        //sysIpLimit.setCreateUser(JwtUtils.getUsername());
        baseMapper.insert(sysIpLimit);
    }

    /**
     * 批量或单个删除黑白名单
     *
     * @param list
     */
    @Override
    public void deleteSysIpLimitByIdList(List<Integer> list) {
        if(list==null||list.size()<=0){
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        list.forEach(l->{
            if(l!=null&&l>0){
                baseMapper.deleteById(l);
            }
        });
    }
}
