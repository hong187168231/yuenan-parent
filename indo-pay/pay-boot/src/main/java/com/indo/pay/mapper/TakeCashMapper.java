package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Mapper
public interface TakeCashMapper extends BaseMapper<PayTakeCash> {

    List<TakeCashRecordVO> takeCashList(@Param("page") Page<TakeCashRecordVO> page, @Param("memId") Long memId);

}
