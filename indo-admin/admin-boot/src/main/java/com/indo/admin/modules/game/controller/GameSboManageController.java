package com.indo.admin.modules.game.controller;

import com.indo.admin.modules.game.service.SboService;
import com.indo.admin.pojo.dto.game.sbo.SboAgentDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentPresetBetDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentStatusDTO;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.i18n.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/game/sbo")
@Slf4j
@AllArgsConstructor
@Api(tags = "游戏管理-SBO体育")
public class GameSboManageController {

    @Autowired
    private SboService sboService;


    @ApiOperation(value = "注册代理", httpMethod = "POST")
    @PostMapping(value = "/registerAgent")
    public Result registerAgent(@LoginUser LoginInfo loginUser, SboAgentDTO sboAgentDTO, HttpServletRequest request) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        String ip = IPAddressUtil.getIpAddress(request);
        log.info("SBO体育log {} registerAgent 注册代理。。。registerAgent:{}",ip,sboAgentDTO,loginUser);
        return sboService.registerAgent(sboAgentDTO,loginUser,ip);
    }

    @ApiOperation(value = "更新代理状态", httpMethod = "POST")
    @PostMapping(value = "/updateAgentStatus")
    public Result updateAgentStatus(@LoginUser LoginInfo loginUser, SboUpdateAgentStatusDTO sboUpdateAgentStatusDTO, HttpServletRequest request) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        String ip = IPAddressUtil.getIpAddress(request);
        log.info("SBO体育log {} registerAgent 注册代理。。。registerAgent:{}",ip,sboUpdateAgentStatusDTO,loginUser);
        return sboService.updateAgentStatus(sboUpdateAgentStatusDTO,loginUser,ip);
    }

    @ApiOperation(value = "修改代理预设下注设定", httpMethod = "POST")
    @PostMapping(value = "/updateAgentPresetBet")
    public Result updateAgentPresetBet(@LoginUser LoginInfo loginUser, SboUpdateAgentPresetBetDTO sboUpdateAgentPresetBetDTO, HttpServletRequest request) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        String ip = IPAddressUtil.getIpAddress(request);
        log.info("SBO体育log {} registerAgent 注册代理。。。registerAgent:{}",ip,sboUpdateAgentPresetBetDTO,loginUser);
        return sboService.updateAgentPresetBet(sboUpdateAgentPresetBetDTO,loginUser,ip);
    }
}
