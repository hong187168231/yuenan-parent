package com.live.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.mybatis.base.PageResult;
import com.live.user.pojo.dto.MsgStationLetterDTO;
import com.live.user.pojo.entity.MsgStationLetter;
import com.live.user.pojo.vo.MsgStationLetterVO;

/**
 * <p>
 * 站内信 服务类
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
public interface IMsgStationLetterService extends IService<MsgStationLetter> {
    /**
     * 分页查询
     * @param letterDTO
     * @return
     */
    PageResult<MsgStationLetterVO> queryList(MsgStationLetterDTO letterDTO);

    /**
     * 新增站内信
     * @param
     * @return
     */
    int add(MsgStationLetterDTO letterDTO);

}
