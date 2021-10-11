package com.indo.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.BusinessRedisUtils;
import com.indo.admin.modules.sys.mapper.SysParameterMapper;
import com.indo.admin.modules.sys.service.ISysParameterService;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import org.springframework.stereotype.Service;

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
    public void saveSysParameter(SysParameter parameter) {
        int row = baseMapper.insert(parameter);
        if (row > 0) {
            BusinessRedisUtils.addSysParameter(parameter);
        }
    }

    @Override
    public void deleteById(Long[] Ids) {
        for (Long id : Ids) {
            SysParameter config = selectById(id);
            baseMapper.deleteById(id);
            BusinessRedisUtils.deleteSysParameter(config.getParamCode());
        }
    }

    @Override
    public void updateSysParameter(SysParameter parameter) {
        SysParameter origin = selectById(parameter.getParamId());
        if (origin == null) {
            return;
        }
        //更新数据库
        baseMapper.updateById(parameter);
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
        SysParameter config = new SysParameter();
        return baseMapper.selectById(config);
    }

    @Override
    public List<SysParameter> selectAll() {
        return baseMapper.selectList(new LambdaQueryWrapper<SysParameter>().eq(SysParameter::getIsDel, false));
    }

    @Override
    public PageResult selectAll(String paramCode, Page<SysParameter> parameterPage) {
        LambdaQueryWrapper<SysParameter> wrapper = new LambdaQueryWrapper<>();
        Page<SysParameter> pageList = baseMapper.selectPage(parameterPage, wrapper.eq(SysParameter::getParamCode, paramCode));
        return PageResult.getPageResult(pageList);
    }


}
