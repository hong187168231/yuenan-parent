package com.indo.admin.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.sys.service.ISysParameterService;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 系统参数 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Api(tags = "系统参数接口")
@RestController
@RequestMapping("/api/v1/sysparam")
@Slf4j
public class SysParameterController {


    @Resource
    private ISysParameterService iSysParameterService;

    @ApiOperation(value = "系统参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long", required = true)
    })
    @GetMapping(name = "系统参数", value = "/list")
    public Result list(SysParameter req, Integer limit,
                       Integer page) {
        Page<SysParameter> paramPage = new Page<>(page, limit);
        return Result.success(iSysParameterService.selectAll(req.getParamCode(), paramPage));
    }


    @ApiOperation(value = "根据id查询系统参数")
    @GetMapping(value = "/{parameterId}")
    public Result getInfo(@PathVariable Long parameterId) {
        return Result.success(iSysParameterService.selectById(parameterId));
    }


    @ApiOperation(value = "保存系统参数")
    @PostMapping
    public Result save(SysParameter parameter) {
        parameter.setCreateUser(JwtUtils.getUsername());
        iSysParameterService.saveSysParameter(parameter);
        return Result.success();
    }


    @ApiOperation(value = "修改系统参数")
    @PutMapping
    public Result edit(@Validated @RequestBody SysParameter parameter) {
        parameter.setUpdateUser(JwtUtils.getUsername());
        iSysParameterService.updateSysParameter(parameter);
        return Result.success();
    }


    @ApiOperation(value = "删除系统参数")
    @DeleteMapping("/{paramIds}")
    public Result remove(@PathVariable Long[] paramIds) {
        iSysParameterService.deleteById(paramIds);
        return Result.success();
    }


    /**
     * 刷新参数缓存
     */
    @ApiOperation(value = "刷新系统参数")
    @GetMapping("/refreshCache")
    public Result refreshCache() {
        iSysParameterService.refreshSysParameterCache();
        return Result.success();
    }


}
