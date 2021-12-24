package com.indo.admin.modules.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.SysMenu;
import com.indo.admin.pojo.vo.MenuVO;
import com.indo.admin.pojo.vo.RouteVO;
import com.indo.admin.pojo.vo.TreeVO;

import java.util.List;
/**
 * @author puff
 * @date 2020-11-06
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<MenuVO> listMenuVO(LambdaQueryWrapper<SysMenu> baseQuery);

    List<TreeVO> listTreeVO(LambdaQueryWrapper<SysMenu> baseQuery);

    List<RouteVO> listRoute();
}
