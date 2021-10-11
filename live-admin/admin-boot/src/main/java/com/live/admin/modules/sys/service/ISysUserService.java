package com.live.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.admin.pojo.entity.SysUser;
import com.live.common.mybatis.base.PageResult;

public interface ISysUserService extends IService<SysUser> {

    PageResult list(Page<SysUser> page, SysUser sysUser);

    boolean saveUser(SysUser user);

    boolean updateUser(SysUser user);

    SysUser getByUsername(String username);
}
