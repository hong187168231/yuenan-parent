package com.indo.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.SysRole;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    boolean delete(List<Long> ids);
}
