package com.indo.game.service.tcg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.common.util.TCGWinEncrypt;
import com.indo.game.common.util.TCGWinSHA256Encrypt;
import com.indo.game.mapper.lottery.GameTcgLotteryMapper;
import com.indo.game.pojo.dto.ae.AeApiResponseData;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.tcgwin.*;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.lottery.TcgLottery;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.tcg.TCGWinService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service
public class TCGWinServiceImpl implements TCGWinService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Resource
    private GameTcgLotteryMapper gameTcgLotteryMapper;
    @Autowired
    private GameLogoutService gameLogoutService;

    @Override
    public Result tcgwinGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("tcgwinlog  {} tcgwinGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == platformGameParent) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("0".equals(platformGameParent.getIsStart())) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(platformGameParent.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        }
        GamePlatform gamePlatform = new GamePlatform();
        if (!platform.equals(parentName)) {
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
            if (null == gamePlatform) {
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
            }
            if ("0".equals(gamePlatform.getIsStart())) {
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", MessageUtils.get("g091047",countryCode));
            }
        }
//        BigDecimal balance = loginUser.getBalance();
//        //验证站点棋牌余额
//        if (null == balance || BigDecimal.ZERO.equals(balance)) {
//            logger.info("站点tcgwin余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
//                logout(loginUser, platform, ip,countryCode);
                return  initGame(platformGameParent, gamePlatform,  cptOpenMember, isMobileLogin, countryCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }
    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin,String countryCode) {
        TcgwinApiResp tcgwinApiResp = createMember(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
        if (null == tcgwinApiResp) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (tcgwinApiResp.getStatus()==0) {
            externalService.saveCptOpenMember(cptOpenMember);
            return initGame(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, countryCode);
        } else {
            return errorCode(tcgwinApiResp.getStatus(), tcgwinApiResp.getError_desc(),countryCode);
        }
    }

    /**
     * 调用API创建账号
     */
    private TcgwinApiResp createMember(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {

        TcgwinApiRegisterPlayerReq tcgwinApiRegisterPlayerReq = new TcgwinApiRegisterPlayerReq();
        tcgwinApiRegisterPlayerReq.setMethod("cm");
        tcgwinApiRegisterPlayerReq.setUsername(cptOpenMember.getUserName());
        tcgwinApiRegisterPlayerReq.setPassword(cptOpenMember.getPassword());
        tcgwinApiRegisterPlayerReq.setCurrency(platformGameParent.getCurrencyType());
        String jsonStr = JSON.toJSONString(tcgwinApiRegisterPlayerReq);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.TCGWIN_API_URL);
        TcgwinApiResp tcgwinApiResp = null;
        try {
            logger.info("tcglog createMember创建账号请求。 apiUrl:{},params:{},user:{}", apiUrl.toString(),jsonStr,cptOpenMember);
            tcgwinApiResp = commonRequest(apiUrl.toString(), jsonStr, cptOpenMember.getUserId(), "createMember");
            logger.info("tcglog createMember创建账号返回。 aeApiResponseData:{}", JSONObject.toJSONString(tcgwinApiResp));
        } catch (Exception e) {
            logger.error("aelog aeCeateMember:{}", e);
            e.printStackTrace();
        }
        return tcgwinApiResp;
    }

    /**
     * 登录逻辑
     */
    private Result initGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                            CptOpenMember cptOpenMember, String isMobileLogin,String countryCode) {
        TcgwinApiResp tcgwinApiResp = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, countryCode);
        if (null == tcgwinApiResp) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (tcgwinApiResp.getStatus()==0) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonObject = JSON.parseObject(tcgwinApiResp.getGame_url());
            responseData.setPathUrl(jsonObject.getString("gameUrl"));
            return Result.success(responseData);
        } else {
            return errorCode(tcgwinApiResp.getStatus(), tcgwinApiResp.getError_desc(),countryCode);
        }
    }

    /**
     * @return
     */
    /**
     * 调用API登录
     */
    private TcgwinApiResp gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                                    CptOpenMember cptOpenMember, String isMobileLogin,String countryCode) {
        TcgwinApiLoginReq tcgwinApiLoginReq = new TcgwinApiLoginReq();
        tcgwinApiLoginReq.setMethod("lg");
        tcgwinApiLoginReq.setUsername(cptOpenMember.getUserName());
        int proType = Integer.valueOf(gamePlatform.getPlatformCode());
        tcgwinApiLoginReq.setProduct_type(proType);
        //1：手机 0:PC
        if(420==proType){
            if (!"1".equals(isMobileLogin)) {
                tcgwinApiLoginReq.setPlatform("WEB");
            } else {
                tcgwinApiLoginReq.setPlatform("MOBILE");
            }
        }else if(460==proType) {
            tcgwinApiLoginReq.setPlatform("MOBILE");
        }else {
            if (!"1".equals(isMobileLogin)) {
                tcgwinApiLoginReq.setPlatform("html5-desktop");
            } else {
                tcgwinApiLoginReq.setPlatform("html5");
            }
        }
        tcgwinApiLoginReq.setGame_mode("1");
        tcgwinApiLoginReq.setGame_code("Lobby");
        if(420==proType){
            tcgwinApiLoginReq.setView("Lobby");
        }else {
            tcgwinApiLoginReq.setView("Lobby Page");
        }
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "EN";
                    break;
                case "EN":
                    lang = "EN";
                    break;
                case "CN":
                    lang = "CN";
                    break;
                case "VN":
                    lang = "VI";
                    break;
                case "TW":
                    lang = "TW";
                    break;
                case "TH":
                    lang = "TH";
                    break;
                case "ID":
                    lang = "ID";
                    break;
                case "KM":
                    lang = "KM";
                    break;
                case "KR":
                    lang = "KO";
                    break;
                case "JP":
                    lang = "JA";
                    break;
                default:
                    lang = platformGameParent.getLanguageType();
                    break;
            }
        }else{
            lang = platformGameParent.getLanguageType();
        }
        tcgwinApiLoginReq.setLanguage(lang);
        if(2==proType||384==proType||420==proType){
            List list = new ArrayList();
            LambdaQueryWrapper<TcgLottery> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TcgLottery::getGame_prod_type, proType);
            List<TcgLottery> tcgLotteryList = gameTcgLotteryMapper.selectList(wrapper);
            for(TcgLottery tcgLottery:tcgLotteryList) {
                if (2 == proType) {
                    TcgLottery lottery = new TcgLottery();
                    BeanUtils.copyProperties(tcgLottery, lottery);
                    list.add(lottery);
                } else if (384 == proType) {
                    TcgLotterySEA tcgLotterySEA = new TcgLotterySEA();
                    BeanUtils.copyProperties(tcgLottery, tcgLotterySEA);
                    list.add(tcgLotterySEA);
                } else if (420 == proType) {
                    TcgLotteryVN tcgLotteryVN = new TcgLotteryVN();
                    BeanUtils.copyProperties(tcgLottery, tcgLotteryVN);
                    list.add(tcgLotteryVN);
                }
            }
            tcgwinApiLoginReq.setSeries(list);
        }

        String jsonStr = JSON.toJSONString(tcgwinApiLoginReq);
        TcgwinApiResp tcgwinApiResp = null;
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.TCGWIN_API_URL);
            logger.info("tcgwin  gameLogin登录请求apiUrl:{}, params:{}, user:{}", apiUrl, jsonStr, cptOpenMember);
            tcgwinApiResp = commonRequest(apiUrl.toString(), jsonStr, cptOpenMember.getUserId(), "gameLogin");
            logger.info("tcgwin  gameLogin登录返回resultString:{}", JSON.toJSONString(tcgwinApiResp));
        } catch (Exception e) {
            logger.error("aelog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return tcgwinApiResp;
    }



    /**
     * 强迫登出玩家
     */
    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        try {
            TcgwinApiOutLottoMemberReq tcgwinApiOutLottoMemberReq = new TcgwinApiOutLottoMemberReq();
            tcgwinApiOutLottoMemberReq.setMethod("kom");
            tcgwinApiOutLottoMemberReq.setUsername(account);

            String jsonStr = JSON.toJSONString(tcgwinApiOutLottoMemberReq);
            TcgwinApiResp tcgwinApiResp = null;
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.TCGWIN_API_URL);
            logger.info("tcgwin  logout注销请求apiUrl:{}, params:{}, user:{}", apiUrl, jsonStr, account);
            tcgwinApiResp = commonRequest(apiUrl.toString(), jsonStr, 0, "logout");
            logger.info("tcgwin  logout注销返回resultString:{}", JSON.toJSONString(tcgwinApiResp));
            if (null == tcgwinApiResp) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if(tcgwinApiResp.getStatus()==0){
                return Result.success();
            }else {
                return this.errorCode(tcgwinApiResp.getStatus(),tcgwinApiResp.getError_desc(),countryCode);
            }
        } catch (Exception e) {
            logger.error("BLlog  BLlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
    }
    /**
     * 呼叫天成接口
     * @param json 参数
     * @return 结果字串
     */
    protected TcgwinApiResp commonRequest(String apiUrl,String json, Integer userId, String type){
        TcgwinApiResp tcgwinApiResp = null;
        try {
            // 参数加密
            String encryptedParams = TCGWinEncrypt.encryptDes(json,OpenAPIProperties.TCGWIN_DES_KEY);

            //签名档加密
            String sign = TCGWinSHA256Encrypt.encryptDes(encryptedParams+OpenAPIProperties.TCGWIN_SHA256_KEY);

            //组连接字串
            String data = "merchant_code="+ URLEncoder.encode(OpenAPIProperties.TCGWIN_MERCHANT_CODE,"UTF-8")
                    + "&params="+URLEncoder.encode(encryptedParams,"UTF-8")
                    + "&sign="+URLEncoder.encode(sign,"UTF-8");
            Map<String, String> paramsMap = new HashMap<>();
            logger.info("tcgwin  请求commonRequest:{}", apiUrl+"?"+data);
            String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                    apiUrl+"?"+data, paramsMap, type, userId);
            //传送
            if (StringUtils.isNotEmpty(resultString)) {
                tcgwinApiResp = JSONObject.parseObject(resultString, TcgwinApiResp.class);
                return tcgwinApiResp;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tcgwinApiResp;
    }
    public Result errorCode(int errorCode, String errorMessage,String countryCode) {
        switch (errorCode) {
            case 1://	Unknown system error, please contact TCG customer support	未知的系统错误，请联系TCG客服
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
            case 2://	Missing required parameter	缺少必需的参数
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            case 3://	Method not supported for the this product type	此产品类型不支持此方法
                return Result.failed("g000002", MessageUtils.get("g000002",countryCode));
            case 4://	Merchant is not allowed for this product type	商家不允许使用此产品类型
                return Result.failed("g000002", MessageUtils.get("g000002",countryCode));
            case 5://	Merchant not found	找不到商家
                return Result.failed("g000002", MessageUtils.get("g000002",countryCode));
            case 6://	Invalid parameters, Failed to decrypt the parameters	参数无效，无法解密参数。
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            case 7://	Invalid signature	签名无效
                return Result.failed("g091161", MessageUtils.get("g091161",countryCode));
            case 8://	Unsupported currency	不支持的货币
                return Result.failed("g100001", MessageUtils.get("g100001",countryCode));
            case 9://	Invalid Account type	帐户类型无效
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
            case 10://	Invalid product type	产品类型无效
                return Result.failed("g091162", MessageUtils.get("g091162",countryCode));
            case 11://	Insufficient balance to fund out / withdraw	提现余额不足
                return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
            case 12://	Transaction already exists.	交易序号已經存在
                return Result.failed("g091038", MessageUtils.get("g091038",countryCode));
            case 13://	Invalid game code	游戏代码无效
                return Result.failed("g091033", MessageUtils.get("g091033",countryCode));
            case 15://	User Does Not Exists	用户不存在
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            case 16://	Insufficient merchant credit to fund in	信用额度不足
                return Result.failed("g091163", MessageUtils.get("g091163",countryCode));
            case 18://	Trial mode is not supported for this game code	此游戏代码不支持试用模式
                return Result.failed("g091164", MessageUtils.get("g091164",countryCode));
            case 19://	Batch not ready	批次理未准备好
                return Result.failed("g091165", MessageUtils.get("g091165",countryCode));
            case 21://	Method not found	找不到方法
                return Result.failed("g091166", MessageUtils.get("g091166",countryCode));
            case 22://	Parameter Validation Failed	参数验证失败
                return Result.failed("g091167", MessageUtils.get("g091167",countryCode));
            case 23://	API is busy	API 繁忙
                return Result.failed("g000005", MessageUtils.get("g000005",countryCode));
            case 24://	Transaction not found	未找到此交易
                return Result.failed("g091017", MessageUtils.get("g091017",countryCode));
            case 25://	Reservation already process	预约已经完成
                return Result.failed("g091168", MessageUtils.get("g091168",countryCode));
            case 26://	Decimal not supported	Decimal 格式未支持
                return Result.failed("g091118", MessageUtils.get("g091118",countryCode));
            case 27://	API is under maintenance	接口正在维护中
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
            case 28://	API is prohibited for this merchant	此商户禁止使用此接口
                return Result.failed("g000002", MessageUtils.get("g000002",countryCode));
            case 30://	Product is under maintenance	产品正在维护中
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
            //        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
    public static void main(String[] args) throws Exception {
//        TcgwinApiResp tcgwinApiResp = new TcgwinApiResp();
//        tcgwinApiResp.setError_desc("888");
//        TcgwinApiRegisterPlayerReq registerPlayerReq = new TcgwinApiRegisterPlayerReq();
//        registerPlayerReq.setPassword("test");
//        List<TcgwinApiRegisterPlayerReq> list = new ArrayList<>();
//        list.add(registerPlayerReq);
//        tcgwinApiResp.setSeries(list);
//        System.out.println(JSON.toJSONString(tcgwinApiResp));

    }
}
