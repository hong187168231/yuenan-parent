package com.indo.admin.modules.ade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.AdvertiseRecord;
import com.indo.admin.pojo.vo.AdvertiseRecordVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.AdvertiseQueryDTO;
import com.indo.user.pojo.dto.AdvertiseRecordDTO;

import java.util.List;

/**
 * <p>
 * 站内信 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
public interface IAdvertiseRecordService extends IService<AdvertiseRecord> {

    /**
     * 分页查询
     * @param pushRecordDTO
     * @return
     */
    Result<List<AdvertiseRecordVO>> queryList(AdvertiseQueryDTO pushRecordDTO);

    /**
     * 新增广告
     * @param pushRecordAddDTO
     * @return
     */
    boolean add(AdvertiseRecordDTO pushRecordAddDTO);


    /**
     * 编辑广告
     * @param pushRecordAddDTO
     * @return
     */
    boolean edit(AdvertiseRecordDTO pushRecordAddDTO);

}
