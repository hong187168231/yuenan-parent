package com.indo.admin.modules.sys.controller;

import com.indo.common.result.Result;
import com.indo.core.pojo.entity.SysParameter;
import com.indo.core.pojo.req.SysParameterReq;
import com.indo.core.service.ISysParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "程序包切换设置")
@RestController
@RequestMapping("/api/v1/ProgramSwitchController")
public class ProgramSwitchController {
    @Resource
    private ISysParameterService iSysParameterService;

    @ApiOperation(value = "查询程序切换时间(分钟)")
    @GetMapping("/findProgramSwitchTime")
    public Result<SysParameter> findProgramSwitchTime() {
        return Result.success(iSysParameterService.findProgramSwitchTime());
    }
    @ApiOperation(value = "新增程序切换时间(分钟)")
    @GetMapping("/insertProgramSwitchTime")
    @ApiImplicitParam(name = "minute", value = "时间（分钟）", required = true, paramType = "path", dataType = "String")
    public Result insertProgramSwitchTime(String minute) {
        iSysParameterService.insertProgramSwitchTime(minute);
        return Result.success();
    }
    @ApiOperation(value = "修改程序切换时间(分钟)")
    @GetMapping("/updateProgramSwitchTime")
    @ApiImplicitParam(name = "minute", value = "时间（分钟）", required = true, paramType = "path", dataType = "String")
    public Result updateProgramSwitchTime(String minute) {
        iSysParameterService.updateProgramSwitchTime(minute);
        return Result.success();
    }
}
