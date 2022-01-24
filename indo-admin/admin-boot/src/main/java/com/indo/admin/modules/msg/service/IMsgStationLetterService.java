package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.core.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.req.msg.StationLetterAddReq;
import com.indo.user.pojo.req.msg.StationLetterQueryReq;

import java.util.List;

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
     *
     * @param queryDTO
     * @return
     */
    Page<MsgStationLetterVO> queryList(StationLetterQueryReq queryDTO);

    /**
     * 新增站内信
     *
     * @param
     * @return
     */
    int add(StationLetterAddReq letterDTO);


    List<MsgStationLetterVO> getPersonalMsg(MsgDTO msgDTO);


    int personalMsgTotal(MsgDTO msgDTO);

}
