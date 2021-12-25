package com.indo.user.controller;


import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.entity.Activity;
import com.indo.admin.pojo.entity.ActivityType;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.pojo.req.gift.GiftReceiveReq;
import com.indo.user.pojo.vo.act.ActivityTypeVo;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.service.IMemGiftReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */
@Api(tags = "app礼金接口")
@RestController
@RequestMapping("/api/v1/users/gift")
public class GiftController {

    @Resource
    private IMemGiftReceiveService iMemGiftReceiveService;

    @ApiOperation(value = "礼金领取")
    @PostMapping(value = "/receive")
    public Result add(GiftReceiveReq giftReceiveReq, @LoginUser LoginInfo loginInfo) {
        return Result.judge(iMemGiftReceiveService.saveMemGiftReceive(giftReceiveReq, loginInfo));
    }


}
