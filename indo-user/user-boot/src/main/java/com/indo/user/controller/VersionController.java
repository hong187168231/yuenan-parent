package com.indo.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.admin.pojo.vo.act.ActivityTypeVO;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.ActivityType;
import com.indo.core.pojo.entity.AppVersion;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.AppVersionMapper;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.pojo.vo.app.AppVersionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * app版本信息 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */
@Api(tags = "app版本接口")
@RestController
@RequestMapping("/api/v1/users/version")
public class VersionController {

    @Resource
    private DozerUtil dozerUtil;

    @Resource
    private AppVersionMapper appVersionMapper;


    @ApiOperation(value = "app版本更新信息", httpMethod = "GET")
    @GetMapping(value = "/updateInfo")
    @AllowAccess
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型 ", paramType = "query", dataType = "int", defaultValue = "0")
    })
    public Result<AppVersionVo> updateInfo(@RequestParam(value = "deviceType") Integer deviceType) {
        Map<Object, Object> map = UserBusinessRedisUtils.hmget(RedisConstants.APP_VERSION_KEY);
        List<AppVersion> versionList = new ArrayList(map.values());
        if (CollectionUtil.isEmpty(versionList)) {
            versionList = appVersionMapper.selectList(new LambdaQueryWrapper<AppVersion>().
                    eq(AppVersion::getEnable, 1));
        }
        List<AppVersionVo> voVersionList = dozerUtil.convert(versionList, AppVersionVo.class);
        voVersionList = voVersionList.stream()
                .filter(act -> deviceType.equals(act.getDeviceType()))
                .collect(Collectors.toList());
        List<AppVersionVo> collect = voVersionList.
                stream().
                sorted(Comparator.comparing(AppVersionVo::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return Result.success(collect.get(0));
    }


}
