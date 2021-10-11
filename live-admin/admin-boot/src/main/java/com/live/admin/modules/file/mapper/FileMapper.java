package com.live.admin.modules.file.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.pojo.entity.FileInfo;
import com.live.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 上传存储db
 *
 * @author zlt
 */
@Mapper
public interface FileMapper extends SuperMapper<FileInfo> {
    List<FileInfo> findList(Page<FileInfo> page, @Param("f") Map<String, Object> params);
}
