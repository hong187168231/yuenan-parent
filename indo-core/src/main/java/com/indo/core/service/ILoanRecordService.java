package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.entity.LoanRecord;
import com.indo.core.pojo.vo.LoanRecordVo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

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
    void loanMoney(BigDecimal amount,LoginInfo loginInfo, HttpServletRequest request);

//    /**
//     * 自动还款
//     */
//    void automaticbBackMoney();

    /**
     * 主动还款
     */
    void activeBackMoney(BigDecimal amount,LoginInfo loginInfo, HttpServletRequest request);

    /**
     * 查询用户借款相关信息
     * @return
     */
    LoanRecordVo findMemLoanInfo(LoginInfo loginInfo, HttpServletRequest request);
}
