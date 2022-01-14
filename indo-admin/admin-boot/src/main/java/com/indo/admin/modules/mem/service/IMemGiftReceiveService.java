package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.core.pojo.entity.MemGiftReceive;

/**
 * <p>
 * 活动类型表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-22
 */
public interface IMemGiftReceiveService extends IService<MemGiftReceive> {


    Page<MemGiftReceiveVO> receiveList(GiftReceiveDTO queryDto);


}
