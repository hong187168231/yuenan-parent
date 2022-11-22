package com.indo.admin.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.constant.SystemConstants;
import com.indo.admin.modules.sys.mapper.SysMenuMapper;
import com.indo.admin.modules.sys.service.ISysMenuService;
import com.indo.admin.pojo.entity.SysMenu;
import com.indo.admin.pojo.vo.MenuVO;
import com.indo.admin.pojo.vo.RouteVO;
import com.indo.admin.pojo.vo.TreeVO;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author xianrui
 * @date 2020-11-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<MenuVO> listMenuVO(LambdaQueryWrapper<SysMenu> baseQuery, HttpServletRequest request) {
        String countryCode = request.getHeader("countryCode");
        List<SysMenu> menuList = this.baseMapper.selectList(baseQuery);
        List<MenuVO> list = recursionForTree(SystemConstants.ROOT_MENU_ID, menuList);
        if(StringUtils.isNotEmpty(countryCode)&&!countryCode.equals("CN")){
            list.forEach(l->{
                l.setName(MessageUtils.get(l.getId().toString(),countryCode));
                l.getChildren().forEach(ll->{
                    ll.setName(MessageUtils.get(ll.getId().toString(),countryCode));
                });
            });
        }
        return list;
    }

    @Override
    public List<TreeVO> listTreeVO(LambdaQueryWrapper<SysMenu> baseQuery) {
        List<SysMenu> menuList = this.list(baseQuery);
        List<TreeVO> list = recursionForTreeSelect(SystemConstants.ROOT_MENU_ID, menuList);
        return list;
    }


    /**
     * 递归生成表格数据
     *
     * @param parentId
     * @param menuList
     * @return
     */
    public static List<MenuVO> recursionForTree(Long parentId, List<SysMenu> menuList) {
        List<MenuVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    MenuVO menuVO = new MenuVO();
                    BeanUtil.copyProperties(menu, menuVO);
                    List<MenuVO> children = recursionForTree(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        menuVO.setChildren(children);
                    }
                    list.add(menuVO);
                });
        return list;
    }


    /**
     * 递归生成部门树形下拉数据
     *
     * @param parentId
     * @param menuList
     * @return
     */
    public static List<TreeVO> recursionForTreeSelect(Long parentId, List<SysMenu> menuList) {
        List<TreeVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    TreeVO treeVO = new TreeVO();
                    treeVO.setId(menu.getId());
                    treeVO.setLabel(menu.getName());
                    List<TreeVO> children = recursionForTreeSelect(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        treeVO.setChildren(children);
                    }
                    list.add(treeVO);
                });
        return list;
    }


    @Override
    public List<RouteVO> listRoute() {
        List<SysMenu> menuList = this.baseMapper.listRoute();
        List<RouteVO> list = recursionRoute(SystemConstants.ROOT_MENU_ID, menuList);
        return list;
    }

    // 递归生成路由
    private List<RouteVO> recursionRoute(Long parentId, List<SysMenu> menuList) {
        List<RouteVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouteVO routeVO = new RouteVO();

                    routeVO.setName(menu.getId() + ""); // 根据name路由跳转 this.$router.push({path:xxx})
                    routeVO.setPath(menu.getPath()); // 根据path路由跳转 this.$router.push({name:xxx})
                    routeVO.setRedirect(menu.getRedirect());
                    routeVO.setComponent(menu.getComponent());
                    routeVO.setRedirect(menu.getRedirect());
                    routeVO.setMeta(routeVO.new Meta(
                            menu.getName(),
                            menu.getIcon(),
                            menu.getRoles()
                    ));
                    // 菜单显示隐藏
                    routeVO.setHidden(!GlobalConstants.STATUS_YES.equals(menu.getVisible()));
                    List<RouteVO> children = recursionRoute(menu.getId(), menuList);
                    routeVO.setChildren(children);
                    if (CollectionUtil.isNotEmpty(children)) {
                        routeVO.setAlwaysShow(Boolean.TRUE); // 显示子节点
                    }
                    list.add(routeVO);
                }));
        return list;

    }
}
