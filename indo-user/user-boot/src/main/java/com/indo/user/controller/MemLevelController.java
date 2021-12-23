package com.indo.user.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.vo.level.MemLevelVo;
import com.indo.user.service.IMemLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员等级表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Api(tags = "会员等级")
@RestController
@RequestMapping("/api/v1/users/level")
public class MemLevelController {

    @Autowired
    private IMemLevelService memLevelService;

    @ApiOperation(value = "查询会员等级信息", httpMethod = "GET")
    @GetMapping(value = "/info")
    public Result<MemLevelVo> findAllVips(@LoginUser LoginInfo loginInfo) {
        return Result.success(memLevelService.findInfo(loginInfo));
    }
}
