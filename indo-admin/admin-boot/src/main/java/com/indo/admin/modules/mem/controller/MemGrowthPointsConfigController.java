package com.indo.admin.modules.mem.controller;

import com.indo.admin.modules.mem.service.IMemGrowthPointsConfigService;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MemGrowthPointsConfigDTO;
import com.indo.user.pojo.entity.MemGrowthPointsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 成长积分配置Controller
 * </p>
 *
 * @author puff
 * @since 2021-08-30
 */
@Api(tags = "成长积分配置接口")
@RestController
@RequestMapping("/api/v1/growthPoints")
public class MemGrowthPointsConfigController {

    @Autowired
    private IMemGrowthPointsConfigService configService;

    @ApiOperation(value = "查询所有成长积分配置信息")
    @GetMapping(value = "/list")
    public Result list(){
        List<MemGrowthPointsConfig> list = configService.list();
        return Result.success(list);
    }

    @ApiOperation(value = "修改成长积分配置信息")
    @PostMapping("/update")
    public Result update(@RequestBody List<MemGrowthPointsConfigDTO> list){
        int count = configService.updateGrowthPoints(list);
        if(count > 0){
            return Result.success();
        }else {
            return Result.failed();
        }
    }
}
