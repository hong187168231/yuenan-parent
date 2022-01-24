package com.indo.user.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.vo.Invite.InviteCodeVo;
import com.indo.user.service.IMemInviteCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 会员邀请码 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-05
 */
@Api(tags = {"邀请码信息"})
@RestController
@RequestMapping("/api/v1/mem/inviteCode")
public class InviteCodeController {

    @Autowired
    private IMemInviteCodeService memInviteCodeService;

    @ApiOperation(value = "邀请码信息")
    @PostMapping(value = "/info")
    public Result<InviteCodeVo> page(@LoginUser LoginInfo loginInfo) {
        InviteCodeVo info = memInviteCodeService.findInviteCode(loginInfo.getId());
        return Result.success(info);
    }

}
