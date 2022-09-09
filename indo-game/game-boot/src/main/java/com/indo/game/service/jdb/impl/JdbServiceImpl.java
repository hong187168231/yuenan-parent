package com.indo.game.service.jdb.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.JDBAESEncrypt;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.mapper.game.GamePlatformMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.jdb.JdbApiRequestGetTokenDto;
import com.indo.game.pojo.dto.jdb.JdbApiRequestGetTryTokenDto;
import com.indo.game.pojo.dto.jdb.JdbApiRequestParentDto;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.vo.callback.jdb.JdbApiIsGameingInfoRequestBack;
import com.indo.game.pojo.vo.callback.jdb.JdbApiIsGameingRequestBack;
import com.indo.game.pojo.vo.callback.jdb.JdbApiRequestBack;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.jdb.JdbService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * jdb电子
 *
 * @author eric
 */
@Service
public class JdbServiceImpl implements JdbService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameLogoutService gameLogoutService;
    /**
     * 登录游戏jdb电子
     * @return loginUser 用户信息
     */
    @Override
    public Result jdbGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode) {
        logger.info("jdblog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
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
//        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
//        if (null==memBaseinfo){
//            return Result.failed(loginUser.getAccount()+"用户不存在");
//        }
//        BigDecimal balance = loginUser.getBalance();
//        //验证站点棋牌余额
//        if (null==balance || BigDecimal.ZERO==balance) {
//            logger.info("站点awc余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004",MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            boolean b = true;
            if (cptOpenMember == null) {
                b = false;
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
            }
//            if(b) {
//                JdbApiIsGameingRequestBack<JdbApiIsGameingInfoRequestBack> jdbApiIsGameingRequestBack = getIsGameing(loginUser, ip);
//                if (null != jdbApiIsGameingRequestBack) {
//                    if ("0000".equals(jdbApiIsGameingRequestBack.getErr_text())) {
//                        List<JdbApiIsGameingInfoRequestBack> list = jdbApiIsGameingRequestBack.getData();
//                        if (null != list && list.size() > 0) {
//                            this.logout(loginUser, ip,countryCode);
//                        }
//                    }
//                }
//            }
            return getToken(gameParentPlatform,gamePlatform,loginUser, ip,isMobileLogin, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     *  强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode){
        JdbApiRequestParentDto jdbApiRequestParentDto = new JdbApiRequestParentDto();
        jdbApiRequestParentDto.setParent(OpenAPIProperties.JDB_AGENT);//代理账号
        jdbApiRequestParentDto.setTs(new Date().getTime());//当前系统时间
        jdbApiRequestParentDto.setAction(17);//交易号
        jdbApiRequestParentDto.setUid(account);//玩家账号

        try {
            String resultString = commonRequest(JSONObject.toJSONString(jdbApiRequestParentDto), 0, ip, "logout");
            JdbApiRequestBack jdbApiRequestBack = null;
            if (StringUtils.isNotEmpty(resultString)) {
                jdbApiRequestBack = JSONObject.parseObject(resultString, JdbApiRequestBack.class);
            }
            if (null == jdbApiRequestBack ) {
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
            }
            if("0000".equals(jdbApiRequestBack.getStatus())){
                return Result.success(jdbApiRequestBack);
            }else {
                return errorCode(jdbApiRequestBack.getStatus(),jdbApiRequestBack.getErr_text(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }

    /**
     *  取得 Token
     */
    public Result getToken(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform,LoginInfo loginUser,String ip,String isMobileLogin,String countryCode){
        JdbApiRequestGetTokenDto jdbApiRequestParentDto = new JdbApiRequestGetTokenDto();
        jdbApiRequestParentDto.setParent(OpenAPIProperties.JDB_AGENT);//代理账号
        jdbApiRequestParentDto.setTs(new Date().getTime());//当前系统时间
        jdbApiRequestParentDto.setAction(21);//交易号
        jdbApiRequestParentDto.setUid(loginUser.getAccount());//玩家账号

        jdbApiRequestParentDto.setBalance(loginUser.getBalance());// Double Y 余额
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "en";
                    break;
                case "EN":
                    lang = "en";
                    break;
                case "CN":
                    lang = "cn";
                    break;
                case "VN":
                    lang = "vn";
                    break;
                case "TH":
                    lang = "th";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
        jdbApiRequestParentDto.setLang(lang);// String(2) N 语系
//        en：英文（默认值）
//        cn：简体中文
//        th：泰文
//        vn：越南文  ※尚未支援该语系的游戏预设将以英文开启
        if(null!=gamePlatform) {
//            Live	真人视讯
//            Slots	老虎机
//            Sports	体育
//            Animal	斗鸡
//            Poker	棋牌游戏
//            Fishing	捕鱼
//            Arcade 街机
//            Lottery彩票
            GameCategory gameCategory = gameCategoryMapper.selectById(gamePlatform.getCategoryId());
            String gtype = "";
            if("Slots".equals(gameCategory.getGameType())){
                gtype = "0";
            }
            if("Fishing".equals(gameCategory.getGameType())){
                gtype = "7";
            }
            if("Lottery".equals(gameCategory.getGameType())){
                gtype = "12";
            }
            if("Poker".equals(gameCategory.getGameType())){
                gtype = "18";
            }
            jdbApiRequestParentDto.setGType(gtype);// String(2) N 游戏型态
            jdbApiRequestParentDto.setMType(gamePlatform.getPlatformCode());// String(5) N 机台类型
            jdbApiRequestParentDto.setWindowMode("2");// String(1) N
        }else {
            jdbApiRequestParentDto.setWindowMode("1");// String(1) N
        }
//        1: 使用 JDB 游戏大厅（默认值）※若未带入 gType 及 mType，则直接到游戏大 厅 ※若带入 gType 及 mType 时，直接进入游戏。
//        2: 不使用 JDB 游戏大厅※gType 及 mType 为必填字段。
        //            1 手机 APP
        // Boolean N 是否为手机 APP 进入游戏
        //        true：手机 APP
//        false：手机网页、计算机网页（默认值）
        if("1".equals(isMobileLogin)) {
            jdbApiRequestParentDto.setIsAPP(true);
        }else {
            jdbApiRequestParentDto.setIsAPP(false);
        }

//        jdbApiRequestParentDto.setLobbyURL();// String(1000) N 游戏大厅网址
//        当 windowMode 为 2 时, 此参数才会有作用
//        moreGame Integer N 0: 不显示更多游戏
//        1: 显示更多游戏（默认值）
//        jdbApiRequestParentDto.setMute();// Integer N 默认音效开关
//        0: 开启音效（默认值）
//        1: 静音
//        jdbApiRequestParentDto.setJackpotFlag();// Integer N 设定彩金开关
//        0: 打开（默认值）
//        1: 关闭
        jdbApiRequestParentDto.setIsShowDollarSign(true);// Boolean N 是否显示币别符号
//        true：显示币别符号
//        false：不显示币别符号
        try {
            String resultString = commonRequest(JSONObject.toJSONString(jdbApiRequestParentDto), Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            JdbApiRequestBack jdbApiRequestBack = null;
            if (StringUtils.isNotEmpty(resultString)) {
                jdbApiRequestBack = JSONObject.parseObject(resultString, JdbApiRequestBack.class);
            }
            if (null == jdbApiRequestBack ) {
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
            }
            if("0000".equals(jdbApiRequestBack.getStatus())){
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jdbApiRequestBack.getPath());
                return Result.success(responseData);
            }else {
                return errorCode(jdbApiRequestBack.getStatus(),jdbApiRequestBack.getErr_text(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }
    /**
     *  取得试玩 Token
     */
    public Result getTryToken(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform,LoginInfo loginUser,String ip,String isMobileLogin,String countryCode){
        JdbApiRequestGetTryTokenDto jdbApiRequestGetTryTokenDto = new JdbApiRequestGetTryTokenDto();
        jdbApiRequestGetTryTokenDto.setParent(OpenAPIProperties.JDB_AGENT);//代理账号
        jdbApiRequestGetTryTokenDto.setTs(new Date().getTime());//当前系统时间
        jdbApiRequestGetTryTokenDto.setAction(47);//交易号
        jdbApiRequestGetTryTokenDto.setUid(loginUser.getAccount());//玩家账号

        jdbApiRequestGetTryTokenDto.setLang(gameParentPlatform.getLanguageType());// String(2) N 语系
//        en：英文（默认值）
//        cn：简体中文
//        th：泰文
//        vn：越南文  ※尚未支援该语系的游戏预设将以英文开启
        if(null!=gamePlatform) {
//            Live	真人视讯
//            Slots	老虎机
//            Sports	体育
//            Animal	斗鸡
//            Poker	棋牌游戏
//            Fishing	捕鱼
//            Arcade 街机
//            Lottery彩票
            GameCategory gameCategory = gameCategoryMapper.selectById(gamePlatform.getCategoryId());
            String gtype = "";
            if("Slots".equals(gameCategory.getGameType())){
                gtype = "0";
            }
            if("Fishing".equals(gameCategory.getGameType())){
                gtype = "7";
            }
            if("Lottery".equals(gameCategory.getGameType())){
                gtype = "12";
            }
            if("Poker".equals(gameCategory.getGameType())){
                gtype = "18";
            }
            jdbApiRequestGetTryTokenDto.setGType(gtype);// String(2) N 游戏型态
            jdbApiRequestGetTryTokenDto.setMType(gamePlatform.getPlatformCode());// String(5) N 机台类型
            jdbApiRequestGetTryTokenDto.setWindowMode("2");// String(1) N
        }else {
            jdbApiRequestGetTryTokenDto.setWindowMode("1");// String(1) N
        }
//        1: 使用 JDB 游戏大厅（默认值）※若未带入 gType 及 mType，则直接到游戏大 厅 ※若带入 gType 及 mType 时，直接进入游戏。
//        2: 不使用 JDB 游戏大厅※gType 及 mType 为必填字段。
        //            1 手机 APP
        // Boolean N 是否为手机 APP 进入游戏
        //        true：手机 APP
//        false：手机网页、计算机网页（默认值）
        if("1".equals(isMobileLogin)) {
            jdbApiRequestGetTryTokenDto.setIsAPP(true);
        }else {
            jdbApiRequestGetTryTokenDto.setIsAPP(false);
        }

//        jdbApiRequestGetTryTokenDto.setLobbyURL();// String(1000) N 游戏大厅网址
//        当 windowMode 为 2 时, 此参数才会有作用
//        moreGame Integer N 0: 不显示更多游戏
//        1: 显示更多游戏（默认值）
//        jdbApiRequestGetTryTokenDto.setMute();// Integer N 默认音效开关
//        0: 开启音效（默认值）
//        1: 静音
        jdbApiRequestGetTryTokenDto.setIsShowDollarSign(true);// Boolean N 是否显示币别符号
//        true：显示币别符号
//        false：不显示币别符号
        try {
            String resultString = commonRequest(JSONObject.toJSONString(jdbApiRequestGetTryTokenDto), Integer.valueOf(loginUser.getId().intValue()), ip, "logout");

            JdbApiRequestBack jdbApiRequestBack = null;
            if (StringUtils.isNotEmpty(resultString)) {
                jdbApiRequestBack = JSONObject.parseObject(resultString, JdbApiRequestBack.class);

            }
            if (null == jdbApiRequestBack ) {
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
            }
            if("0000".equals(jdbApiRequestBack.getStatus())){
                return Result.success(jdbApiRequestBack);
            }else {
                return errorCode(jdbApiRequestBack.getStatus(),jdbApiRequestBack.getErr_text(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }

    /**
     *  查询玩家是否在游戏中
     */
    public JdbApiIsGameingRequestBack<JdbApiIsGameingInfoRequestBack> getIsGameing(LoginInfo loginUser,String ip) throws Exception {
        JdbApiRequestParentDto jdbApiRequestParentDto = new JdbApiRequestParentDto();
        jdbApiRequestParentDto.setParent(OpenAPIProperties.JDB_AGENT);//代理账号
        jdbApiRequestParentDto.setTs(new Date().getTime());//当前系统时间
        jdbApiRequestParentDto.setAction(52);//交易号
        jdbApiRequestParentDto.setUid(loginUser.getAccount());//玩家账号
        JdbApiIsGameingRequestBack<JdbApiIsGameingInfoRequestBack> jdbApiRequestBack = null;
        String resultString = commonRequest(JSONObject.toJSONString(jdbApiRequestParentDto), Integer.valueOf(loginUser.getId().intValue()), ip, "logout");

        if (StringUtils.isNotEmpty(resultString)) {
            jdbApiRequestBack = new JdbApiIsGameingRequestBack<JdbApiIsGameingInfoRequestBack>();
            jdbApiRequestBack = JSONObject.parseObject(resultString, jdbApiRequestBack.getClass());
        }
        return jdbApiRequestBack;
    }

    /**
     * 公共请求
     */
    public String commonRequest(String jsonStr, Integer userId, String ip, String type) throws Exception {
        logger.info("jdblog  commonRequest userId:{},paramsMap:{}", userId,  jsonStr);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("dc", OpenAPIProperties.JDB_DC);
        paramsMap.put("x", JDBAESEncrypt.encrypt(jsonStr, OpenAPIProperties.JDB_KEY, OpenAPIProperties.JDB_IV));
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,OpenAPIProperties.JDB_API_URL+"/apiRequest.do", paramsMap, type, userId);
        logger.info("jdblog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                userId, type, null, jsonStr, resultString);
        return resultString;
    }

    public Result  errorCode(String errorCode,String errorMessage,String countryCode){
//        0000 成功。                                                Succeed.
        switch (errorCode){
//        9001 无权访问                                              No authorized to access
            case "9001":
                return Result.failed("g000002",MessageUtils.get("g000002",countryCode));

//        9002 域为空或域的长度小于 2。                              Domain is null or the length of domain less than 2.
            case "9002":
                return Result.failed("g091060",MessageUtils.get("g091060",countryCode));

//        9003 域验证失败。                                          Failed to pass the domain validation.
            case "9003":
                return Result.failed("g091061",MessageUtils.get("g091061",countryCode));

//        9004 加密数据为空或加密数据长度等于0。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "9004":
                return Result.failed("g091062",MessageUtils.get("g091062",countryCode));

//        9005 Assertion(SAML) 未通过时间戳验证。                    Assertion(SAML) didn't pass the timestamp validation.
            case "9005":
                return Result.failed("g091063",MessageUtils.get("g091063",countryCode));

//        9006 无法从加密数据中提取 SAML 参数。                      Failed to extract the SAML parameters from the encrypted data.
            case "9006":
                return Result.failed("g091064",MessageUtils.get("g091064",countryCode));

//        9007 未知动作。                                            Unknow action.
            case "9007":
                return Result.failed("g091065",MessageUtils.get("g091065",countryCode));

//        9008 与之前相同的值。                                      The same value as before.
            case "9008":
                return Result.failed("g091066",MessageUtils.get("g091066",countryCode));

//        9009 超时。                                                Time out.
            case "9009":
                return Result.failed("g091067",MessageUtils.get("g091067",countryCode));

//        9010 读取超时。                                            Read time out.
            case "9010":
                return Result.failed("g091067",MessageUtils.get("g091067",countryCode));

//        9011 重复交易。                                            Duplicate transactions.
            case "9011":
                return Result.failed("g091038",MessageUtils.get("g091038",countryCode));

//        9012 请稍后再试。                                          Please try again later.
            case "9012":
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));

//        9013 系统维护。                                            System is maintained.
            case "9013":
                return Result.failed("g000001",MessageUtils.get("g000001",countryCode));

//        9014 检测到多个帐户登录。                                  Multiple account login detected.
            case "9014":
                return Result.failed("g100003",MessageUtils.get("g100003",countryCode));

//        9015 数据不存在。                                          Data does not exist.
            case "9015":
                return Result.failed("g091068",MessageUtils.get("g091068",countryCode));

//        9017 处理中，请稍后再试。                                  Work in process, please try again later.
            case "9017":
                return Result.failed("g091069",MessageUtils.get("g091069",countryCode));

//        8000 参数输入错误，请检查您的参数是否正确。                The parameter of input error, please check your parameter is correct or not.
            case "8000":
                return Result.failed("g000007",MessageUtils.get("g000007",countryCode));

//        8001 参数不能为空。                                        The parameter cannot be empty.
            case "8001":
                return Result.failed("g091070",MessageUtils.get("g091070",countryCode));

//        8002 参数必须为正整数。                                    The parameter must be an positive integer.
            case "8002":
                return Result.failed("g091071",MessageUtils.get("g091071",countryCode));

//        8003 参数不能为负数。                                      The parameter cannot be negative.
            case "8003":
                return Result.failed("g091072",MessageUtils.get("g091072",countryCode));

//        8005 错误 sdate 秒格式                                     wrong sdate second format
            case "8005":
                return Result.failed("g091073",MessageUtils.get("g091073",countryCode));

//        8006 时间不符。                                            Time does not meet.
            case "8006":
                return Result.failed("g091034",MessageUtils.get("g091034",countryCode));

//        8007 参数只能使用数字。                                    The parameter only can use number.
            case "8007":
                return Result.failed("g091074",MessageUtils.get("g091074",countryCode));

//        8008 找不到参数。                                          The parameter cannot be found.
            case "8008":
                return Result.failed("g091075",MessageUtils.get("g091075",countryCode));

//        8009 时间间隔超出允许范围。                                Time interval exceeds the allowable range.
            case "8009":
                return Result.failed("g000006",MessageUtils.get("g000006",countryCode));

//        8010 参数长度过长。                                        The length of parameter is too long.
            case "8010":
                return Result.failed("g091076",MessageUtils.get("g091076",countryCode));

//        7001 找不到指定的父 ID。                                   The specified parent ID cannot be found.
            case "7001":
                return Result.failed("g091077",MessageUtils.get("g091077",countryCode));

//        7002 父级被暂停。                                          Parent is suspended.
            case "7002":
                return Result.failed("g091078",MessageUtils.get("g091078",countryCode));

//        7003 父级被锁定。                                          Parent is locked.
            case "7003":
                return Result.failed("g091079",MessageUtils.get("g091079",countryCode));

//        7004 父级已关闭                                            Parent is closed
            case "7004":
                return Result.failed("g091080",MessageUtils.get("g091080",countryCode));

//        7405 您已下线！                                            You have been logged out!
            case "7405":
                return Result.failed("g091082",MessageUtils.get("g091082",countryCode));
//                7501 找不到用户 ID。                                       User ID cannot be found.
            case "7501":
                return Result.failed("g010001",MessageUtils.get("g010001",countryCode));

//        7502 用户被暂停。                                          User is suspended.
            case "":
                return Result.failed("g091081",MessageUtils.get("g091081",countryCode));

//        7503 用户被锁定。                                          User is locked.
            case "7503":
                return Result.failed("g200003",MessageUtils.get("g200003",countryCode));

//        7504 用户已关闭。                                          User is closed.
            case "7504":
                return Result.failed("g200002",MessageUtils.get("g200002",countryCode));

//        7505 用户没有在玩                                          User is not playing
            case "7505":
                return Result.failed("g091083",MessageUtils.get("g091083",countryCode));

//        7601 无效的用户 ID。 请仅使用 a-z、0-9 之间的字符          Invalid User ID. Please only use characters between a-z, 0-9
            case "7601":
                return Result.failed("g100002",MessageUtils.get("g100002",countryCode));

//        7602 帐户已存在。 请选择其他用户ID                         Account already exist. Please choose other User ID
            case "7602":
                return Result.failed("g100003",MessageUtils.get("g100003",countryCode));

//        7603 用户名无效。                                          Invalid username.
            case "7603":
                return Result.failed("g091084",MessageUtils.get("g091084",countryCode));

//        7604 密码必须至少为 6 个字符，包含 1 个字母和 1 个数字。   The password must at least 6 characters, with 1 alphabet and 1 number.
            case "7604":
                return Result.failed("g100108",MessageUtils.get("g100108",countryCode));

//        7605 无效的操作码。 请仅使用数字 2、3、4、5。              Invalid operation_code. Please only use number 2, 3, 4, 5.
            case "7605":
                return Result.failed("g091011",MessageUtils.get("g091011",countryCode));

//        6001 您的现金余额不足以提取                                Your Cash Balance not enough to withdraw
            case "6001":
                return Result.failed("g300004",MessageUtils.get("g300004",countryCode));

//        6002 用户余额为零                                          User balance is zero
            case "6002":
                return Result.failed("g091086",MessageUtils.get("g091086",countryCode));

//        6003 提取负数                                              Withdraw negative amount
            case "6003":
                return Result.failed("g091085",MessageUtils.get("g091085",countryCode));

//        6004 重复传输                                              Duplicate Transfer
            case "6004":
                return Result.failed("g091016",MessageUtils.get("g091016",countryCode));

//        6005 重复序列号。                                          Repeat serial number.
            case "6005":
                return Result.failed("g091038",MessageUtils.get("g091038",countryCode));

//        6006 您的现金余额不足。                                    Your Cash Balance not enough.
            case "6006":
                return Result.failed("g300004",MessageUtils.get("g300004",countryCode));

//        6101 无法取消。                                            Can not cancel.
            case "6101":
                return Result.failed("g091087",MessageUtils.get("g091087",countryCode));

//        6901 用户正在玩游戏，不允许转账余额                        User is playing game, and not allow transfer balance
            case "6901":
                return Result.failed("g091088",MessageUtils.get("g091088",countryCode));

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
        }
    }
}
