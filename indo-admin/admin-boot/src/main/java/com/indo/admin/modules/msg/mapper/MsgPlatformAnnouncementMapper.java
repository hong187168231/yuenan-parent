package com.indo.admin.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.indo.user.pojo.entity.MsgPlatformAnnouncement;
import com.indo.user.pojo.vo.MsgPlatformAnnouncementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 平台公告表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-09-06
 */
@Mapper
public interface MsgPlatformAnnouncementMapper extends BaseMapper<MsgPlatformAnnouncement> {
    /**
     * 分页查询
     * @param
     * @return
     */
    List<MsgPlatformAnnouncementVO> queryList(@Param("page") Page<MsgPlatformAnnouncementVO> page, @Param("dto") MsgPlatformAnnouncementDTO dto);

    /**
     * 批量删除公告信息
     * @param ids
     */
    int deleteAnnouncement(List<Long> ids);

    /**
     * 修改公告信息
     * @param
     */
    int updateAnnouncement(MsgPlatformAnnouncement msgPlatformAnnouncement);
}
