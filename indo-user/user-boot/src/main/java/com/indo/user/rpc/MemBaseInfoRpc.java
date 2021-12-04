package com.indo.user.rpc;


import com.indo.admin.pojo.entity.SysParameter;
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
    public MemBaseinfo getMemBaseInfo(@PathVariable Long id){
        return memBaseInfoService.getMemBaseInfoById(id);
    }

    @GetMapping("/getByAccountNo/{accno}")
    public MemBaseinfo getMemBaseInfo(@PathVariable String accountNo){
        return memBaseInfoService.getByAccountNo(accountNo);
    }

}    
    