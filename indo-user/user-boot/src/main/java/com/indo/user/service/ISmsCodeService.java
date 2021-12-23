package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.sms.SmsCodeVo;

import java.util.List;

/**
 * <p>
 * 会员等级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
public interface ISmsCodeService {

    List<SmsCodeVo> smsList();

}
