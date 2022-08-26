package com.indo.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.SysIpLimitDTO;
import com.indo.core.pojo.entity.SysIpLimit;

import java.util.List;

/**
 * <p>
 * 黑白名单IP限制表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-04-02
 */
public interface ISysIpLimitService extends IService<SysIpLimit> {
    /**
     * 根据类型查询对应IP列表
     * @param types
     * @return
     */
    List<SysIpLimit> findSysIpLimitByType(Integer types);

    /**
     * 查询黑白名单列表分页及筛选
     * @param sysIpLimit
     * @return
     */
    Page<SysIpLimit> findSysIpLimitPage(SysIpLimitDTO sysIpLimit);

    /**
     * 新增黑白名单
     * @param sysIpLimit
     */
    void insertSysIpLimit(SysIpLimit sysIpLimit);

    /**
     * 批量或单个删除黑白名单
     */
    void deleteSysIpLimitByIdList(List<Integer> list);
}
