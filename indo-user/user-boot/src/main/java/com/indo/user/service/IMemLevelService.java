package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.level.LevelUpRuleVO;
import com.indo.user.pojo.vo.level.MemLevelVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
public interface IMemLevelService extends IService<MemLevel> {

    MemLevelVo findInfo(LoginInfo loginInfo);

    List<LevelUpRuleVO> upRule(LoginInfo loginInfo);


}
