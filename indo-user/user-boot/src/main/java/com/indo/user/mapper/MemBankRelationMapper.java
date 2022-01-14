package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.BankCardPageReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-17
 */
@Mapper
public interface MemBankRelationMapper extends BaseMapper<MemBankRelation> {

    List<MemBankRelation> queryList(Page<MemBankRelation> page, BankCardPageReq req);
}
