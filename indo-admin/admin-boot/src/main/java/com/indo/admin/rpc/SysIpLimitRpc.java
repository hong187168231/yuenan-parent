package com.indo.admin.rpc;

import com.indo.admin.modules.sys.service.ISysIpLimitService;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rpc/sysIpLimit")
public class SysIpLimitRpc {
    @Resource
    private ISysIpLimitService saveSysParameter;
    @GetMapping("/findSysIpLimitByType")
    public Result<List<SysIpLimit>> findSysIpLimitByType(@RequestParam("types") Integer types) {
        return Result.success(saveSysParameter.findSysIpLimitByType(types));
    }
}
