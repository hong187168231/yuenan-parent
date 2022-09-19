package com.indo.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.mapper.SysParameterMapper;
import com.indo.core.pojo.entity.SysParameter;
import com.indo.core.pojo.req.SysParameterQueryReq;
import com.indo.core.pojo.req.SysParameterReq;
import com.indo.core.service.ISysParameterService;
import com.indo.core.util.BusinessRedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    public SysParameter getByCode(SysParameterEnum sysParameterEnum,HttpServletRequest request) {
        return getByCode(null == sysParameterEnum ? null : sysParameterEnum.getCode(),request);
    }

    @Override
    public SysParameter getByCode(String paramCode, HttpServletRequest request) {
        SysParameter sysParameter = BusinessRedisUtils.getSysParameter(paramCode);
        if (sysParameter == null) {
            LambdaQueryWrapper<SysParameter> wrapper = new LambdaQueryWrapper<>();
            sysParameter = baseMapper.selectOne(wrapper.eq(SysParameter::getParamCode, paramCode));
            if (sysParameter == null || StringUtils.isEmpty(sysParameter.getParamValue())) {
                String countryCode = request.getHeader("countryCode");
                throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_NOT_EXIST.getCode(),countryCode));
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
    public void insertProgramSwitchTime(String minute, HttpServletRequest request) {
        SysParameter sysParameter =findProgramSwitchTime(request);
        if(sysParameter!=null){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_DUPLICATION.getCode(),countryCode));
        }
        SysParameterReq parameter = new SysParameterReq();
        parameter.setParamCode(SysParameterEnum.PROGRAM_SWITCH_TIME.getCode());
        parameter.setParamName(SysParameterEnum.PROGRAM_SWITCH_TIME.getRemark());
        parameter.setParamValue(minute);
        parameter.setStatus(0);
        saveSysParameter(parameter);
    }

    @Override
    public SysParameter findProgramSwitchTime(HttpServletRequest request) {
        return getByCode(SysParameterEnum.PROGRAM_SWITCH_TIME.getCode(),request);
    }

    @Override
    public void updateProgramSwitchTime(String minute, HttpServletRequest request) {
        SysParameter parameter =findProgramSwitchTime(request);
        if(parameter==null){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        parameter.setParamValue(minute);
        parameter.setUpdateUser(JwtUtils.getUsername());
        parameter.setUpdateTime(new Date());
       int row = baseMapper.updateById(parameter);
        if (row > 0) {
            BusinessRedisUtils.deleteSysParameter(SysParameterEnum.PROGRAM_SWITCH_TIME.getCode());
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
