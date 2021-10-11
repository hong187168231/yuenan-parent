package com.live.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.pay.pojo.dto.PayBankConfigDto;
import com.live.pay.pojo.dto.PayGroupConfigDto;
import com.live.pay.pojo.entity.PayBankConfig;
import com.live.pay.pojo.entity.PayGroupConfig;
import com.live.pay.pojo.vo.PayBankConfigVO;
import com.live.pay.pojo.vo.PayGroupConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 支付层级配置持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Mapper
public interface PayGroupConfigMapper extends BaseMapper<PayGroupConfig> {
    /**
     * 支付层级配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayGroupConfigVO> queryList(@Param("page") Page<PayGroupConfigVO> page, @Param("dto") PayGroupConfigDto dto);

    /**
     * 查询单个支付层级配置
     * @param dto
     * @return
     */
    PayGroupConfigVO querySingle(@Param("dto") PayGroupConfigDto dto);

    /**
     * 支付层级配置写入
     * @param payGroupConfig
     */
    void insertPayGroup(PayGroupConfig payGroupConfig);

    /**
     * 支付层级配置更新
     * @param payGroupConfig
     */
    void updatePayGroup(PayGroupConfig payGroupConfig);

    /**
     * 支付层级配置删除
     * @param dto
     */
    void deletePayGroup(PayGroupConfigDto dto);

    /**
     * 停启支付层级配置
     * @param dto
     */
    void stopStartPayGroup(PayGroupConfigDto dto);

    /**
     * 根据ids查询name
     * @param list
     * @return
     */
    List<String> selectNameByIds(List<String> list);
}
