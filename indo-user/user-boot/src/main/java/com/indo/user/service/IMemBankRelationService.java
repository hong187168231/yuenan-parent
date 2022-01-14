package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.vo.MemBankVo;

import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-17
 */
public interface IMemBankRelationService extends IService<MemBankRelation> {

    boolean addBankCard(AddBankCardReq req,LoginInfo loginUser);

    List<MemBankVo> findPage(LoginInfo loginUser);


}
