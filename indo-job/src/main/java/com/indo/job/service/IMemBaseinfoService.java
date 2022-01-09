package com.indo.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.user.pojo.entity.MemBaseinfo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员基础信息表 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
public interface IMemBaseinfoService extends IService<MemBaseinfo> {

    void upLevel(String payLoad);

    List<Long> findIdListByCreateTime(Date addDay);

}
