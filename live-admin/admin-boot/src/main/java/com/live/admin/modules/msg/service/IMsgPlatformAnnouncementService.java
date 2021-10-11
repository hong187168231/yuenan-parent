package com.live.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.mybatis.base.PageResult;
import com.live.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.live.user.pojo.entity.MsgPlatformAnnouncement;
import com.live.user.pojo.vo.MsgPlatformAnnouncementVO;

import java.util.List;

/**
 * <p>
 * 平台公告表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-09-06
 */
public interface IMsgPlatformAnnouncementService extends IService<MsgPlatformAnnouncement> {
    /**
     * 分页查询
     * @param announcementDTO
     * @return
     */
    PageResult<MsgPlatformAnnouncementVO> queryList(MsgPlatformAnnouncementDTO announcementDTO);

    /**
     * 根据Id查询单个公告信息
     * @param id
     * @return
     */
    MsgPlatformAnnouncementVO queryInfo(Long id);

    /**
     * 新增公告信息
     * @param
     * @return
     */
    int addInfo(MsgPlatformAnnouncementDTO announcementDTO);

    /**
     * 编辑公告信息
     * @param
     * @return
     */
    int updateInfo(MsgPlatformAnnouncementDTO announcementDTO);

    /**
     * 批量删除公告信息
     * @param
     * @return
     */
    int deleteInfo(List<Long> ids);
}
