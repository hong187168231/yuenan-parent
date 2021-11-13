package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemBankRelation;
import com.indo.admin.modules.mem.req.MemBankRelationPageReq;
import com.indo.admin.modules.mem.req.MemBankRelationSwitchStatusReq;
import com.indo.admin.modules.mem.vo.MemBankRelationVO;
import com.indo.common.result.PageResult;

/**
 * <p>
 * 用户绑定银行卡信息表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-06
 */
public interface IMemBankRelationService extends IService<MemBankRelation> {

    PageResult<MemBankRelationVO> queryList(MemBankRelationPageReq req);

    void switchStatus(MemBankRelationSwitchStatusReq req);

}
