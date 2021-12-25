package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.user.pojo.entity.MemGiftReceive;
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
public interface MemGiftReceiveMapper extends BaseMapper<MemGiftReceive> {



}
