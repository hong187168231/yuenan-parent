package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.mem.MemBankPageReq;
import com.indo.admin.pojo.vo.mem.MemBankVO;
import com.indo.core.pojo.entity.MemBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Mapper
public interface MemBankMapper extends BaseMapper<MemBank> {
    /**
     * 分页查询用户银行卡信息
     *
     * @param page
     * @param req
     * @return
     */
    List<MemBankVO> queryList(@Param("page") Page<MemBankVO> page, @Param("req") MemBankPageReq req);
}
