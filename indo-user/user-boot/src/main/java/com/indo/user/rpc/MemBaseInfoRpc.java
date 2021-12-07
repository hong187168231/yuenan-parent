package com.indo.user.rpc;


import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.service.IMemGoldChangeService;
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

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    @GetMapping("/getMemBaseInfo/{userId}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable Long id) {
        return Result.success(memBaseInfoService.getMemBaseInfoById(id));
    }

    @GetMapping("/getByAccountNo/{accountNo}")
    public Result<MemBaseinfo> getMemBaseInfo(@PathVariable String accountNo) {
        return Result.success(memBaseInfoService.getByAccountNo(accountNo));
    }

    @PostMapping("/updateMemGoldChange")
    public boolean updateMemGoldChange(@RequestBody MemGoldChangeDTO goldChangeDTO) {
        return iMemGoldChangeService.updateMemGoldChange(goldChangeDTO);
    }

}    
    