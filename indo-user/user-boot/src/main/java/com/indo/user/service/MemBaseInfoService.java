package com.indo.user.service;


import com.indo.common.mybatis.base.service.SuperService;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;

import java.math.BigDecimal;

public interface MemBaseInfoService extends SuperService<MemBaseinfo> {

    /**
     * 用户登录
     *
     * @param req
     * @return
     */
    Result<AppLoginVo> appLogin(LoginReq req);

    /**
     * 用户注册
     *
     * @param req
     * @return
     */
    Result<AppLoginVo> register(RegisterReq req);

    /**
     * 查询用户信息
     *
     * @param account
     * @return
     */
    MemBaseInfoVo getMemBaseInfoByAccount(String account);

    Result<MemBaseInfoVo> getMemInfo(MemInfoReq req);

    void updatePassword(UpdatePasswordReq req);

    void updateBaseInfo(UpdateBaseInfoReq req);

    void addbankCard(AddBankCardReq req);

    MemBaseinfo getMemBaseInfoById(Long id);

    MemBaseinfo getByAccountNo(String accountNo);

    int updateMemberAmount(BigDecimal amount, BigDecimal canAmount, Long userId);

}
