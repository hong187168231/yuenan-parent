package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.msg.mapper.MsgAddressMapper;
import com.indo.admin.modules.msg.service.IMsgAddressService;
import com.indo.admin.pojo.vo.msg.MsgAddressVO;
import com.indo.core.pojo.entity.MsgAddress;
import com.indo.user.pojo.req.msg.MsgAddressQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息官方地址
 *
 * @author justin
 */
@Service
public class MsgAddressServiceImpl extends ServiceImpl<MsgAddressMapper, MsgAddress>
		implements IMsgAddressService {

	@Autowired
	private MsgAddressMapper msgAddressMapper;

	@Override
	public Page<MsgAddressVO> queryList(MsgAddressQueryReq queryDTO) {
		Page<MsgAddressVO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
		page.setRecords(msgAddressMapper.queryList(page, null));
		return page;
	}
}
