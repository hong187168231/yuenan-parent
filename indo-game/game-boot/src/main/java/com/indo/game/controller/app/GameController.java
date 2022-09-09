package com.indo.game.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.indo.admin.api.SysIpLimitClient;
import com.indo.common.annotation.LoginUser;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.IPUtils;
import com.indo.core.pojo.entity.SysIpLimit;
import com.indo.game.service.ae.AeService;
import com.indo.game.service.ag.AgService;
import com.indo.game.service.awc.AwcService;
import com.indo.game.service.bl.BlService;
import com.indo.game.service.bti.BtiService;
import com.indo.game.service.cmd.CmdService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cq.CqService;
import com.indo.game.service.dg.DgService;
import com.indo.game.service.dj.DjService;
import com.indo.game.service.fc.FCService;
import com.indo.game.service.jdb.JdbService;
import com.indo.game.service.jili.JiliService;
import com.indo.game.service.ka.KaService;
import com.indo.game.service.km.KmService;
import com.indo.game.service.mg.MgService;
import com.indo.game.service.mt.MtService;
import com.indo.game.service.ob.ObService;
import com.indo.game.service.pg.PgService;
import com.indo.game.service.pp.PpService;
import com.indo.game.service.ps.PsService;
import com.indo.game.service.redtiger.RedtigerService;
import com.indo.game.service.rich.Rich88Service;
import com.indo.game.service.sa.SaService;
import com.indo.game.service.saba.SabaService;
import com.indo.game.service.sbo.SboService;
import com.indo.game.service.sgwin.SGWinService;
import com.indo.game.service.t9.T9Service;
import com.indo.game.service.tcg.TCGWinService;
import com.indo.game.service.tp.TpService;
import com.indo.game.service.ug.UgService;
import com.indo.game.service.v8.V8Service;
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
import org.springframework.web.bind.annotation.*;

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
    private MgService mgService;
    @Autowired
    private RedtigerService redtigerService;
    @Autowired
    private CmdService cmdService;
    @Autowired
    private WmService wmService;
    @Autowired
    private DgService dgService;
    @Autowired
    private BtiService btiService;
    @Autowired
    private MtService mtService;
    @Autowired
    private KmService kmService;
    @Autowired
    private V8Service v8Service;
    @Autowired
    private BlService blService;
    @Autowired
    private ObService obService;
    @Autowired
    private SaService saService;
    @Autowired
    private TpService tpService;
    @Autowired
    private AgService agService;
    @Autowired
    private SGWinService sgWinService;
    @Autowired
    private TCGWinService tcgWinService;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private SysIpLimitClient sysIpLimitClient;
    @Autowired
    private GameLogoutService gameLogoutService;

    @ApiOperation(value = "登录平台或单一游戏登录", httpMethod = "POST")
    @PostMapping(value ="/initGame",produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isMobileLogin", value = "是否手机登录1：手机 0:PC", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "platform", value = "登录平台请输入平台代码parentName， 单一游戏登录请输入游戏代码", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "parentName", value = "第三方平台代码（AWC,UG,SBO,SABA,JDB,AE,CQ） ", paramType = "query", dataType = "string", required = true)
    })
    @ResponseBody
    public Result initGame(@LoginUser LoginInfo loginUser, @RequestParam("isMobileLogin") String isMobileLogin, @RequestParam("platform") String platform,
                           @RequestParam("parentName") String parentName,
                           HttpServletRequest request) throws InterruptedException {
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed("g100103", MessageUtils.get("g100103",countryCode));
        }
        log.info("登录平台或单一游戏登录 进入游戏。。。 platform:{},loginUser:{},parentName:{},isMobileLogin:{},countryCode:{}", platform, loginUser, parentName,isMobileLogin,countryCode);
        //黑名单校验
        List<SysIpLimit> list =sysIpLimitClient.findSysIpLimitByType(1).getData();
        if(list!=null&&list.size()>0){
            // 获取请求信息
            String clientIP = IPUtils.getIpAddr(request);
            Boolean status = false;
            for(SysIpLimit l :list){
                if(l.getIp().equals(clientIP)){
                    status=true;
                }
            }
            if(status){
                return Result.failed("g000003", MessageUtils.get("g000003",countryCode));
            }
        }
        RLock lock = redissonClient.getLock("INITGAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IPAddressUtil.getIpAddress(request);
                Result resultInfo = null;
                if (OpenAPIProperties.AWC_PLATFORM_CODE.equals(parentName)) {//AE真人视讯
                    resultInfo = awcService.awcGame(loginUser, isMobileLogin, ip, platform, parentName,countryCode);
                }
                if (OpenAPIProperties.UG_PLATFORM_CODE.equals(parentName)) {
                    String loginType = "mobile";
                    if (!"1".equals(isMobileLogin))
                        loginType = "pc";

                    resultInfo = ugService.ugGame(loginUser, ip, platform, loginType, parentName,countryCode);
                }
                if (OpenAPIProperties.SBO_PLATFORM_CODE.equals(parentName)) {
                    String loginType = "m";//mobile
                    if (!"1".equals(isMobileLogin))
                        loginType = "d";//desktop
                    resultInfo = sboSportsService.sboGame(loginUser, ip, platform, parentName,loginType,countryCode);
                }
                if (OpenAPIProperties.SABA_PLATFORM_CODE.equals(parentName)) {
                    String loginType = "2";//mobile
                    if (!"1".equals(isMobileLogin))
                        loginType = "1";//desktop
                    resultInfo = sabaService.sabaGame(loginUser, ip, platform, parentName,loginType,countryCode);
                }
                if (OpenAPIProperties.JDB_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = jdbService.jdbGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.AE_PLATFORM_CODE.equals(parentName)) {//AE电子
                    resultInfo = aeService.aeGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.CQ_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = cqService.cqGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.PG_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = pgService.pgGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.PS_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = psService.psGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.T9_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = t9Service.t9Game(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.PP_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = ppService.ppGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.RICH_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = rich88Service.rich88Game(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.KA_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = kaService.kaGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.DJ_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = djService.djGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.JILI_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = jiliService.jiliGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.FC_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = fcService.fcGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.YL_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = ylService.ylGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.REDTIGER_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = redtigerService.redtigerGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.MG_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = mgService.mgGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.CMD_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = cmdService.cmdGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.WM_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = wmService.wmGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.DG_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = dgService.dgGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.BTI_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = btiService.btiGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.MT_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = mtService.mtGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.KM_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = kmService.kmGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.V8_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = v8Service.v8Game(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.BL_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = blService.blGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.OB_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = obService.obGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.SA_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = saService.saGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }

                if (OpenAPIProperties.TP_PLATFORM_CODE.equals(parentName)) {
                    String loginType = "mobile";
                    if (!"1".equals(isMobileLogin))
                        loginType = "web";
                    resultInfo = tpService.tpGame(loginUser, loginType, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.AG_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = agService.agGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.SGWIN_PLATFORM_CODE.equals(parentName)) {
                    resultInfo = sgWinService.sgwinGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }
                if (OpenAPIProperties.TCGWIN_PLATFORM_CODE.equals(platform)) {
                    resultInfo = tcgWinService.tcgwinGame(loginUser, isMobileLogin, ip, platform, parentName, countryCode);
                }

                if (resultInfo == null) {
                    log.info("登录平台或单一游戏登录log {} loginPlatform result is null. params:{},ip:{},parentName:{}", loginUser.getId(), params, ip, parentName);
                    return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
                }
                log.info("登录平台或单一游戏登录log {} loginPlatform resultInfo:{}, params:{},parentName:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params, parentName);
                return resultInfo;
            } else {
                log.info("登录平台或单一游戏登录log {} loginPlatform lock  repeat request. error");
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录平台或单一游戏登录log {} loginPlatform occur error:{}. params:{},parentName:{}", loginUser.getId(), e.getMessage(), params, parentName);
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
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
            @ApiImplicitParam(name = "platform", value = "第三方平台代码 ", paramType = "query", dataType = "string", required = true)
    })
    @PostMapping(value ="/logoutPlatform",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result logout(@LoginUser LoginInfo loginUser, @RequestParam("platform") String platform, HttpServletRequest request) throws InterruptedException {
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        String params = "";
        if (loginUser == null) {
            return Result.failed("g100103", MessageUtils.get("g100103",countryCode));
        }
        log.info("退出平台logout loginUser:{}, params:{}", loginUser, platform);

        String account = loginUser.getAccount();
        try {
            Result resultInfo = new Result();
            String ip = IPAddressUtil.getIpAddress(request);
            if (OpenAPIProperties.AWC_PLATFORM_CODE.equals(platform)) {
                resultInfo = awcService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.UG_PLATFORM_CODE.equals(platform)) {
                resultInfo = ugService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.SBO_PLATFORM_CODE.equals(platform)) {
                resultInfo = sboSportsService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.SABA_PLATFORM_CODE.equals(platform)) {
                resultInfo = sabaService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.JDB_PLATFORM_CODE.equals(platform)) {
                resultInfo = jdbService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.AE_PLATFORM_CODE.equals(platform)) {
                resultInfo = aeService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.CQ_PLATFORM_CODE.equals(platform)) {
                resultInfo = cqService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.PG_PLATFORM_CODE.equals(platform)) {
                resultInfo = pgService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.PS_PLATFORM_CODE.equals(platform)) {
                resultInfo = psService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.T9_PLATFORM_CODE.equals(platform)) {
                resultInfo = t9Service.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.PP_PLATFORM_CODE.equals(platform)) {
                resultInfo = ppService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.RICH_PLATFORM_CODE.equals(platform)) {
                resultInfo = rich88Service.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.KA_PLATFORM_CODE.equals(platform)) {
                resultInfo = kaService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.DJ_PLATFORM_CODE.equals(platform)) {
                resultInfo = djService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.JILI_PLATFORM_CODE.equals(platform)) {
                resultInfo = jiliService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.FC_PLATFORM_CODE.equals(platform)) {
                resultInfo = fcService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.YL_PLATFORM_CODE.equals(platform)) {
                resultInfo = ylService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.REDTIGER_PLATFORM_CODE.equals(platform)) {
                resultInfo = redtigerService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.CMD_PLATFORM_CODE.equals(platform)) {
                resultInfo = cmdService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.WM_PLATFORM_CODE.equals(platform)) {
                resultInfo = wmService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.MG_PLATFORM_CODE.equals(platform)) {
                resultInfo = mgService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.DG_PLATFORM_CODE.equals(platform)) {
                resultInfo = dgService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.BTI_PLATFORM_CODE.equals(platform)) {
                resultInfo = btiService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.MT_PLATFORM_CODE.equals(platform)) {
                resultInfo = mtService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.KM_PLATFORM_CODE.equals(platform)) {
                resultInfo = kmService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.V8_PLATFORM_CODE.equals(platform)) {
                resultInfo = v8Service.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.BL_PLATFORM_CODE.equals(platform)) {
                resultInfo = blService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.OB_PLATFORM_CODE.equals(platform)) {
                resultInfo = obService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.SA_PLATFORM_CODE.equals(platform)) {
                resultInfo = saService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.TP_PLATFORM_CODE.equals(platform)) {
                resultInfo = tpService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.SGWIN_PLATFORM_CODE.equals(platform)) {
                resultInfo = sgWinService.logout(account, platform, ip,countryCode);
            }
            if (OpenAPIProperties.TCGWIN_PLATFORM_CODE.equals(platform)) {
                resultInfo = tcgWinService.logout(account, platform, ip,countryCode);
            }
            if (resultInfo == null) {
                log.info("退出平台log {} loginPlatform result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
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
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @ApiOperation(value = "强迫登出游戏", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户账号 ", paramType = "query", dataType = "string", required = true)
    })
    @PostMapping(value ="/gameLogout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result gameLogout(String account, HttpServletRequest request){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        String ip = IPAddressUtil.getIpAddress(request);
        log.info("强迫登出游戏gameLogout loginUser:{}, ip:{}", account,ip);
        return gameLogoutService.gamelogout( account,  ip,  countryCode);
    }

}
