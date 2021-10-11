package com.live.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.entity.PayCashConfig;
import com.live.pay.pojo.vo.PayCashConfigVO;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.entity.ManualDepositWithDraw;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;

import java.util.List;

/**
 * <p>
 * 提款配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-08
 */
public interface IPayCashConfigService extends IService<PayCashConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayCashConfigVO> queryList(Page<PayCashConfigVO> page, PayCashConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayCashConfigVO querySingle(PayCashConfigDto dto);

    /**
     * 提取配置写入
     * @param dto
     */
    Result insert(PayCashConfigDto dto);

    /**
     * 提取配置更新
     * @param dto
     */
    Result updateCash(PayCashConfigDto dto);

    /**
     * 提取配置删除
     * @param dto
     */
    Result deleteCash(PayCashConfigDto dto);
}
