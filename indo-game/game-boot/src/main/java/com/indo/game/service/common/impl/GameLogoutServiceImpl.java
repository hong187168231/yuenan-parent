package com.indo.game.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.admin.api.SysIpLimitClient;
import com.indo.common.annotation.LoginUser;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.util.IPUtils;
import com.indo.core.pojo.entity.SysIpLimit;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.service.ae.AeService;
import com.indo.game.service.ag.AgService;
import com.indo.game.service.awc.AwcService;
import com.indo.game.service.bl.BlService;
import com.indo.game.service.bti.BtiService;
import com.indo.game.service.cmd.CmdService;
import com.indo.game.service.common.GameCommonService;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GameLogoutServiceImpl implements GameLogoutService {
    @Resource
    private GameCommonService gameCommonService;
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

    @Override
    public Result logout(String account, String ip, String parentName, String countryCode) {

        try {
            Result resultInfo = new Result();
            if (OpenAPIProperties.AWC_PLATFORM_CODE.equals(parentName)) {
                resultInfo = awcService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.UG_PLATFORM_CODE.equals(parentName)) {
                resultInfo = ugService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.SBO_PLATFORM_CODE.equals(parentName)) {
                resultInfo = sboSportsService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.SABA_PLATFORM_CODE.equals(parentName)) {
                resultInfo = sabaService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.JDB_PLATFORM_CODE.equals(parentName)) {
                resultInfo = jdbService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.AE_PLATFORM_CODE.equals(parentName)) {
                resultInfo = aeService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.CQ_PLATFORM_CODE.equals(parentName)) {
                resultInfo = cqService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.PG_PLATFORM_CODE.equals(parentName)) {
                resultInfo = pgService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.PS_PLATFORM_CODE.equals(parentName)) {
                resultInfo = psService.logout(account,parentName,ip,countryCode);
            }
            if (OpenAPIProperties.T9_PLATFORM_CODE.equals(parentName)) {
                resultInfo = t9Service.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.PP_PLATFORM_CODE.equals(parentName)) {
                resultInfo = ppService.logout(account,parentName,ip,countryCode);
            }
            if (OpenAPIProperties.RICH_PLATFORM_CODE.equals(parentName)) {
                resultInfo = rich88Service.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.KA_PLATFORM_CODE.equals(parentName)) {
                resultInfo = kaService.logout(account,parentName,ip,countryCode);
            }
            if (OpenAPIProperties.DJ_PLATFORM_CODE.equals(parentName)) {
                resultInfo = djService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.JILI_PLATFORM_CODE.equals(parentName)) {
                resultInfo = jiliService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.FC_PLATFORM_CODE.equals(parentName)) {
                resultInfo = fcService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.YL_PLATFORM_CODE.equals(parentName)) {
                resultInfo = ylService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.REDTIGER_PLATFORM_CODE.equals(parentName)) {
                resultInfo = redtigerService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.CMD_PLATFORM_CODE.equals(parentName)) {
                resultInfo = cmdService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.WM_PLATFORM_CODE.equals(parentName)) {
                resultInfo = wmService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.MG_PLATFORM_CODE.equals(parentName)) {
                resultInfo = mgService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.DG_PLATFORM_CODE.equals(parentName)) {
                resultInfo = dgService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.BTI_PLATFORM_CODE.equals(parentName)) {
                resultInfo = btiService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.MT_PLATFORM_CODE.equals(parentName)) {
                resultInfo = mtService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.KM_PLATFORM_CODE.equals(parentName)) {
                resultInfo = kmService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.V8_PLATFORM_CODE.equals(parentName)) {
                resultInfo = v8Service.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.BL_PLATFORM_CODE.equals(parentName)) {
                resultInfo = blService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.OB_PLATFORM_CODE.equals(parentName)) {
                resultInfo = obService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.SA_PLATFORM_CODE.equals(parentName)) {
                resultInfo = saService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.TP_PLATFORM_CODE.equals(parentName)) {
                resultInfo = tpService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.SGWIN_PLATFORM_CODE.equals(parentName)) {
                resultInfo = sgWinService.logout(account,parentName, ip,countryCode);
            }
            if (OpenAPIProperties.TCGWIN_PLATFORM_CODE.equals(parentName)) {
                resultInfo = tcgWinService.logout(account,parentName, ip,countryCode);
            }
            log.info("退出平台log  resultInfo:{} ",JSONObject.toJSONString(resultInfo));
            if (resultInfo == null) {
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
            }
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("退出平台log  logout  error:{}", e.getMessage());
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }
    @Override
    public Result gamelogout(String account, String ip, String countryCode) {
        try {
            List<LoginGame> loginGameList = gameCommonService.queryAllLoginGame(account);
            if(null!=loginGameList&&loginGameList.size()>0){
                for(LoginGame loginGame:loginGameList){
                    this.logout(loginGame.getAccount(),ip,loginGame.getParentName(),countryCode);
                }
                gameCommonService.deleteBatchLoginGame(loginGameList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed();
        }
        return Result.success();
    }
}
