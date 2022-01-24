package com.indo.user.service;


import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.bo.MemTradingBO;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import org.apache.ibatis.annotations.Param;

public interface AppMemBaseInfoService extends SuperService<MemBaseinfo> {


    boolean checkAccount(String account);

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


    /**
     * 修改密码
     *
     * @param req
     * @param loginUser
     * @return
     */
    boolean updatePassword(UpdatePasswordReq req, LoginInfo loginUser);

    /**
     * 修改头像
     *
     * @param headImage
     * @param loginUser
     * @return
     */
    boolean updateHeadImage(String headImage, LoginInfo loginUser);

    /**
     * 更新个人信息
     *
     * @param req
     * @param loginUser
     */
    void updateBaseInfo(UpdateBaseInfoReq req, LoginInfo loginUser);

    /**
     * 更加账号查询用户信息
     *
     * @param account
     * @return
     */
    MemBaseInfoBO findMemBaseInfo(String account);


    /**
     * 用户交易余额信息
     *
     * @param account
     * @return
     */
    MemTradingBO tradingInfo(String account);


}
