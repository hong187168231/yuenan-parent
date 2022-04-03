package com.indo.game.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.indo.admin.api.SysIpLimitClient;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.IPUtils;
import com.indo.game.service.ae.AeService;
import com.indo.game.service.awc.AwcService;
import com.indo.game.service.cmd.CmdService;
import com.indo.game.service.cq.CqService;
import com.indo.game.service.dj.DjService;
import com.indo.game.service.fc.FCService;
import com.indo.game.service.jdb.JdbService;
import com.indo.game.service.jili.JiliService;
import com.indo.game.service.ka.KaService;
import com.indo.game.service.pg.PgService;
import com.indo.game.service.pp.PpService;
import com.indo.game.service.ps.PsService;
import com.indo.game.service.redtiger.RedtigerService;
import com.indo.game.service.rich.Rich88Service;
import com.indo.game.service.saba.SabaService;
import com.indo.game.service.sbo.SboService;
import com.indo.game.service.t9.T9Service;
import com.indo.game.service.ug.UgService;
import com.indo.game.service.wm.WmService;
import com.indo.game.service.yl.YlService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/games/platform")
@Slf4j
@Api(tags = "登录、退出平台")
public class GameController {
    @Autowired
    private AwcService awcService;
    @Autowired
    private UgService ugService;
    @Autowired
    private SboService sboSportsService;
    @Autowired
    private SabaService sabaService;
    @Autowired
    private JdbService jdbService;
    @Autowired
    private AeService aeService;
    @Autowired
    private CqService cqService;
    @Autowired
    private PgService pgService;
    @Autowired
    private T9Service t9Service;
    @Autowired
    private PpService ppService;
    @Autowired
    private PsService psService;
    @Autowired
    private Rich88Service rich88Service;
    @Autowired
    private KaService kaService;
    @Autowired
    private DjService djService;
    @Autowired
    private JiliService jiliService;
    @Autowired
    private FCService fcService;
    @Autowired
    private YlService ylService;
    @Autowired
    private RedtigerService redtigerService;
    @Autowired
    private CmdService cmdService;
    @Autowired
    private WmService wmService;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private SysIpLimitClient sysIpLimitClient;
    @ApiOperation(value = "登录平台或单一游戏登录", httpMethod = "POST")
    @PostMapping("/initGame")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isMobileLogin", value = "是否手机登录1：手机 0:PC", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "platform", value = "登录平台请输入平台代码parentName， 单一游戏登录请输入游戏代码", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "parentName", value = "第三方平台代码（AWC,UG,SBO,SABA,JDB,AE,CQ） ", paramType = "query", dataType = "string", required = true)
    })
    public Result initGame(@LoginUser LoginInfo loginUser, @RequestParam("isMobileLogin") String isMobileLogin, @RequestParam("platform") String platform,
                           @RequestParam("parentName") String parentName,
                           HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed("g100103", "会员登录失效，请重新登录！");
        }
        log.info("登录平台或单一游戏登录 进入游戏。。。 platform:{},loginUser:{},parentName:{}", platform, loginUser, parentName);
        //黑名单校验
        List<SysIpLimit> list =sysIpLimitClient.findSysIpLimitByType(1).getData();
        if(list!=null||list.size()>0){
            // 获取请求信息
            String clientIP = IPUtils.getIpAddr(request);
            Boolean status = false;
            for(SysIpLimit l :list){
                if(l.getIp().equals(clientIP)){
                    status=true;
                }
            }
            if(status){
                throw new BizException("非法的IP登录");
            }
        }
        RLock lock = redissonClient.getLock("INITGAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IPAddressUtil.getIpAddress(request);
                Result resultInfo = null;
                if ("AWC".equals(parentName)) {
                    resultInfo = awcService.awcGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("UG".equals(parentName)) {
                    String loginType = "Smart";
                    if (!"1".equals(isMobileLogin))
                        loginType = "PC";

                    resultInfo = ugService.ugGame(loginUser, ip, platform, loginType, parentName);
                }
                if ("SBO".equals(parentName)) {
                    resultInfo = sboSportsService.sboGame(loginUser, ip, platform, parentName);
                }
                if ("SABA".equals(parentName)) {
                    resultInfo = sabaService.sabaGame(loginUser, ip, platform, parentName);
                }
                if ("JDB".equals(parentName)) {
                    resultInfo = jdbService.jdbGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("AE".equals(parentName)) {
                    resultInfo = aeService.aeGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("CQ".equals(parentName)) {
                    resultInfo = cqService.cqGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("PG".equals(parentName)) {
                    resultInfo = pgService.pgGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("PS".equals(parentName)) {
                    resultInfo = psService.psGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("T9".equals(parentName)) {
                    resultInfo = t9Service.t9Game(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("PP".equals(parentName)) {
                    resultInfo = ppService.ppGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("RICH".equals(parentName)) {
                    resultInfo = rich88Service.rich88Game(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("KA".equals(parentName)) {
                    resultInfo = kaService.kaGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("S128".equals(parentName)) {
                    resultInfo = djService.djGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("JILI".equals(parentName)) {
                    resultInfo = jiliService.jiliGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("FC".equals(parentName)) {
                    resultInfo = fcService.fcGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("YL".equals(parentName)) {
                    resultInfo = ylService.ylGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("RT".equals(parentName)) {
                    resultInfo = redtigerService.redtigerGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("CMD".equals(parentName)) {
                    resultInfo = cmdService.cmdGame(loginUser, isMobileLogin, ip, platform, parentName);
                }
                if ("WM".equals(parentName)) {
                    resultInfo = wmService.wmGame(loginUser, isMobileLogin, ip, platform, parentName);
                }

                if (resultInfo == null) {
                    log.info("登录平台或单一游戏登录log {} loginPlatform result is null. params:{},ip:{},parentName:{}", loginUser.getId(), params, ip, parentName);
                    return Result.failed("g100104", "网络繁忙，请稍后重试！");
                }
                log.info("登录平台或单一游戏登录log {} loginPlatform resultInfo:{}, params:{},parentName:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params, parentName);
                return resultInfo;
            } else {
                log.info("登录平台或单一游戏登录log {} loginPlatform lock  repeat request. error");
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录平台或单一游戏登录log {} loginPlatform occur error:{}. params:{},parentName:{}", loginUser.getId(), e.getMessage(), params, parentName);
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "强迫登出玩家", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "第三方游戏平台代码 ", paramType = "query", dataType = "string", required = true)
    })
    @PostMapping("/logoutPlatform")
    public Result logout(@LoginUser LoginInfo loginUser, @RequestParam("platform") String platform, HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null) {
            return Result.failed("g100103", "会员登录失效，请重新登录！");
        }
        log.info("退出平台logout loginUser:{}, params:{}", loginUser, platform);

        try {
            Result resultInfo = new Result();
            String ip = IPAddressUtil.getIpAddress(request);
            if ("AWC".equals(platform)) {
                resultInfo = awcService.logout(loginUser, ip);
            }
            if ("UG".equals(platform)) {
                resultInfo = ugService.logout(loginUser, ip);
            }
            if ("SBO".equals(platform)) {
                resultInfo = sboSportsService.logout(loginUser, ip);
            }
            if ("SABA".equals(platform)) {
                resultInfo = sabaService.logout(loginUser, ip);
            }
            if ("JDB".equals(platform)) {
                resultInfo = jdbService.logout(loginUser, ip);
            }
            if ("AE".equals(platform)) {
                resultInfo = aeService.logout(loginUser, platform, ip);
            }
            if ("CQ".equals(platform)) {
                resultInfo = cqService.logout(loginUser, platform, ip);
            }
            if ("PG".equals(platform)) {
                resultInfo = pgService.logout(loginUser, platform, ip);
            }
            if ("PS".equals(platform)) {
                resultInfo = psService.logout(loginUser, platform, ip);
            }
            if ("T9".equals(platform)) {
                resultInfo = t9Service.logout(loginUser, platform, ip);
            }
            if ("PP".equals(platform)) {
                resultInfo = ppService.logout(loginUser, platform, ip);
            }
            if ("RICH".equals(platform)) {
                resultInfo = rich88Service.logout(loginUser, platform, ip);
            }
            if ("DJ".equals(platform)) {
                resultInfo = djService.logout(loginUser, platform, ip);
            }
            if ("JILI".equals(platform)) {
                resultInfo = jiliService.logout(loginUser, platform, ip);
            }
            if ("FC".equals(platform)) {
                resultInfo = fcService.logout(loginUser, platform, ip);
            }
            if ("YL".equals(platform)) {
                resultInfo = ylService.logout(loginUser, platform, ip);
            }
            if ("CMD".equals(platform)) {
                resultInfo = cmdService.logout(loginUser, platform, ip);
            }
            if ("WM".equals(platform)) {
                resultInfo = wmService.logout(loginUser, platform, ip);
            }
            if (resultInfo == null) {
                log.info("退出平台log {} loginPlatform result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            } else {
                if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                    return resultInfo;
                }
            }
            log.info("退出平台log {} loginPlatform resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("退出平台log {} logout occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }
}
