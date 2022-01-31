package com.indo.pay.controller;


import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.pay.pojo.req.TakeCashApplyReq;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
import com.indo.pay.service.ITakeCashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付渠道配置 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/pay/takeCash")
public class TakeCashController {

    @Resource
    private ITakeCashService iPayCashOrderService;


    @ApiOperation(value = "提现申请")
    @PostMapping(value = "/apply")
    public Result takeCashApply(TakeCashApplyReq cashApplyReq, @LoginUser LoginInfo loginInfo) {
        boolean flag = iPayCashOrderService.takeCashApply(cashApplyReq, loginInfo);
        return Result.judge(flag);
    }


    @ApiOperation(value = "提现记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "Long")
    })
    @GetMapping(value = "/list")
    public Result<List<TakeCashRecordVO>> cashList(@RequestParam Integer page,
                                                   Integer limit, @LoginUser LoginInfo loginInfo) {
        return iPayCashOrderService.cashRecordList(page, limit, loginInfo);
    }


}
