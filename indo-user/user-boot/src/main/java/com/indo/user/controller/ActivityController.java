package com.indo.user.controller;


import com.indo.admin.pojo.entity.Activity;
import com.indo.admin.pojo.entity.ActivityType;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.admin.pojo.vo.ActivityTypeVO;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.vo.MemBankVo;
import com.indo.user.pojo.vo.PayBankVO;
import com.indo.user.pojo.vo.act.ActivityTypeVo;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.service.IMemBankRelationService;
import com.indo.user.service.IPayBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */
@Api(tags = "app活动接口")
@RestController
@RequestMapping("/api/v1/users/act")
public class ActivityController {

    @Resource
    private DozerUtil dozerUtil;

    @ApiOperation(value = "查询活动类型列表", httpMethod = "GET")
    @GetMapping(value = "/typeList")
    @AllowAccess
    public Result<List<ActivityTypeVo>> typeList() {
        Map<Object, Object> map = UserBusinessRedisUtils.hmget(RedisConstants.ACTIVITY_TYPE_KEY);
        List<ActivityType> activityTypeList = new ArrayList(map.values());
        List<ActivityTypeVo> voTypeList = dozerUtil.convert(activityTypeList, ActivityTypeVo.class);
        return Result.success(voTypeList);
    }


    @ApiOperation(value = "查询活动列表", httpMethod = "GET")
    @GetMapping(value = "/actList")
    @AllowAccess
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actTypeId", value = "活动类型id ", paramType = "query", dataType = "long", required = false)
    })
    public Result<List<ActivityVo>> actList(@RequestParam("actTypeId") Long actTypeId) {
        Map<Object, Object> map = UserBusinessRedisUtils.hmget(RedisConstants.ACTIVITY_KEY);
        List<Activity> activityList = new ArrayList(map.values());
        if (actTypeId != null) {
            activityList = activityList.stream()
                    .filter(act -> !actTypeId.equals(act.getActTypeId()))
                    .collect(Collectors.toList());
        }
        List<ActivityVo> voTypeList = dozerUtil.convert(activityList, ActivityVo.class);
        Iterator<ActivityVo> iter = voTypeList.iterator();
        while (iter.hasNext()) {
            ActivityVo item = iter.next();
            if (item.getStatus().equals(GlobalConstants.ACT_SOLD_OUT)) {
                iter.remove();
                continue;
            }
            if (item.getStatus().equals(GlobalConstants.ACT_DATED)) {
                iter.remove();
                continue;
            }
            if (item.getIsPer() == false) {
                LocalDateTime currentDate = LocalDateTime.now();
                if (item.getBeginTime().isBefore(currentDate)) {
                    iter.remove();
                    continue;
                }
                if (item.getEndTime().isAfter(currentDate)) {
                    iter.remove();
                    continue;
                }
            }
        }
        return Result.success(voTypeList);
    }


}
