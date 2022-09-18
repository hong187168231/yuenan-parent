package com.indo.admin.modules.ade.controller;


import com.indo.admin.modules.ade.service.IAdvertiseService;
import com.indo.admin.pojo.vo.act.AdvertiseVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.AdvertiseQueryReq;
import com.indo.user.pojo.req.agent.AdvertiseReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 站内信 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Api(tags = "广告管理")
@RestController
@RequestMapping("/api/v1/ade")
public class AdvertiseController {


    @Autowired
    private IAdvertiseService iAdvertiseRecordService;

    @ApiOperation(value = "分页查询广告")
    @GetMapping(value = "/list")
    public Result<List<AdvertiseVO>> list(AdvertiseQueryReq queryDTO) {
        return iAdvertiseRecordService.queryList(queryDTO);
    }

    @ApiOperation(value = "增加广告")
    @PostMapping(value = "/add")
    public Result add(AdvertiseReq advertiseDTO) {
        return Result.judge(iAdvertiseRecordService.add(advertiseDTO));
    }


    @ApiOperation(value = "编辑广告")
    @PostMapping(value = "/edit")
    public Result edit(AdvertiseReq advertiseDTO,
                       HttpServletRequest request) {
        return Result.judge(iAdvertiseRecordService.edit(advertiseDTO,request));
    }

    @ApiOperation(value = "删除广告")
    @DeleteMapping(value = "/{adeId}")
    @ApiImplicitParam(name = "adeId", value = "广告id", required = true, paramType = "path", dataType = "long")
    public Result delete(@PathVariable Long adeId,HttpServletRequest request) {
        return Result.judge(iAdvertiseRecordService.delAde(adeId,request));
    }


    @ApiOperation(value = "广告上下架")
    @PutMapping(value = "/operateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adeId", value = "广告id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态 0 下架1 上架", required = true, paramType = "query", dataType = "int")
    })
    public Result operateStatus(@RequestParam("adeId") Long adeId,@RequestParam("status") Integer status,
                                HttpServletRequest request) {
        return Result.judge(iAdvertiseRecordService.operateStatus(adeId,status,request));
    }

}
