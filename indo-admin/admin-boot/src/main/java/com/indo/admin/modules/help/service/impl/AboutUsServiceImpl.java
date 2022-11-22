package com.indo.admin.modules.help.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.ade.mapper.AdvertiseRecordMapper;
import com.indo.admin.modules.ade.service.IAdvertiseService;
import com.indo.admin.modules.help.mapper.AboutUsMapper;
import com.indo.admin.modules.help.service.IAboutUsService;
import com.indo.admin.modules.mem.mapper.MemBankMapper;
import com.indo.admin.modules.mem.service.IMemBankService;
import com.indo.core.pojo.entity.AboutUs;
import com.indo.core.pojo.entity.Advertise;
import com.indo.core.pojo.entity.MemBank;
import org.springframework.stereotype.Service;

/**
 * 关于我们
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@Service
public class AboutUsServiceImpl extends ServiceImpl<AboutUsMapper, AboutUs>
		implements IAboutUsService {

}
