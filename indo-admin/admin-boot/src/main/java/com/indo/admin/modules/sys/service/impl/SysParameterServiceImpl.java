package com.indo.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.BusinessRedisUtils;
import com.indo.admin.modules.sys.mapper.SysParameterMapper;
import com.indo.admin.modules.sys.service.ISysParameterService;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.admin.pojo.req.SysParameterQueryReq;
import com.indo.admin.pojo.req.SysParameterReq;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Service
public class SysParameterServiceImpl extends ServiceImpl<SysParameterMapper, SysParameter> implements ISysParameterService {


    @Override
    public SysParameter getByCode(SysParameterEnum sysParameterEnum) {
        return getByCode(null == sysParameterEnum ? null : sysParameterEnum.getCode());
    }

    @Override
    public SysParameter getByCode(String paramCode) {
        SysParameter sysParameter = BusinessRedisUtils.getSysParameter(paramCode);
        if (sysParameter == null) {
            LambdaQueryWrapper<SysParameter> wrapper = new LambdaQueryWrapper<>();
            sysParameter = baseMapper.selectOne(wrapper.eq(SysParameter::getParamCode, paramCode));
            if (sysParameter == null || StringUtils.isEmpty(sysParameter.getParamValue())) {
                throw new BizException(ResultCode.SYSPARAMETER_NOT_EXIST);
            }
            BusinessRedisUtils.addSysParameter(sysParameter);
        }
        return sysParameter;
    }


    public void refreshSysParameterCache() {
        List<SysParameter> parameterList = selectAll();
        for (SysParameter sysParameter : parameterList) {
            BusinessRedisUtils.addSysParameter(sysParameter);
        }
    }

    @Override
    public void saveSysParameter(SysParameterReq parameter) {
        SysParameter sysParameter = new SysParameter();
        BeanUtils.copyProperties(parameter, sysParameter);
        sysParameter.setCreateUser(JwtUtils.getUsername());
        int row = baseMapper.insert(sysParameter);
        if (row > 0) {
            BusinessRedisUtils.addSysParameter(sysParameter);
        }
    }

    @Override
    public void deleteById(List<Long> ids) {
        for (Long id : ids) {
            SysParameter config = selectById(id);
            if (baseMapper.deleteById(id) > 0) {
                BusinessRedisUtils.deleteSysParameter(config.getParamCode());
            }
        }
    }

    @Override
    public void updateSysParameter(SysParameterReq parameter) {
        SysParameter sysParameter = new SysParameter();
        BeanUtils.copyProperties(parameter, sysParameter);
        sysParameter.setUpdateUser(JwtUtils.getUsername());
        SysParameter origin = selectById(sysParameter.getParamId());
        if (origin == null) {
            return;
        }
        //更新数据库
        baseMapper.updateById(sysParameter);
        //删除以前的缓存
        BusinessRedisUtils.deleteSysParameter(origin.getParamCode());
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysParameter selectById(Long configId) {
        return baseMapper.selectById(configId);
    }

    @Override
    public List<SysParameter> selectAll() {
        return baseMapper.selectList(new LambdaQueryWrapper<SysParameter>().eq(SysParameter::getStatus, 1));
    }

    @Override
    public Page<SysParameter> selectAll(SysParameterQueryReq req, Page<SysParameter> parameterPage) {
        LambdaQueryWrapper<SysParameter> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(req.getParamCode())) {
            wrapper.like(SysParameter::getParamCode, req.getParamCode());
        }
        if (StringUtils.isNotBlank(req.getParamName())) {
            wrapper.like(SysParameter::getParamName, req.getParamName());
        }
        Page<SysParameter> pageList = baseMapper.selectPage(parameterPage, wrapper);
        return pageList;
    }


    public static void main(String[] args) {



        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Chongqing"));
        System.out.println(localDateTime);


    }

}
