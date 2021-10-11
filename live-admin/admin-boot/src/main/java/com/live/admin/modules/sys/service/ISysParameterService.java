package com.live.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.admin.pojo.entity.SysParameter;
import com.live.common.enums.SysParameterEnum;
import com.live.common.mybatis.base.PageResult;
import com.live.common.web.exception.BizException;

import java.util.List;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
public interface ISysParameterService extends IService<SysParameter> {


    /**
     * 获取系统配置
     *
     * @param sysParameterEnum 系统配置码枚举
     * @return
     * @throws BizException
     */
    SysParameter getByCode(SysParameterEnum sysParameterEnum);

    /**
     * 获取系统配置
     *
     * @param paramCode 系统配置码
     * @return
     * @throws BizException
     */
    SysParameter getByCode(String paramCode);

    /**
     * 新增系统参数
     *
     * @param parameter
     */
    void saveSysParameter(SysParameter parameter);

    /**
     * 查询参数配置信息
     *
     * @param parameterId 参数配置ID
     * @return 参数配置信息
     */
    SysParameter selectById(Long parameterId);


    /**
     * 删除系统参数
     *
     * @param Ids
     */
    void deleteById(Long[] Ids);


    /**
     * 修改系统参数
     *
     * @param parameter
     */
    void updateSysParameter(SysParameter parameter);


    /**
     * 获取系统参数配置
     */
    List<SysParameter> selectAll();


    /**
     * 分页获取系统配置
     *
     * @param paramCode     系统参数code值
     * @param parameterPage 分页实体类
     * @return
     */
    PageResult selectAll(String paramCode, Page<SysParameter> parameterPage);


    /**
     * 刷新系统参数
     */
    void refreshSysParameterCache();


}
