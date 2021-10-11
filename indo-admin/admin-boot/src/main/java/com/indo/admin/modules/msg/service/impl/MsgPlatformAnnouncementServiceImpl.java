package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.msg.mapper.MsgPlatformAnnouncementMapper;
import com.indo.admin.modules.msg.service.IMsgPlatformAnnouncementService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.entity.MsgPlatformAnnouncement;
import com.indo.user.pojo.vo.MsgPlatformAnnouncementVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 平台公告表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-09-06
 */
@Service
public class MsgPlatformAnnouncementServiceImpl extends ServiceImpl<MsgPlatformAnnouncementMapper, MsgPlatformAnnouncement> implements IMsgPlatformAnnouncementService {

    @Autowired
    private MsgPlatformAnnouncementMapper announcementMapper;
    @Autowired
    private MemLevelMapper memLevelMapper;

    @Override
    public PageResult<MsgPlatformAnnouncementVO> queryList(MsgPlatformAnnouncementDTO announcementDTO) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != announcementDTO.getPage() && null != announcementDTO.getLimit()){
            pageNum = announcementDTO.getPage();
            pageSize = announcementDTO.getLimit();
        }
        Page<MsgPlatformAnnouncementVO> page =  new Page<>(pageNum, pageSize);
        List<MsgPlatformAnnouncementVO> list = announcementMapper.queryList(page, announcementDTO);
        // 插入等级名称
        list.forEach(data -> {
            List<String> levelNames = new ArrayList<>();
            List<String> levelIds= Arrays.asList(data.getLevelIds().split(","));
            levelIds.forEach(ids -> {
                MemLevel memLevel = memLevelMapper.selectById(ids);
                levelNames.add(memLevel.getName());
            });
            data.setLevelNames(levelNames);
        });
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public MsgPlatformAnnouncementVO queryInfo(Long id) {
        List<String> levelNames = new ArrayList<>();
        MsgPlatformAnnouncementVO announcementVO = new MsgPlatformAnnouncementVO();

        // 查询公告信息
        MsgPlatformAnnouncement announcement = announcementMapper.selectById(id);
        BeanUtils.copyProperties(announcement,announcementVO);
        // 插入等级名称
        List<String> levelIds= Arrays.asList(announcement.getLevelIds().split(","));
        levelIds.forEach(ids -> {
            MemLevel memLevel = memLevelMapper.selectById(ids);
            levelNames.add(memLevel.getName());
        });

        announcementVO.setLevelNames(levelNames);
        return announcementVO;
    }

    @Override
    public int addInfo(MsgPlatformAnnouncementDTO announcementDTO) {
        MsgPlatformAnnouncement announcement = new MsgPlatformAnnouncement();
        BeanUtils.copyProperties(announcementDTO,announcement);
        announcement.setLevelIds(StringUtils.strip(announcementDTO.getLevelIds().toString(),"[]"));
        announcement.setCreateTime(new Date());
        return announcementMapper.insert(announcement);
    }

    @Override
    public int updateInfo(MsgPlatformAnnouncementDTO announcementDTO) {
        MsgPlatformAnnouncement announcement = new MsgPlatformAnnouncement();
        BeanUtils.copyProperties(announcementDTO,announcement);
        if(!CollectionUtils.isEmpty(announcementDTO.getLevelIds())){
            announcement.setLevelIds(StringUtils.strip(announcementDTO.getLevelIds().toString(),"[]"));
        }
        announcement.setUpdateTime(new Date());
        return announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    public int deleteInfo(List<Long> ids) {
        return announcementMapper.deleteAnnouncement(ids);
    }
}
