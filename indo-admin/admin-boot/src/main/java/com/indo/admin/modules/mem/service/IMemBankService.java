package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.MemBankRelationPageReq;
import com.indo.admin.pojo.req.MemBankRelationSwitchStatusReq;
import com.indo.admin.pojo.vo.MemBankVO;
import com.indo.core.pojo.entity.MemBank;

/**
 * <p>
 * 用户绑定银行卡信息表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-06
 */
public interface IMemBankService extends IService<MemBank> {

    Page<MemBankVO> queryList(MemBankRelationPageReq req);

    void switchStatus(MemBankRelationSwitchStatusReq req);

}
