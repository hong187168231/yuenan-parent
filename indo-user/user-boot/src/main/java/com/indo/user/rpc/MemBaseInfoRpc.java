package com.indo.user.rpc;


import com.alibaba.fastjson.JSON;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.service.IMemGoldChangeService;
import com.indo.user.service.MemBaseInfoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 * 用户信息 rpc控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/rpc/memBaseInfo")
public class MemBaseInfoRpc {

    @Resource
    private MemBaseInfoService memBaseInfoService;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    @GetMapping("/getMemBaseInfo/{userId}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable Long userId) {
        return Result.success(memBaseInfoService.getMemBaseInfoById(userId));
    }

    @GetMapping("/getByAccount/{account}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable String account) {
        return Result.success(memBaseInfoService.getByAccount(account));
    }

    @PostMapping("/updateMemGoldChange")
    public Boolean updateMemGoldChange(@RequestBody MemGoldChangeDTO goldChangeDTO) {
        return iMemGoldChangeService.updateMemGoldChange(goldChangeDTO);
    }

}    
    