package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.entity.LoanRecord;

/**
 * <p>
 * 借款记录表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-07-28
 */
public interface ILoanRecordService extends IService<LoanRecord> {
    /**
     * 根据用户ID查询用户借款记录分页
     * @return
     */
    Page<LoanRecord> findLoanRecordPageByMemId(LoanRecordDTO loanRecordDTO, LoginInfo loginInfo);

    /**
     * 借款
     */
    void loanMoney(LoginInfo loginInfo);

    /**
     * 自动还款
     */
    void automaticbBackMoney(LoginInfo loginInfo);

    /**
     * 主动还款
     */
    void activeBackMoney(LoginInfo loginInfo);
}
