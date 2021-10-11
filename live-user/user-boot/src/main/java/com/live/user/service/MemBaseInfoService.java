package com.live.user.service;


import com.live.common.mybatis.base.service.SuperService;
import com.live.common.result.Result;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.vo.AppLoginVo;
import com.live.user.pojo.req.LoginReq;
import com.live.user.pojo.req.RegisterReq;

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
    MemBaseinfo getMemBaseInfoByAccount(String account);

}
