package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.admin.pojo.vo.MsgStationLetterVO;
import com.indo.common.result.PageResult;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.dto.StationLetterAddDTO;
import com.indo.user.pojo.dto.StationLetterQueryDTO;

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
    Page<MsgStationLetterVO> queryList(StationLetterQueryDTO queryDTO);

    /**
     * 新增站内信
     *
     * @param
     * @return
     */
    int add(StationLetterAddDTO letterDTO);


    List<MsgStationLetter> getPersonalMsg(MsgDTO msgDTO);

}
