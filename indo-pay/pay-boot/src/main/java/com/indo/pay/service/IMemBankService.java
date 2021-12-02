package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.pay.pojo.entity.MemBank;
import com.indo.pay.pojo.vo.MemBankVO;
import org.elasticsearch.client.license.LicensesStatus;

import java.util.List;

/**
 * <p>
 *
 * 银行信息配置表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-29
 */
public interface IMemBankService extends IService<MemBank> {

    List<MemBankVO> memBankList(LoginInfo loginInfo);


}
