//package com.indo.admin.modules.sys.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.indo.common.result.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 系统参数 前端控制器
// * </p>
// *
// * @author puff
// * @since 2021-09-02
// */
//@Api(tags = "系统参数接口")
//@RestController
//@RequestMapping("/api/v1/sysparam")
//@Slf4j
//public class SysParameterController {
//
//    @Resource
//    private ISysParameterService iSysParameterService;
//
//    @ApiOperation(value = "系统参数列表")
//    @GetMapping(value = "/list")
//    public Result list(SysParameterQueryReq req) {
//        Page<SysParameter> paramPage = new Page<>(req.getPage(), req.getLimit());
//        iSysParameterService.selectAll(req, paramPage);
//        return Result.success(paramPage.getRecords(), paramPage.getTotal());
//    }
//
//    @ApiOperation(value = "根据id查询系统参数")
//    @GetMapping(value = "/{paramId}")
//    @ApiImplicitParam(name = "paramId", value = "系统参数id", required = true, paramType = "path", dataType = "int")
//    public Result getInfo(@PathVariable Long paramId) {
//        return Result.success(iSysParameterService.selectById(paramId));
//    }
//
//    @ApiOperation(value = "保存系统参数")
//    @PostMapping
//    public Result save(SysParameterReq parameter) {
//        iSysParameterService.saveSysParameter(parameter);
//        return Result.success();
//    }
//
//    @ApiOperation(value = "修改系统参数")
//    @PutMapping
//    public Result edit(@Validated SysParameterReq parameter) {
//        iSysParameterService.updateSysParameter(parameter);
//        return Result.success();
//    }
//
//    @ApiOperation(value = "删除系统参数")
//    @DeleteMapping("/{ids}")
//    @ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "path", dataType = "String")
//    public Result remove(@PathVariable("ids") String ids) {
//        List<String> strIds = Arrays.asList(ids.split(","));
//        List idsLong = strIds.stream().map(Long::parseLong).collect(Collectors.toList());
//        iSysParameterService.deleteById(idsLong);
//        return Result.success();
//    }
//
//    /**
//     * 刷新参数缓存
//     */
//    @ApiOperation(value = "刷新系统参数")
//    @GetMapping("/refreshCache")
//    public Result refreshCache() {
//        iSysParameterService.refreshSysParameterCache();
//        return Result.success();
//    }
//
//
//}
