package com.indo.admin.security.service;

import com.indo.admin.api.UserFeignClient;
import com.indo.admin.common.enums.OAuthClientEnum;
import com.indo.admin.modules.sys.mapper.SysUserMapper;
import com.indo.admin.modules.sys.service.ISysUserService;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.admin.security.domain.OAuthUserDetails;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.web.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 【重要】从数据库获取用户信息，用于和前端传过来的用户信息进行密码判读
 *
 * @author puff
 * @date 2020-05-27
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = JwtUtils.getOAuthClientId();
        OAuthClientEnum client = OAuthClientEnum.getByClientId(clientId);
        OAuthUserDetails oauthUserDetails;
        switch (client) {
            default:
                SysUser sysUser = sysUserMapper.getByUsername(username);
                oauthUserDetails = new OAuthUserDetails(sysUser);
                break;
        }
        if (oauthUserDetails == null || oauthUserDetails.getId() == null) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        } else if (!oauthUserDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!oauthUserDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!oauthUserDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return oauthUserDetails;
    }

}
