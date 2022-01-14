package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.PayBank;
import com.indo.pay.pojo.vo.PayBankVO;

import java.util.List;

/**
 * <p>
 * 支付银行表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
public interface IPayBankService extends IService<PayBank> {

    List<PayBankVO> bankList();

}
