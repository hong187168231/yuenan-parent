package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.req.PayTakeCashReq;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Mapper
public interface PayTakeCashMapper extends BaseMapper<PayTakeCash> {



    List<PayTakeCashApplyVO> cashApplyList(Page<PayTakeCashApplyVO> page, PayTakeCashReq dto);

}
