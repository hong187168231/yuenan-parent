package com.indo.admin.modules.stat.controller;


import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.service.IUserRetentionService;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/stat/user-retention")
public class UserRetentionController {

    @Autowired
    private IUserRetentionService userRetentionService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<PageResult<UserRetentionVo>> getMemBaseInfo(@RequestBody UserRetentionPageReq req) {
        PageResult<UserRetentionVo> result = userRetentionService.queryList(req);
        return Result.success(result);
    }
}
