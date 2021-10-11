package com.indo.admin.rpc;


import com.indo.admin.modules.sys.service.ISysParameterService;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 * 系统参数 rpc控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/rpc/sysparam")
public class SysParameterRpc {


    @Resource
    private ISysParameterService iSysParameterService;

    /**
     * 根据参数code 获取系统参数
     *
     * @param paramCode
     * @return
     */
    @GetMapping
    public Result<SysParameter> getByParamCode(@RequestParam("paramCode") String paramCode) {
        return Result.success(iSysParameterService.getByCode(paramCode));
    }


}    
    