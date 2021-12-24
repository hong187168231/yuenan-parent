package com.indo.admin.modules.stat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.service.IUserRetentionService;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.common.result.Result;
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
 * 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
@Api(tags = "用户留存")
@RestController
@RequestMapping("/stat/user-retention")
public class UserRetentionController {

    @Autowired
    private IUserRetentionService userRetentionService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<List<UserRetentionVo>> getMemBaseInfo(@RequestBody UserRetentionPageReq req) {
        Page<UserRetentionVo> result = userRetentionService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
