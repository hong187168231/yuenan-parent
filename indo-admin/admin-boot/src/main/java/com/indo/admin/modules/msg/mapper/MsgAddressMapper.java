package com.indo.admin.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.msg.MsgAddressVO;
import com.indo.admin.pojo.vo.msg.MsgPushRecordVO;
import com.indo.core.pojo.entity.MsgAddress;
import com.indo.user.pojo.req.msg.MsgAddressQueryReq;
import com.indo.user.pojo.req.msg.PushRecordQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息官方地址Mapper
 * @author justin
 */
@Mapper
public interface MsgAddressMapper extends BaseMapper<MsgAddress> {
	/**
	 * 分页查询
	 * @param
	 * @return
	 */
	List<MsgAddressVO> queryList(@Param("page") Page<MsgAddressVO> page, @Param("dto") MsgAddressQueryReq dto);
}
