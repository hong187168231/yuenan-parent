package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.SysParameter;
import com.indo.core.service.ISysParameterService;
import io.minio.messages.Item;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
public class AppParamController {

    @Autowired
    private ISysParameterService iSysParameterService;

    @ApiOperation(value = "根据参数编码获取参数值")
    @GetMapping(value = "/{paramCode}")
    @ApiImplicitParam(name = "paramCode", value = "参数code 多个用,号分隔", required = true, paramType = "path", dataType = "String")
    @AllowAccess
    public Result<List<SysParameter>> getByCode(@PathVariable String paramCode) {
        List<SysParameter> parameterList = new LinkedList<>();
        if (paramCode.contains(",")) {
            List<String> strsToList1 = Arrays.asList(paramCode.split(","));
            strsToList1.forEach(item -> {
                parameterList.add(iSysParameterService.getByCode(item));
            });
        } else {
            parameterList.add(iSysParameterService.getByCode(paramCode));
        }
        return Result.success(parameterList);
    }
    @ApiOperation(value = "查询AB程序切换时间(分钟)")
    @GetMapping("/findProgramSwitchTime")
    public Result<SysParameter> findProgramSwitchTime() {
        return Result.success(iSysParameterService.findProgramSwitchTime());
    }

}
