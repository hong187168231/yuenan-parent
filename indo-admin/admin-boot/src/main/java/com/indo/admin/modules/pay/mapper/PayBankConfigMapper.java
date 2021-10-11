package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.pay.pojo.dto.PayBankConfigDto;
import com.indo.pay.pojo.dto.PayCashConfigDto;
import com.indo.pay.pojo.entity.PayBankConfig;
import com.indo.pay.pojo.entity.PayCashConfig;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.pay.pojo.vo.PayCashConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 银行支付配置持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Mapper
public interface PayBankConfigMapper extends BaseMapper<PayBankConfig> {
    /**
     * 银行支付配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayBankConfigVO> queryList(@Param("page") Page<PayBankConfigVO> page, @Param("dto") PayBankConfigDto dto);

    /**
     * 查询单个银行支付配置
     * @param dto
     * @return
     */
    PayBankConfigVO querySingle(@Param("dto") PayBankConfigDto dto);

    /**
     * 银行支付配置写入
     * @param payBankConfig
     */
    void insertPayBank(PayBankConfig payBankConfig);

    /**
     * 银行支付配置更新
     * @param payBankConfig
     */
    void updatePayBank(PayBankConfig payBankConfig);

    /**
     * 银行支付配置删除
     * @param dto
     */
    void deletePayBank(PayBankConfigDto dto);

    /**
     * 停启银行支付配置
     * @param dto
     */
    void stopStartPayBank(PayBankConfigDto dto);
}
