package com.indo.user.service;


import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;

public interface MemBaseInfoService extends SuperService<MemBaseinfo> {

    /**
     * 用户登录
     *
     * @param req
     * @return
     */
    Result<AppLoginVo> appLogin(LoginReq req);

    /**
     * 退出登录
     *
     * @param req
     * @return
     */
    boolean logout(LogOutReq req);


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
    MemBaseInfoVo getMemBaseInfo(String account);

    boolean updatePassword(UpdatePasswordReq req, LoginInfo loginUser);

    boolean updateHeadImage(String headImage, LoginInfo loginUser);

    void updateBaseInfo(UpdateBaseInfoReq req, LoginInfo loginUser);

    MemBaseinfo getMemBaseInfoById(Long id);

    MemBaseinfo getByAccount(String account);

    MemBaseinfo findByMobile(String mobule);

    MemTradingVo tradingInfo(Long memId);


}
