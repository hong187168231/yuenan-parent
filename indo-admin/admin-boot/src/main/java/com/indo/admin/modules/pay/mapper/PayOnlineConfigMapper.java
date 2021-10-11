package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.pay.pojo.dto.PayOnlineConfigDto;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayOnlineConfig;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayOnlineConfigVO;
import com.indo.pay.pojo.vo.PayWayConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 在线支付配置持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Mapper
public interface PayOnlineConfigMapper extends BaseMapper<PayOnlineConfig> {
    /**
     * 在支付配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayOnlineConfigVO> queryList(@Param("page") Page<PayOnlineConfigVO> page, @Param("dto") PayOnlineConfigDto dto);

    /**
     * 查询单个在线支付配置
     * @param dto
     * @return
     */
    PayOnlineConfigVO querySingle(@Param("dto") PayOnlineConfigDto dto);

    /**
     * 在线支付配置写入
     * @param payOnlineConfig
     */
    void insertPayOnline(PayOnlineConfig payOnlineConfig);

    /**
     * 在线支付配置更新
     * @param payOnlineConfig
     */
    void updatePayOnline(PayOnlineConfig payOnlineConfig);

    /**
     * 在线支付配置复制
     * @param payOnlineConfig
     */
    void copyPayOnline(PayOnlineConfig payOnlineConfig);

    /**
     * 在线支付配置删除
     * @param dto
     */
    void deletePayOnline(PayOnlineConfigDto dto);

    /**
     * 停启在线支付配置
     * @param dto
     */
    void stopStartPayOnline(PayOnlineConfigDto dto);
}
