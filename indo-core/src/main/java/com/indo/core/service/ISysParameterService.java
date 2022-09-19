package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.SysParameter;
import com.indo.core.pojo.req.SysParameterQueryReq;
import com.indo.core.pojo.req.SysParameterReq;

import javax.servlet.http.HttpServletRequest;
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
    SysParameter getByCode(SysParameterEnum sysParameterEnum,HttpServletRequest request);

    /**
     * 获取系统配置
     *
     * @param paramCode 系统配置码
     * @return
     * @throws BizException
     */
    SysParameter getByCode(String paramCode, HttpServletRequest request);

    /**
     * 新增系统参数
     *
     * @param parameter
     */
    void saveSysParameter(SysParameterReq parameter);

    /**
     * 查询参数配置信息
     *
     * @param paramId 参数配置ID
     * @return 参数配置信息
     */
    SysParameter selectById(Long paramId);


    /**
     * 删除系统参数
     *
     * @param ids
     */
    void deleteById(List<Long> ids);


    /**
     * 修改系统参数
     *
     * @param parameter
     */
    void updateSysParameter(SysParameterReq parameter);


    /**
     * 获取系统参数配置
     */
    List<SysParameter> selectAll();


    /**
     * 分页获取系统配置
     *
     * @param req     系统参数code值
     * @param parameterPage 分页实体类
     * @return
     */
    Page<SysParameter> selectAll(SysParameterQueryReq req, Page<SysParameter> parameterPage);


    /**
     * 刷新系统参数
     */
    void refreshSysParameterCache();

    /**
     * 新增程序切换时间(分钟)
     * @param minute
     */
    void insertProgramSwitchTime(String minute, HttpServletRequest request);

    /**
     * 查询程序切换时间(分钟)
     * @return
     */
    SysParameter findProgramSwitchTime(HttpServletRequest request);

    /**
     * 修改程序切换时间(分钟)
     */
    void updateProgramSwitchTime(String minute, HttpServletRequest request);
}
