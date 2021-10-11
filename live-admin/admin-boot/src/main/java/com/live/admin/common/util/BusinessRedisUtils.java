package com.live.admin.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.live.admin.pojo.entity.SysParameter;
import com.live.common.constant.RedisKeys;
import com.live.common.redis.utils.RedisUtils;
import com.live.common.result.ResultCode;
import com.live.common.web.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @ClassName CommonRedisUtils
 * @Desc 公共redis工具类
 * @Date 2021/3/26 10:14
 */
@Slf4j
public class BusinessRedisUtils extends RedisUtils {

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
    