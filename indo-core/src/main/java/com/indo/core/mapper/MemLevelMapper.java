package com.indo.core.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.entity.MemLevel;
import com.indo.core.pojo.vo.MemLevelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户等级表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Mapper
@Repository
public interface MemLevelMapper extends SuperMapper<MemLevel> {

    List<MemLevelVo> listByMemLevel(@Param("page") Page<MemLevelVo> page);

    /**
     * 根据ids查询name
     * @param list
     * @return
     */
    List<String> selectNameByIds(List<String> list);
}
