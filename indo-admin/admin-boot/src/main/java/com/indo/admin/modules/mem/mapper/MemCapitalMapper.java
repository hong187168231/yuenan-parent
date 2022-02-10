package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.CapitalDTO;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.admin.pojo.vo.mem.MemTradingVO;
import com.indo.core.pojo.entity.MemGiftReceive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动类型表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Mapper
public interface MemCapitalMapper  {

    List<MemTradingVO> capitalList(@Param("page") Page<MemTradingVO> page, @Param("dto") CapitalDTO dto);

}
