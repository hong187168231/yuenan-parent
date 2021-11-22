package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBank;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.service.IMemLevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 会员等级表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/mem/level")
public class MemLevelController {

    @Autowired
    private IMemLevelService memLevelService;

    @ApiOperation(value = "查询会员级别", httpMethod = "POST")
    @PostMapping(value = "/findAllVips")
    @AllowAccess
    public Result<List<MemLevel>> findAllVips() {
        return Result.success(memLevelService.findAllVips());
    }
}
