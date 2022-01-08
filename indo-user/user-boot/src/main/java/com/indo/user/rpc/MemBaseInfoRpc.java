package com.indo.user.rpc;

import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.service.MemBaseInfoService;
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


    @GetMapping("/getMemBaseInfo/{userId}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable Long userId) {
        return Result.success(memBaseInfoService.getMemBaseInfoById(userId));
    }

    @GetMapping("/getByAccount/{account}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable String account) {
        return Result.success(memBaseInfoService.getByAccount(account));
    }


}    
    