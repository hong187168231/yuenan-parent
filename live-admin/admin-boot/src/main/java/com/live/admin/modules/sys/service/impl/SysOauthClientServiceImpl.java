package com.live.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.live.admin.modules.sys.mapper.SysOauthClientMapper;
import com.live.admin.modules.sys.service.ISysOauthClientService;
import com.live.admin.pojo.entity.SysOauthClient;
import org.springframework.stereotype.Service;

/**
 * @author puff
 * @date 2020-11-06
 */
@Service
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements ISysOauthClientService {
}
