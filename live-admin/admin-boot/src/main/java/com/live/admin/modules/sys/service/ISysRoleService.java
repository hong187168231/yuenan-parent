package com.live.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.live.admin.pojo.entity.SysRole;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    boolean delete(List<Long> ids);
}
