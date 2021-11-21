package com.indo.user.common.util;


import com.alibaba.fastjson.JSON;
import com.indo.common.constant.AppConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.user.pojo.entity.MemBaseinfo;

/**
 * @ClassName UserBusinessRedisUtils
 * @Desc 用户业务redis类
 * @Date 2021/3/29 9:31
 */
public class UserBusinessRedisUtils extends RedisUtils {


    public static String createMemAccToken(MemBaseinfo userInfo) {
        String secrecy = userInfo.getAccount() + RandomUtil.uuid();
        String accToken = MD5.md5(secrecy, "UTF-8");
        set(AppConstants.USER_LOGIN_ACCTOKEN + accToken, JSON.toJSONString(userInfo), 60 * 60 * 24 * 7);
        set(AppConstants.USER_LOGIN_INFO_KEY + userInfo.getAccount(), accToken, 60 * 60 * 24 * 7);
        return accToken;
    }


}
    