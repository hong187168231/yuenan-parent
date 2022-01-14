package com.indo.admin.modules.ade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.vo.AdvertiseVO;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.Advertise;
import com.indo.user.pojo.dto.AdvertiseQueryDTO;
import com.indo.user.pojo.dto.AdvertiseDTO;

import java.util.List;

/**
 * <p>
 * 站内信 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
public interface IAdvertiseService extends IService<Advertise> {

    /**
     * 分页查询
     *
     * @param pushRecordDTO
     * @return
     */
    Result<List<AdvertiseVO>> queryList(AdvertiseQueryDTO pushRecordDTO);

    /**
     * 新增广告
     *
     * @param advertiseDTO
     * @return
     */
    boolean add(AdvertiseDTO advertiseDTO);


    /**
     * 编辑广告
     *
     * @param advertiseDTO
     * @return
     */
    boolean edit(AdvertiseDTO advertiseDTO);

    /**
     * 删除广告
     *
     * @param adeId
     * @return
     */
    boolean delAde(Long adeId);

    /**
     * 广告上下架
     *
     * @param adeId
     * @param status
     * @return
     */
    boolean operateStatus(Long adeId, Integer status);

}
