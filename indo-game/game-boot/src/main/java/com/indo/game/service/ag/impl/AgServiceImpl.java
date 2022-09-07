package com.indo.game.service.ag.impl;

import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.mapper.game.GamePlatformMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.common.util.AGEncrypt;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.service.ag.AgService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

;

/**
 * ag ae真人 游戏业务类
 *
 * @author eric
 */
@Service
public class AgServiceImpl implements AgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    /**
     * 登录游戏AG
     * @return loginUser 用户信息
     */
    @Override
    public Result agGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode) {
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", MessageUtils.get("g091047",countryCode));
        }
        //初次判断站点棋牌余额是否够该用户
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
        if (null==memBaseinfo){
            return Result.failed("g010001",MessageUtils.get("g010001",countryCode));
        }
//        BigDecimal balance = memBaseinfo.getBalance();
//        //验证站点棋牌余额
//        if (null==balance || BigDecimal.ZERO==balance) {
//            logger.info("站点ag余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004",MessageUtils.get("g300004",countryCode));
//        }

        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin, countryCode);
            } else {
                this.logout(loginUser,ip, countryCode);
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                //登录
                return initGame(gameParentPlatform,gamePlatform, ip, cptOpenMember, isMobileLogin, countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip,String countryCode){
        Map<String, String> trr = new HashMap<>();
        trr.put("userIds", loginUser.getAccount());

        try {
//            String paramStr = "cagent="+OpenAPIProperties.AG_CAGENT+"/\\\\\\\\/loginname="+cptOpenMember.getUserName()+
//                    "/\\\\\\\\/method=lg/\\\\\\\\/actype=0/\\\\\\\\/" +
//                    "password="+cptOpenMember.getUserName()+"/\\\\\\\\/oddtype="+gamePlatform.getBetLimit()+"/\\\\\\\\/cur="+gameParentPlatform.getCurrencyType();
//            logger.info("aglog  createMember创建玩家加密前 paramStr:{}", paramStr);
//            String param = AGEncrypt.encryptDes(paramStr,OpenAPIProperties.AG_API_KEY);
//            param += "";
//            logger.info("aglog  createMember创建玩家加密后 param:{},url:{} ", paramStr,OpenAPIProperties.AG_API_URL+"/doBusiness.do");
//            String resultString = commonRequest(trr, OpenAPIProperties.AG_API_URL+"/wallet/logout",);
//            if (null == resultString || "".equals(resultString) ) {
//                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
//            }
//            Document doc = commonXml(resultString);
//            String errorCode = doc.getElementsByTagName("info").item(0).getTextContent();
//            String msg = doc.getElementsByTagName("msg").item(0).getTextContent();
//            if (StringUtils.isNotEmpty(errorCode) && errorCode.equals("0")) {
//                return Result.success();
//            }else {
//                return errorCode(errorCode,msg);
//            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin,String countryCode) throws Exception {
        String resultString = game(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin, countryCode);
        if (null == resultString || "".equals(resultString) ) {
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
        Document doc = commonXml(resultString);
        String errorCode = doc.getElementsByTagName("info").item(0).getTextContent();
        String msg = doc.getElementsByTagName("msg").item(0).getTextContent();
        if (StringUtils.isNotEmpty(errorCode) && errorCode.equals("0")) {
            return Result.success();
        }else {
            return errorCode(errorCode,msg, countryCode);
        }
    }
    /**
     * 创建玩家
     */
    private Result createMemberGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin, String countryCode) throws Exception {
        String resultString = createMember(gameParentPlatform,gamePlatform, ip, cptOpenMember);
        if (null == resultString || "".equals(resultString) ) {
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
        Document doc = commonXml(resultString);
        String errorCode = doc.getElementsByTagName("info").item(0).getTextContent();
        String msg = doc.getElementsByTagName("msg").item(0).getTextContent();
        if (StringUtils.isNotEmpty(errorCode) && errorCode.equals("0")) {
            return Result.success();
        }else {
            return errorCode(errorCode,msg, countryCode);
        }
    }

    /***
     * 创建玩家
     * @param gamePlatform
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public String createMember(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember) {
        try {
            String paramStr = "cagent="+OpenAPIProperties.AG_CAGENT+"/\\\\\\\\/loginname="+cptOpenMember.getUserName()+
                    "/\\\\\\\\/method=lg/\\\\\\\\/actype=0/\\\\\\\\/" +
                    "password="+cptOpenMember.getPassword()+"/\\\\\\\\/oddtype="+gamePlatform.getBetLimit()+"/\\\\\\\\/cur="+gameParentPlatform.getCurrencyType();
            logger.info("aglog  createMember创建玩家加密前 paramStr:{}", paramStr);
            String param = AGEncrypt.encryptDes(paramStr,OpenAPIProperties.AG_API_KEY);
            param += "";
            logger.info("aglog  createMember创建玩家加密后 param:{},url:{} ", param,OpenAPIProperties.AG_API_URL+"/doBusiness.do");
            return commonRequest(param, OpenAPIProperties.AG_API_URL+"/doBusiness.do");
        } catch (Exception e) {
            logger.error("aglog game error {} ", e);
            return null;
        }
    }
//    建立遊戏 Session
public String createSession(BigDecimal balance, CptOpenMember cptOpenMember) {

    try {
        String paramStr = "productid=B17&username="+cptOpenMember.getUserName()+"&session_token="+cptOpenMember.getPassword()+"&credit="+balance;
        logger.info("aglog  createSession建立遊戏 param:{},url:{} ", paramStr,OpenAPIProperties.AG_SESSION_URL+"/resource/player-tickets.ucs");
        return commonRequest(paramStr, OpenAPIProperties.AG_SESSION_URL+"/resource/player-tickets.ucs");
    } catch (Exception e) {
        logger.error("aglog game error {} ", e);
        return null;
    }
}

    /***
     * 登录请求
     * @param gamePlatform
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public String game(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin,String countryCode) {
        try {
            //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            String lang = "";
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        lang = "3";
                    case "EN":
                        lang = "3";
                    case "CN":
                        lang = "1";
                    case "VN":
                        lang = "4";
                    case "TW":
                        lang = "2";
                    default:
                        lang = gameParentPlatform.getLanguageType();
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            String paramStr = "cagent="+OpenAPIProperties.AG_CAGENT+"/\\\\\\\\/loginname="+cptOpenMember.getUserName()+
                    "/\\\\\\\\/method=lg/\\\\\\\\/actype=0/\\\\\\\\/" +
                    "password="+cptOpenMember.getUserName()+"/\\\\\\\\/dm=" +
                    "/\\\\\\\\/sid="+OpenAPIProperties.AG_SID_KEY+"/\\\\\\\\/lang="+lang+
                    "/\\\\\\\\/gameType=0/\\\\\\\\/oddtype="+gamePlatform.getBetLimit()+"/\\\\\\\\/cur="+gameParentPlatform.getCurrencyType();
            logger.info("aglog  createMember创建玩家加密前 paramStr:{}", paramStr);
            String param = AGEncrypt.encryptDes(paramStr,OpenAPIProperties.AG_API_KEY);
            param += "&key="+OpenAPIProperties.AG_API_KEY;
            logger.info("aglog  createMember创建玩家加密后 param:{},url:{} ", paramStr,OpenAPIProperties.AG_LOGIN_URL+"/forwardGame.do");

            return commonRequest(param, OpenAPIProperties.AG_LOGIN_URL+"/forwardGame.do");
        } catch (Exception e) {
            logger.error("aglog game error {} ", e);
            return null;
        }
    }



    /**
     * 公共请求
     */
    public String commonRequest(String param, String url) throws Exception {
        return GameUtil.sendGet(url, param);
    }

    public Document commonXml(String repXML) {
        Document doc = null;
        try {
            StringReader sr = new StringReader(repXML);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            return doc;
        } catch (Exception e) {
            logger.error("djlog commonXml error {}", e);
            return doc;
        }
    }

    public Result  errorCode(String errorCode,String errorMessage,String countryCode){
        if ("key_error".equals(errorCode)){//Key值(参考3.1.1)为错误
            return Result.failed("g091061",MessageUtils.get("g091061",countryCode));
        }else if ("network_error ".equals(errorCode)){//网络问题导致资料遗失
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }else if ("account_add_fail".equals(errorCode)){//创建新账号失败, 可能是密码不正确或账号已存在
            return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
        }if ("key_error".equals(errorCode)){//Key值(参考3.1.1)为错误
            return Result.failed("g091061",MessageUtils.get("g091061",countryCode));
        }if ("1000".equals(errorCode)){//缺少必要的参数.
            return Result.failed("g000007",MessageUtils.get("g000007",countryCode));
        }if ("1016".equals(errorCode)){//玩家账户是禁用状态的
            return Result.failed("g200003",MessageUtils.get("g200003",countryCode));
        }if ("2002".equals(errorCode)){//该玩家账户是不存在的
            return Result.failed("g010001",MessageUtils.get("g010001",countryCode));
        }if ("2003".equals(errorCode)){//该产品编码不支持单一钱包功能
            return Result.failed("g000002",MessageUtils.get("g000002",countryCode));
        }if ("9999".equals(errorCode)){//发送请求后, 服务器出现错误.
            return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
        }else {//其他错误, 请联络我们，参看msg错误描述信息
            return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
        }

    }
}
