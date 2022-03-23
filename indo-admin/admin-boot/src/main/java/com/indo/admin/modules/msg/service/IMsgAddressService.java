package com.indo.admin.modules.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.vo.msg.MsgAddressVO;
import com.indo.core.pojo.entity.MsgAddress;
import com.indo.user.pojo.req.msg.MsgAddressQueryReq;

/**
 * 消息官方地址
 *
 * @author justin
 */
public interface IMsgAddressService extends IService<MsgAddress> {

    Page<MsgAddressVO> queryList(MsgAddressQueryReq queryDTO);
}
