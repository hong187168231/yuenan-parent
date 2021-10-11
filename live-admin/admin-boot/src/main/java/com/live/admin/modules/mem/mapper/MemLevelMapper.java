package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.common.mybatis.base.mapper.SuperMapper;
import com.live.user.pojo.entity.MemLevel;
import com.live.user.pojo.vo.MemLevelVo;
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
