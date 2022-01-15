package com.indo.user.rpc;

import com.indo.common.result.Result;
import com.indo.user.pojo.bo.MemTradingBO;
import com.indo.user.service.AppMemBaseInfoService;
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
    private AppMemBaseInfoService memBaseInfoService;

    @GetMapping("/getMemTradingInfo/{account}")
    public Result<MemTradingBO> getMemTradingInfo(@PathVariable String account) {
        return Result.success(memBaseInfoService.tradingInfo(account));
    }


}
    