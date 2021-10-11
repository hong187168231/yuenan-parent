package com.live.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.entity.PayCashConfig;
import com.live.pay.pojo.vo.PayCashConfigVO;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.entity.ManualDepositWithDraw;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:10
 * @Version: 1.0.0
 * @Desc:
 */
@Mapper
public interface PayCashConfigMapper extends BaseMapper<PayCashConfig> {
    /**
     * 出款配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayCashConfigVO> queryList(@Param("page") Page<PayCashConfigVO> page, @Param("dto") PayCashConfigDto dto);

    /**
     * 查询单个出款配置
     * @param dto
     * @return
     */
    PayCashConfigVO querySingle(@Param("dto") PayCashConfigDto dto);

    /**
     * 出款配置写入
     * @param payCashConfig
     */
    void insertCash(PayCashConfig payCashConfig);

    /**
     * 出款配置更新
     * @param payCashConfig
     */
    void updateCash(PayCashConfig payCashConfig);

    /**
     * 出款配置删除
     * @param dto
     */
    void deleteCash(PayCashConfigDto dto);
}
