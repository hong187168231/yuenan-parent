package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.PageResult;
import com.indo.user.pojo.entity.MemBank;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.BankCardPageReq;

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

    void addbankCard(AddBankCardReq req);

    PageResult<MemBankRelation> findPage(BankCardPageReq req);

    List<MemBank> findAllBank();


}
