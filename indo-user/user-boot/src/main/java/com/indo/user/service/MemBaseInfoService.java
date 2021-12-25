package com.indo.user.service;


import com.indo.common.mybatis.base.service.SuperService;
import com.indo.common.pojo.bo.LoginInfo;
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
import com.indo.user.pojo.vo.mem.MemTradingVo;

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
     * @param id
     * @return
     */
    MemBaseInfoVo getMemBaseInfo(Long id);

    Result<MemBaseInfoVo> getMemInfo(MemInfoReq req);

    boolean updatePassword(UpdatePasswordReq req, LoginInfo loginUser);

    void updateBaseInfo(UpdateBaseInfoReq req, LoginInfo loginUser);

    MemBaseinfo getMemBaseInfoById(Long id);

    MemBaseinfo getByAccount(String account);

    MemBaseinfo findByMobile(String mobule);

    int updateMemberAmount(BigDecimal amount, BigDecimal canAmount, Long userId);

    MemTradingVo tradingInfo(Long memId);


}
