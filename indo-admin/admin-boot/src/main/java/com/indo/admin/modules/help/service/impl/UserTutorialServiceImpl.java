package com.indo.admin.modules.help.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.help.mapper.AboutUsMapper;
import com.indo.admin.modules.help.mapper.UserTutorialMapper;
import com.indo.admin.modules.help.service.IAboutUsService;
import com.indo.admin.modules.help.service.IUserTutorialService;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.UserTutorial;
import org.springframework.stereotype.Service;

/**
 * 用户教程
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@Service
public class UserTutorialServiceImpl extends ServiceImpl<UserTutorialMapper, UserTutorial>
		implements IUserTutorialService {
}
