package com.indo.user.common.util;


import com.alibaba.fastjson.JSON;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.constant.AppConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.web.exception.BizException;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName UserBusinessRedisUtils
 * @Desc 用户业务redis类
 * @Date 2021/3/29 9:31
 */
public class UserBusinessRedisUtils extends RedisUtils {


    public static String createMemAccToken(MemBaseinfo userInfo) {
        String secrecy = userInfo.getAccountNo() + RandomUtil.uuid();
        String accToken = MD5.md5(secrecy, "UTF-8");
        set(AppConstants.USER_LOGIN_ACCTOKEN + accToken, JSON.toJSONString(userInfo), 60 * 60 * 24 * 7);
        set(AppConstants.USER_LOGIN_INFO_KEY + userInfo.getAccountNo(), accToken, 60 * 60 * 24 * 7);
        return accToken;
    }

    /**
     * 删除用户缓存
     *
     * @param id
     */
    public static void deleteAppMember(Long id) {
        if (null != id && id > 0) {
            redisTemplate.delete(RedisKeys.APP_MEMBER + id);
        }
    }


    /**
     * 删除系统参数
     *
     * @param code
     */
    public static void deleteSysParameter(String code) {
        if (StringUtils.isNotBlank(code)) {
            code = code.trim().toUpperCase();
            del(RedisKeys.SYS_PARAMETER_CODE + code);
        }
    }


    /**
     * 获取系统参数
     *
     * @param key
     * @return
     */
    public static SysParameter getSysParameter(String key) {
        if (StringUtils.isEmpty(key)) {
            paramError();
        }
        SysParameter cacheParameter = get(RedisKeys.SYS_PARAMETER_CODE + key.toUpperCase());
        return cacheParameter;
    }

    /**
     * 增加系统参数
     *
     * @param info
     */
    public static void addSysParameter(SysParameter info) {
        if (null == info || StringUtils.isEmpty(info.getParamCode())) {
            paramError();
        }
        set(RedisKeys.SYS_PARAMETER_CODE + info.getParamCode().toUpperCase(), info);
    }


    public static void paramError() {
        throw new BizException(ResultCode.SYSPARAMETER_EMPTY.getCode());
    }



}
    