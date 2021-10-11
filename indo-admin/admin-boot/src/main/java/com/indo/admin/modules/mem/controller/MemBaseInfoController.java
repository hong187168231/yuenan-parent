package com.indo.admin.modules.mem.controller;

import cn.hutool.crypto.SecureUtil;
import com.indo.admin.modules.mem.service.IMemBaseInfoService;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.user.pojo.dto.MemBaseInfoDto;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.vo.MemBaseInfoVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:15
 * @Version: 1.0.0
 * @Desc:
 */
@Api(tags = "会员用户接口")
@RestController
@RequestMapping("/api/v1/memBaseInfo")
@Slf4j
public class MemBaseInfoController {

    @Autowired
    private IMemBaseInfoService memBaseInfoService;

    @ApiOperation(value = "列表分页")
    @GetMapping(value = "/page")
    public Result<PageResult<MemBaseInfoVo>> list(MemBaseInfoDto memBaseInfoDto) {
        PageResult<MemBaseInfoVo> result = memBaseInfoService.queryList(memBaseInfoDto);
        return Result.success(result);
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)", required = true)
    @GetMapping(value = "/export")
    public void excelExport(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
        memBaseInfoService.excelExport(response, ids);
    }

    @ApiOperation(value = "新增会员")
    @PostMapping(value = "addGeneralUser")
    public Result addGeneralUser(MemBaseInfoDto memBaseInfoDto) {
        return memBaseInfoService.addGeneralUser(memBaseInfoDto);
    }

    @ApiOperation(value = "新增代理")
    @PostMapping(value = "addAgentUser")
    public Result addAgentUser(MemBaseInfoDto memBaseInfoDto) {
        return memBaseInfoService.addAgentUser(memBaseInfoDto);
    }

    @ApiOperation(value = "用户详情")
    @GetMapping(value = "detail/{id}")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    public Result addAgentUser(@PathVariable Long id) {
        return Result.success(memBaseInfoService.getById(id));
    }

    @ApiOperation(value = "编辑用户")
    @PutMapping(value = "update")
    public Result update(MemBaseinfo memBaseinfo) {
        boolean result = memBaseInfoService.updateById(memBaseinfo);
        if (result) {
            return Result.success();
        }
        return Result.failed();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true),
            @ApiImplicitParam(name = "status", value = "用户状态值：0 正常 1 冻结 2 停用"),
            @ApiImplicitParam(name = "prohibitInvite", value = "禁止邀请发展下级和会员：0 否 1 是"),
            @ApiImplicitParam(name = "prohibitInvestment", value = "禁止投注：0 否 1 是"),
            @ApiImplicitParam(name = "prohibitDisbursement", value = "禁止出款：0 否 1 是"),
            @ApiImplicitParam(name = "prohibitRecharge", value = "禁止出款：0 否 1 是"),
    })
    @ApiOperation(value = "冻结账户")
    @PutMapping(value = "changeStatus")
    public Result changeStatus(Long id, Integer status, Integer prohibitInvite, Integer prohibitInvestment, Integer prohibitDisbursement, Integer prohibitRecharge) {
        return memBaseInfoService.changeStatus(id, status, prohibitInvite, prohibitInvestment, prohibitDisbursement, prohibitRecharge);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true),
            @ApiImplicitParam(name = "maxCashNum", value = "每次出款款次数"),
            @ApiImplicitParam(name = "stageInvestmentLimit", value = "每期投注金额")
    })
    @ApiOperation(value = "金额限制")
    @PutMapping(value = "amountLimit")
    public Result amountLimit(Long id, Integer maxCashNum, Long stageInvestmentLimit) {
        if (Objects.isNull(id)) {
            return Result.failed("id不能为空！");
        }
        MemBaseinfo baseinfo = new MemBaseinfo();
        baseinfo.setId(id);
        baseinfo.setMaxCashNum(maxCashNum);
        baseinfo.setStageInvestmentLimit(stageInvestmentLimit);
        boolean result = memBaseInfoService.updateById(baseinfo);
        if (result) {
            return Result.success();
        }
        return Result.failed();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true),
            @ApiImplicitParam(name = "levelId", value = "层级ID", required = true)
    })
    @ApiOperation(value = "调整层级")
    @PutMapping(value = "updateLevel")
    public Result updateLevel(Long id, Long levelId) {
        if (Objects.isNull(id)) {
            return Result.failed("id不能为空！");
        }
        MemBaseinfo baseinfo = new MemBaseinfo();
        baseinfo.setId(id);
        baseinfo.setLevelId(levelId);
        boolean result = memBaseInfoService.updateById(baseinfo);
        if (result) {
            return Result.success();
        }
        return Result.failed();
    }

    @ApiOperation(value = "带玩会员列表")
    @GetMapping(value = "/playWithList")
    public Result<PageResult<MemBaseInfoVo>> playWithList(MemBaseInfoDto memBaseInfoDto) {
        memBaseInfoDto.setIdentityType(2);
        PageResult<MemBaseInfoVo> result = memBaseInfoService.queryList(memBaseInfoDto);
        return Result.success(result);
    }

    @ApiOperation(value = "导出带玩会员列表")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @GetMapping(value = "/exportPlayWithList")
    public void exportPlayWithList(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
        memBaseInfoService.exportPlayWithList(response, ids);
    }

    @ApiOperation(value = "带玩列表-批量修改密码")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @PutMapping(value = "/updatePlayWith")
    public Result updatePlayWith(@RequestParam(value = "ids") List<Long> ids, @RequestParam(value = "password") String password) {
        List<MemBaseinfo> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MemBaseinfo memBaseinfo = new MemBaseinfo();
                memBaseinfo.setId(id);
                if (StringUtils.isBlank(password)) {
                    memBaseinfo.setPassword(SecureUtil.md5("123456"));
                } else {
                    memBaseinfo.setPassword(SecureUtil.md5(password));
                }
                list.add(memBaseinfo);
            }
        }
        boolean result = memBaseInfoService.updateBatchById(list);
        if (result) {
            return Result.success();
        }
        return Result.failed();
    }

    @ApiOperation(value = "代理列表")
    @GetMapping(value = "agentPage")
    public Result<PageResult<AgentVo>> agentPage(MemBaseInfoDto memBaseInfoDto) {
        PageResult<AgentVo> result = memBaseInfoService.agentPage(memBaseInfoDto);
        return Result.success(result);
    }

    @ApiOperation(value = "清除密码错误次数")
    @PutMapping(value = "clearPwdErrorNum/{id}")
    public Result clearPwdErrorNum(@PathVariable Long id){
        int count = memBaseInfoService.clearPwdErrorNum(id);
        if (count > 0) {
            return Result.success();
        }
        return Result.failed();
    }


    @ApiOperation(value = "下级用户")
    @GetMapping(value = "subordinateMemPage")
    public Result<PageResult<MemBaseInfoVo>> subordinateMemPage(MemBaseInfoDto memBaseInfoDto) {
        PageResult<MemBaseInfoVo> result = memBaseInfoService.subordinateMemPage(memBaseInfoDto);
        return Result.success(result);
    }

    @ApiOperation(value = "批量用户校验")
    @GetMapping(value = "/check/list")
    public Result<List<MemBaseinfo>> checkList(MemBaseInfoDto memBaseInfoDto){
        List<MemBaseinfo> list = memBaseInfoService.checkList(memBaseInfoDto);
        return Result.success(list);
    }
}
