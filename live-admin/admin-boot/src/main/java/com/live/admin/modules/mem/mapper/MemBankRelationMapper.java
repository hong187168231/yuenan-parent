package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.user.pojo.dto.MembankRelationDTO;
import com.live.user.pojo.entity.MemBankRelation;
import com.live.user.pojo.vo.MemBankRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-08-31
 */
@Mapper
public interface MemBankRelationMapper extends BaseMapper<MemBankRelation> {
    /**
     * 分页查询用户银行卡信息
     * @param page
     * @param dto
     * @return
     */
    List<MemBankRelationVO> queryList(@Param("page") Page<MemBankRelationVO> page, @Param("dto") MembankRelationDTO dto);

    /**
     * 根据ids查询用户银行卡信息
     * @param ids
     * @return
     */
    List<MemBankRelationVO> getListByIds(@Param("ids") List<Long> ids);
}
