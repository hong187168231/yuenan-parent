package com.indo.user.common.util;


import com.alibaba.fastjson.JSON;
import com.indo.common.constant.AppConstants;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.util.BusinessRedisUtils;
import com.indo.user.pojo.req.LogOutReq;

/**
 * @ClassName UserBusinessRedisUtils
 * @Desc 用户业务redis类
 * @Date 2021/3/29 9:31
 */
public class UserBusinessRedisUtils extends BusinessRedisUtils {

    public static String createMemAccToken(MemBaseinfoBo userInfo) {
        String secrecy = userInfo.getAccount() + RandomUtil.uuid();
        String accToken = MD5.md5(secrecy, "UTF-8");
        set(AppConstants.USER_LOGIN_ACCTOKEN + accToken, JSON.toJSONString(userInfo), 60 * 60 * 24 * 7);
        set(AppConstants.USER_LOGIN_INFO_KEY + userInfo.getAccount(), accToken, 60 * 60 * 24 * 7);
        saveMemBaseInfo(userInfo);
        return accToken;
    }

    public static void delMemAccToken(LogOutReq logOutReq) {
        del(AppConstants.USER_LOGIN_ACCTOKEN + logOutReq.getAccToken());
        del(AppConstants.USER_LOGIN_INFO_KEY + logOutReq.getAccount());
    }


}
    