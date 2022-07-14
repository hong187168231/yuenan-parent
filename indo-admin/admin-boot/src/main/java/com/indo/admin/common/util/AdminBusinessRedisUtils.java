package com.indo.admin.common.util;

import com.indo.admin.pojo.req.mem.MemRebateAddReq;
import com.indo.common.constant.AppConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.ResultCode;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.MemLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @ClassName CommonRedisUtils
 * @Desc 公共redis工具类
 * @Date 2021/3/26 10:14
 */
@Slf4j
public class AdminBusinessRedisUtils extends RedisUtils {


    /**
     * 添加返点配置
     *
     * @param req
     */
    public static void addMemRebate(MemRebateAddReq req) {
       set(RedisKeys.SYS_REBATE_KEY, req.getBetList());
    }


    public static void delMemAccToken(String account) {
        String token = get(AppConstants.USER_LOGIN_ACCTOKEN + account);
        del(AppConstants.USER_LOGIN_ACCTOKEN + token);
        del(AppConstants.USER_LOGIN_INFO_KEY + account);
    }

    /**
     * 刷新等级缓存
     *
     * @param list
     */
    public static void refreshMemLevel(List<MemLevel> list) {
        RedisUtils.del(RedisKeys.SYS_LEVEL_KEY);
        RedisUtils.lSet(RedisKeys.SYS_LEVEL_KEY, list.toArray());
    }



}    
    