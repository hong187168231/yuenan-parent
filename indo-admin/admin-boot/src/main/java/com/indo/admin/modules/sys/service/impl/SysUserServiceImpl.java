package com.indo.admin.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.sys.mapper.SysUserMapper;
import com.indo.admin.pojo.dto.ChangePasswordDto;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.admin.pojo.entity.SysUserRole;
import com.indo.admin.modules.sys.service.ISysUserRoleService;
import com.indo.admin.modules.sys.service.ISysUserService;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.result.ResultCode;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    private final PasswordEncoder passwordEncoder;

    private final ISysUserRoleService iSysUserRoleService;


    @Override
    public Page<SysUser> list(Page<SysUser> page, SysUser user) {
        List<SysUser> list = this.baseMapper.list(page, user);
        page.setRecords(list);
        return page;
    }


    @Override
    public boolean saveUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(GlobalConstants.DEFAULT_USER_PASSWORD));
        boolean result = this.save(user);
        if (result) {
            List<Long> roleIds = user.getRoleIds();
            if (CollectionUtil.isNotEmpty(roleIds)) {
                List<SysUserRole> userRoleList = new ArrayList<>();
                roleIds.forEach(roleId -> userRoleList.add(new SysUserRole().setUserId(user.getId()).setRoleId(roleId)));
                result = iSysUserRoleService.saveBatch(userRoleList);
            }
        }
        return result;
    }

    @Override
    public boolean updateUser(SysUser user) {

        List<Long> dbRoleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId())).stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        List<Long> roleIds = user.getRoleIds();

        List<Long> addRoleIds = roleIds.stream().filter(roleId -> !dbRoleIds.contains(roleId)).collect(Collectors.toList());
        List<Long> removeRoleIds = dbRoleIds.stream().filter(roleId -> !roleIds.contains(roleId)).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(addRoleIds)) {
            List<SysUserRole> addUserRoleList = new ArrayList<>();
            addRoleIds.forEach(roleId -> {
                addUserRoleList.add(new SysUserRole().setUserId(user.getId()).setRoleId(roleId));
            });
            iSysUserRoleService.saveBatch(addUserRoleList);
        }

        if (CollectionUtil.isNotEmpty(removeRoleIds)) {
            removeRoleIds.forEach(roleId -> {
                iSysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()).eq(SysUserRole::getRoleId, roleId));
            });
        }

        boolean result = this.updateById(user);
        return result;
    }

    @Override
    public SysUser getByUsername(String username) {
        return this.baseMapper.getByUsername(username);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, JwtUtils.getUsername());
//        wrapper.eq(SysUser::getPassword,encode);
        SysUser sysUser = baseMapper.selectOne(wrapper);

        Boolean isEqual = passwordEncoder.matches(changePasswordDto.getUsedPassword(),sysUser.getPassword());

        if(!isEqual){
            throw new BizException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        String encodeNew = passwordEncoder.encode(changePasswordDto.getNewPassword());
        sysUser.setPassword(encodeNew);
        baseMapper.updateById(sysUser);
    }


}
