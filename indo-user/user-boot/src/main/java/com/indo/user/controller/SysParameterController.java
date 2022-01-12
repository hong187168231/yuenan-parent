package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.core.service.ISysParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 活动 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */

@Api(tags = "app系统参数接口")
@RestController
@RequestMapping("/api/v1/users/param")
public class SysParameterController {

    @Autowired
    private ISysParameterService iSysParameterService;

    @ApiOperation(value = "根据参数编码获取参数值")
    @GetMapping(value = "/{paramCode}")
    @ApiImplicitParam(name = "paramCode", value = "参数code", required = true, paramType = "path", dataType = "String")
    @AllowAccess
    public Result getByCode(@PathVariable String paramCode) {
        return Result.success(iSysParameterService.getByCode(paramCode));
    }


}
