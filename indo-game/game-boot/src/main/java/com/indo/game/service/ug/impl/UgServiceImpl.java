package com.indo.game.service.ug.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.ug.*;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.ug.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ug.UgService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * awc ae真人 游戏业务类
 *
 * @author eric
 */
@Service
public class UgServiceImpl implements UgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    GameTypeMapper gameTypeMapper;
    @Autowired
    GameCategoryMapper gameCategoryMapper;

    @Autowired
    private TxnsMapper txnsMapper;
    @Autowired
    private GameLogoutService gameLogoutService;
    /**
     * 登录游戏
     * @return loginUser 用户信息
     */
    @Override
    public Result ugGame(LoginInfo loginUser, String ip,String platform,String WebType,String parentName,String countryCode) {
        logger.info("uglog ugGame loginUser:{}, ip:{}, platform:{}, WebType:{}, parentName:{}", loginUser, platform, WebType, parentName);
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
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
//        BigDecimal balance = loginUser.getBalance();
//        //验证站点棋牌余额
//        if (null==balance || BigDecimal.ZERO==balance) {
//            logger.info("站点ug余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004",MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return restrictedPlayer(gameParentPlatform,loginUser,gamePlatform, ip, cptOpenMember,WebType,countryCode);
            } else {
//                this.logout(loginUser,ip,countryCode);
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                //登录
                return initGame(gameParentPlatform,loginUser, gamePlatform, ip, WebType,countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 注册会员
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(GameParentPlatform gameParentPlatform,LoginInfo loginUser,GamePlatform gamePlatform, String ip,CptOpenMember cptOpenMember,String WebType,String countryCode) {
        logger.info("uglog restrictedPlayer {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            UgRegisterPlayerJsonDTO ugRegisterPlayerJsonDTO = new UgRegisterPlayerJsonDTO();
            ugRegisterPlayerJsonDTO.setUserId(loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            ugRegisterPlayerJsonDTO.setLoginName(null==loginUser.getNickName()?"":loginUser.getNickName());//昵称,长度需要小于 50
            ugRegisterPlayerJsonDTO.setCurrencyId(Integer.valueOf(gameParentPlatform.getCurrencyType()));//货币代码
            if(null!=OpenAPIProperties.UG_AGENT&&!"".equals(OpenAPIProperties.UG_AGENT)){
                ugRegisterPlayerJsonDTO.setAgentId(Integer.valueOf(OpenAPIProperties.UG_AGENT));//代理编号
            }
            ugRegisterPlayerJsonDTO.setApiKey(OpenAPIProperties.UG_API_KEY);
            ugRegisterPlayerJsonDTO.setOperatorId(OpenAPIProperties.UG_COMPANY_KEY);
            logger.info("UG体育注册会员restrictedPlayer输入 apiUrl:{}, params:{}, userId:{}, ip:{}", OpenAPIProperties.UG_API_URL+"/api/single/register", JSONObject.toJSONString(ugRegisterPlayerJsonDTO), loginUser.getId(), ip);
            UgApiResponseData ugApiResponse = commonRequestPost(ugRegisterPlayerJsonDTO, OpenAPIProperties.UG_API_URL+"/api/single/register", loginUser.getId().intValue(), ip, "restrictedPlayer");
            logger.info("UG体育注册会员返回参数: ugApiResponse:{}"+ugApiResponse);
            if(null!=ugApiResponse&&"000000".equals(ugApiResponse.getCode())){
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(gameParentPlatform,loginUser,gamePlatform, ip,WebType,countryCode);
            }else if(null==ugApiResponse){
                return Result.failed();
            }else {
                {
                    return this.errorCode(ugApiResponse.getCode(),ugApiResponse.getMsg(),countryCode);
                }
            }
        } catch (Exception e) {
            logger.error("uglog restrictedPlayer game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,LoginInfo loginUser,GamePlatform gamePlatform, String ip,String WebType,String countryCode) throws Exception {
        logger.info("uglog initGame Login {} initGame ugGame account:{}, ugCodeId:{},ip:{}", loginUser.getId(), loginUser.getNickName(),ip);
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
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
                    lang = "zh_cn";
                    break;
                case "VN":
                    lang = "vi";
                    break;
                case "TW":
                    lang = "zh_TW";
                    break;
                case "TH":
                    lang = "th";
                    break;
                case "ID":
                    lang = "id";
                    break;
                case "MY":
                    lang = "ms_MY";
                    break;
                case "KR":
                    lang = "ko_KR";
                    break;
                case "JP":
                    lang = "ja_JP";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
//        try {
//            operatorId	string	16	Y	商户代码
//            userId	string	50	Y	玩家帐号
//            returnUrl	string	200	Y	登出后重定向网址
//            oddsExpression	string	10	N	赔率样式代码
//            如果没有输入，或是输入的值不在系统设定之内，会使用系统设定的第一个值
//            language	string	5	N	语言，预设值：en
//            webType	string	10	N	入口类型，预设值：mobile
//            theme	string	6	N	版面，预设值：style
//            sportId	number		N	偏好运动类型，预设值：1 (足球)，支援设定的运动有：1 (足球), 2 (篮球), 7 (网球), 11 (板球)
            String urlLogin = OpenAPIProperties.UG_LOGIN_URL+"/auth/single?operatorId="+OpenAPIProperties.UG_COMPANY_KEY+"&userId="+loginUser.getAccount()
                    +"&returnUrl="+OpenAPIProperties.UG_RETURN_URL+"&oddsExpression=decimal"
                    +"&language="+lang
                    +"&webType="+WebType+"&theme=style&sportId=1";
            logger.info("UG体育登录initGame输入 urlLogin:{},  loginUser:{}, ip:{}", urlLogin, loginUser, ip);

//            UgApiResponseData ugApiResponse = commonRequestGet(param, OpenAPIProperties.UG_LOGIN_URL+"/auth/single", loginUser.getId().intValue(), ip, "Login");
//            logger.info("UG体育登录返回参数: ugApiResponse:{}"+ugApiResponse);
//            if(null!=ugApiResponse&&"000000".equals(ugApiResponse.getCode())){
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(urlLogin);
                return Result.success(responseData);
//            }else if(null==ugApiResponse){
//                return Result.failed();
//            }else {
//                {
//                    return this.errorCode(ugApiResponse.getCode(),ugApiResponse.getMsg());
//                }
//            }
//        } catch (Exception e) {
//            logger.error("uglog initGame Login game error {} ", e);
//            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
//        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode){
        UgLogoutJsonDTO ugLogoutJsonDTO = new UgLogoutJsonDTO();
        ugLogoutJsonDTO.setUserId(account);
        ugLogoutJsonDTO.setApiKey(OpenAPIProperties.UG_API_KEY);
        ugLogoutJsonDTO.setOperatorId(OpenAPIProperties.UG_COMPANY_KEY);
        logger.info("UG体育登出玩家输入 apiUrl:{}, params:{}, userId:{}, ip:{}", OpenAPIProperties.UG_API_URL+"/api/single/logout", JSONObject.toJSONString(ugLogoutJsonDTO), 0, ip);
        UgApiResponseData ugApiResponse = null;
        try {
            ugApiResponse = commonRequestPost(ugLogoutJsonDTO, OpenAPIProperties.UG_API_URL+"/api/single/logout", 0, ip, "logout");
            logger.info("UG体育登出玩家返回参数: ugApiResponse:{}"+ugApiResponse);
            if(null!=ugApiResponse&&"000000".equals(ugApiResponse.getCode())){
                return Result.success(ugApiResponse);
            }else if(null==ugApiResponse){
                return Result.failed();
            }else {
                {
                    return this.errorCode(ugApiResponse.getCode(),ugApiResponse.getMsg(),countryCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("uglog logout {} ", e);
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }

    /**
     * 公共请求
     */
    public UgApiResponseData commonRequestGet(String param, String url, Integer userId, String ip, String type) throws Exception {

        UgApiResponseData ugApiResponse = null;
        String resultString = GameUtil.sendGet(url, param);
        logger.info("ug_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            ugApiResponse = JSONObject.parseObject(resultString, UgApiResponseData.class);
            logger.info("uglog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, ugApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, JSONObject.toJSONString(param), resultString, JSONObject.toJSONString(ugApiResponse));
        }
        return ugApiResponse;
    }
    public UgApiResponseData commonRequestPost(Object object, String url, Integer userId, String ip, String type) throws Exception {

        UgApiResponseData ugApiResponse = null;
        logger.info("uglog {} commonRequest ,url:{},paramsMap:{}", userId, url, JSONObject.toJSONString(object));
//        Map<String, String> trr = new HashMap<>();
//        trr.put("MemberAccount", "swuserid");
//        trr.put("CompanyKey", "y6RbXQyRr4");
//        trr.put("APIPassword", "RTJdTw36imErhpDm7p2ePb3Da3h6WT3S");
//        logger.info("ug_api_request:"+JSONObject.toJSONString(object));
//        String resultString1 = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, trr, type, userId);
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, JSONObject.toJSONString(object), type, userId);
        logger.info("ug_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            ugApiResponse = JSONObject.parseObject(resultString, UgApiResponseData.class);
        }
        return ugApiResponse;
    }

    @Override
    public void ugPullOrder(){

        UgApiTasksResponseData ugApiResponse = null;
        UgTasksJsonDTO ugTasksJsonDTO = new UgTasksJsonDTO();
        String sortNo = txnsMapper.getMaxSortNo(OpenAPIProperties.UG_PLATFORM_CODE);
        ugTasksJsonDTO.setSortNo(Long.valueOf(null!=sortNo&&!"".equals(sortNo)?sortNo:"0"));//排序编号 返回大于该排序编号的注单
        try {
            ugApiResponse = commonTasksRequest(ugTasksJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/GetBetSheetBySort",  "ugPullOrder");
            if("000000".equals(ugApiResponse.getErrorCode())){
                GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.UG_PLATFORM_CODE);
                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.UG_PLATFORM_CODE,OpenAPIProperties.UG_PLATFORM_CODE);
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                List<SortBetDto> subBetDtoList = ugApiResponse.getData();
                for (SortBetDto sortBetDto:subBetDtoList){
                    Txns txns = new Txns();
                    txns.setPlatformTxId(sortBetDto.getBetID());// string 是 注单编号
                    txns.setUserId(sortBetDto.getAccount());//  string 是 会员帐号
                    txns.setWinningAmount(sortBetDto.getPayout());//  Decimal 是 赔付金额
                    txns.setBetAmount(sortBetDto.getBetAmount());//  Decimal 是 下注金额
                    txns.setRealBetAmount(sortBetDto.getDeductAmount());// Decimal  是 扣款金额
                    txns.setWinAmount(sortBetDto.getAllWin());// Decimal  是 预计全赢金额
                    txns.setTurnover(sortBetDto.getTurnover());// Decimal  是 有效投注金额(打码量)
                    txns.setOdds(sortBetDto.getBetOdds());//  Decimal 是 投注赔率
                    txns.setRealWinAmount(sortBetDto.getWin());// Decimal  是 实际输赢
                    txns.setOddsType(sortBetDto.getOddsStyle());//  string 是 赔率样式
                    txns.setBetTime(sortBetDto.getBetDate());// Datetime 是 投注时间

//                    0 等待中
//                    1 已接受
//                    2 已结算
//                    3 已取消
//                    4 已拒绝
//  int 是    注单状态 见注单状态代码(Status) 如果 Status 为 1,则不必关心 Result 值
                    if (0==sortBetDto.getStatus()){
                        txns.setMethod("Waiting");
                        txns.setStatus("Running");
                    }
                    if (1==sortBetDto.getStatus()){
                        txns.setMethod("Place Bet");
                        txns.setStatus("Running");
                    }
                    if (2==sortBetDto.getStatus()){
                        txns.setMethod("Settle");
                        txns.setStatus("Running");
                    }
                    if (3==sortBetDto.getStatus()){
                        txns.setMethod("Cancel Bet");
                        txns.setStatus("Running");
                    }
                    if (4==sortBetDto.getStatus()){
                        txns.setMethod("Void Bet");
                        txns.setStatus("Running");
                    }
//                    0 Draw 和
//                    1 Win 赢
//                    2 Lose 输
//                    3 Win Half 赢一半
//                    4 Lose Half 输一半
                    txns.setResultType(sortBetDto.getResult());//  int 是 注单输赢结果 见注单结果代码(Result)

                    txns.setTxTime(sortBetDto.getReportDate());//  Datetime 是 注单报表时间
                    txns.setBetIp(sortBetDto.getBetIP());//  string 是 投注 IP
                    txns.setUpdateTime(sortBetDto.getUpdateTime());//  Datetime 是 注单修改时间
                    txns.setAgentId(String.valueOf(sortBetDto.getAgentID()));//  long 是 代理编号
                    txns.setGroupComm(sortBetDto.getGroupComm());//  string 是 组别佣金代码
                    txns.setMpId(sortBetDto.getMpID());//  int 是 混合过关类型 ID
                    txns.setCurrency(sortBetDto.getCurrency());//  string 是 货币代码
//                    1 PC
//                    2 Wap
//                    4 Smart
                    txns.setBetWay(sortBetDto.getBetWay());//  int 是 投注方式
                    txns.setSortNo(String.valueOf(sortBetDto.getSortNo()));//  long 是 注单排序值

                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    txns.setCreateTime(dateStr);
                    txns.setPlatform(gamePlatform.getPlatformCode());
                    txns.setPlatformCnName(gamePlatform.getPlatformCnName());
                    txns.setPlatformEnName(gamePlatform.getPlatformEnName());
                    txns.setCategoryId(gameCategory.getId());
                    txns.setCategoryName(gameCategory.getGameName());
                    txnsMapper.insert(txns);
                    List<SubBetDto> subBets = sortBetDto.getSubBets();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("ugPullOrder:",e);
        }
    }

    @Override
    public void ugPullOrderByBetID(){

        UgApiTasksResponseData ugApiResponse = null;
        UgTasksJsonBetIdDTO ugTasksJsonBetIdDTO = new UgTasksJsonBetIdDTO();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Waiting").or().eq(Txns::getMethod, "Cancel Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.UG_PLATFORM_CODE);
        List<Txns> txnsList = txnsMapper.selectList(wrapper);
        for (Txns txns:txnsList) {
        ugTasksJsonBetIdDTO.setBetID(txns.getPlatformTxId());
            try {
                ugApiResponse = commonTasksRequest(ugTasksJsonBetIdDTO, OpenAPIProperties.UG_API_URL + "/SportApi/GetBetSheetByBetID", "ugPullOrderByBetID");
                if ("000000".equals(ugApiResponse.getErrorCode())) {
                    GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(OpenAPIProperties.UG_PLATFORM_CODE);
                    GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                    List<SortBetDto> subBetDtoList = ugApiResponse.getData();
                    for (SortBetDto sortBetDto : subBetDtoList) {
                        txns.setPlatformTxId(sortBetDto.getBetID());// string 是 注单编号
                        txns.setUserId(sortBetDto.getAccount());//  string 是 会员帐号
                        txns.setWinningAmount(sortBetDto.getPayout());//  Decimal 是 赔付金额
                        txns.setBetAmount(sortBetDto.getBetAmount());//  Decimal 是 下注金额
                        txns.setRealBetAmount(sortBetDto.getDeductAmount());// Decimal  是 扣款金额
                        txns.setWinAmount(sortBetDto.getAllWin());// Decimal  是 预计全赢金额
                        txns.setTurnover(sortBetDto.getTurnover());// Decimal  是 有效投注金额(打码量)
                        txns.setOdds(sortBetDto.getBetOdds());//  Decimal 是 投注赔率
                        txns.setRealWinAmount(sortBetDto.getWin());// Decimal  是 实际输赢
                        txns.setOddsType(sortBetDto.getOddsStyle());//  string 是 赔率样式
                        txns.setBetTime(sortBetDto.getBetDate());// Datetime 是 投注时间

//                    0 等待中
//                    1 已接受
//                    2 已结算
//                    3 已取消
//                    4 已拒绝
//  int 是    注单状态 见注单状态代码(Status) 如果 Status 为 1,则不必关心 Result 值
                        if (0 == sortBetDto.getStatus()) {
                            txns.setMethod("Waiting");
                            txns.setStatus("Running");
                        }
                        if (1 == sortBetDto.getStatus()) {
                            txns.setMethod("Place Bet");
                            txns.setStatus("Running");
                        }
                        if (2 == sortBetDto.getStatus()) {
                            txns.setMethod("Settle");
                            txns.setStatus("Running");
                        }
                        if (3 == sortBetDto.getStatus()) {
                            txns.setMethod("Cancel Bet");
                            txns.setStatus("Running");
                        }
                        if (4 == sortBetDto.getStatus()) {
                            txns.setMethod("Void Bet");
                            txns.setStatus("Running");
                        }
//                    0 Draw 和
//                    1 Win 赢
//                    2 Lose 输
//                    3 Win Half 赢一半
//                    4 Lose Half 输一半
                        txns.setResultType(sortBetDto.getResult());//  int 是 注单输赢结果 见注单结果代码(Result)

                        txns.setTxTime(sortBetDto.getReportDate());//  Datetime 是 注单报表时间
                        txns.setBetIp(sortBetDto.getBetIP());//  string 是 投注 IP
                        txns.setUpdateTime(sortBetDto.getUpdateTime());//  Datetime 是 注单修改时间
                        txns.setAgentId(String.valueOf(sortBetDto.getAgentID()));//  long 是 代理编号
                        txns.setGroupComm(sortBetDto.getGroupComm());//  string 是 组别佣金代码
                        txns.setMpId(sortBetDto.getMpID());//  int 是 混合过关类型 ID
                        txns.setCurrency(sortBetDto.getCurrency());//  string 是 货币代码
//                    1 PC
//                    2 Wap
//                    4 Smart
                        txns.setBetWay(sortBetDto.getBetWay());//  int 是 投注方式
                        txns.setSortNo(String.valueOf(sortBetDto.getSortNo()));//  long 是 注单排序值

//                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
//
//                    txns.setCreateTime(dateStr);
                        txns.setPlatformCnName(gamePlatform.getPlatformCnName());
                        txns.setPlatformEnName(gamePlatform.getPlatformEnName());
                        txns.setCategoryId(gameCategory.getId());
                        txns.setCategoryName(gameCategory.getGameName());
                        txnsMapper.updateById(txns);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("ugPullOrderByBetID:", e);
            }
        }
    }

    /**
     * 注单请求
     */
    public UgApiTasksResponseData commonTasksRequest(Object object, String url,String type) throws Exception {

        UgApiTasksResponseData ugApiResponse = null;
        logger.info("uglog {} commonTasksRequest ,url:{},paramsMap:{}", url, object);
//        Map<String, String> trr = new HashMap<>();
//        trr.put("MemberAccount", "swuserid");
//        trr.put("CompanyKey", "y6RbXQyRr4");
//        trr.put("APIPassword", "RTJdTw36imErhpDm7p2ePb3Da3h6WT3S");
//        logger.info("ug_api_request:"+JSONObject.toJSONString(object));
//        String resultString1 = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, trr, type, userId);
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, JSONObject.toJSONString(object), type,0);
        logger.info("ug_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            ugApiResponse = JSONObject.parseObject(resultString, UgApiTasksResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("uglog {}:commonTasksRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, ugApiResponse:{}",
                    //userId, type, operateFlag, url,
                     type, null, url, JSONObject.toJSONString(object), resultString, JSONObject.toJSONString(ugApiResponse));
        }
        return ugApiResponse;
    }
        public Result errorCode(String errorCode, String errorMessage,String countryCode) {
            switch (errorCode) {
                case "000001"://	系统维护	SYSTEM MAINTENANCE
                    return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
                case "000002"://	授权已取消	AUTHORIZATION HAS BEEN CANCELED
                    return Result.failed("g000002", MessageUtils.get("g000002",countryCode));
                case "000003"://	IP 不在白名单中	IP NOT IN THE WHITELIST
                    return Result.failed("g000003", MessageUtils.get("g000003",countryCode));
                case "000004"://	API 密钥错误	API KEY ERROR
                    return Result.failed("g091160", MessageUtils.get("g091160",countryCode));
                case "000005"://	系统忙碌	SYSTEM BUSY
                    return Result.failed("g000005", MessageUtils.get("g000005",countryCode));
                case "000006"://	超出查询时间范围	OVER INQUIRY TIME RANGE
                    return Result.failed("g000006", MessageUtils.get("g000006",countryCode));
                case "000007"://	参数错误	ENTER PARAMETER ERROR
                    return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
                case "000008"://	频繁请求	REQUEST TOO MUCH FREQUENTLY
                    return Result.failed("g000008", MessageUtils.get("g000008",countryCode));
                case "000009"://	无效的网址	INVALID URL
                    return Result.failed("g091159", MessageUtils.get("g091159",countryCode));
                case "000010"://	API password 错误	API PASSWORD ERROR
                    return Result.failed("g000004", MessageUtils.get("g000004",countryCode));
//                case "000011"://	不允许的请求方法	METHOD NOT ALLOWED
//                    return Result.failed("g009999", errorMessage);
//                case "000012"://	operatorId 错误	OPERATOR ID ERROR
//                    return Result.failed("g009999", errorMessage);
//                case "000013"://	商户不存在	OPERATOR NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "000014"://	session 不存在	SESSION NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "000015"://	转帐 key 错误	TRANSFER KEY ERROR
//                    return Result.failed("g009999", errorMessage);
//                case "000016"://	不被接受的请求	REQUEST NOT ACCEPTED
//                    return Result.failed("g009999", errorMessage);
//                case "000017"://	商户未启用	OPERATOR IS NOT ENABLE
//                    return Result.failed("g009999", errorMessage);
//                case "100001"://	会员帐号不存在	MEMBER ACCOUNT IS NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "100002"://	不允许的货币	NOT ALLOWED CURRENCY
//                    return Result.failed("g009999", errorMessage);
//                case "100003"://	无效的帐号	INVALID ACCOUNT
//                    return Result.failed("g009999", errorMessage);
//                case "100004"://	会员帐号已存在	MEMBER ACCOUNT ALREADY EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "100005"://	建立帐号失败	CREATE ACCOUNT FAIL
//                    return Result.failed("g009999", errorMessage);
//                case "100006"://	错误的货币	CURRENCY ERROR
//                    return Result.failed("g009999", errorMessage);
//                case "100007"://	钱包不存在	WALLET NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "100008"://	钱包更新失败	WALLET UPDATE FAIL
//                    return Result.failed("g009999", errorMessage);
//                case "100009"://	管理帐号不存在	MANAGER ACCOUNT NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "100010"://	userId 只限英数	USER ID ALPHANUMERIC ONLY
//                    return Result.failed("g009999", errorMessage);
//                case "100011"://	不支援的货币	CURRENCY NOT SUPPORT
//                    return Result.failed("g009999", errorMessage);
//                case "100012"://	商户不存在	COMPANY ACCOUNT IS NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "200001"://	登入失败	LOGIN FAILED
//                    return Result.failed("g009999", errorMessage);
//                case "200002"://	帐户已关闭	ACCOUNT CLOSED
//                    return Result.failed("g009999", errorMessage);
//                case "200003"://	帐户已锁定	ACCOUNT LOCKED
//                    return Result.failed("g009999", errorMessage);
//                case "300001"://	输入的存款 / 提款金额少于或等于0	ENTER DEPOSIT/WITHDRAWAL AMOUNT LESS THAN OR EQUAL TO 0
//                    return Result.failed("g009999", errorMessage);
//                case "300002"://	存款 / 提款失败	DEPOSIT/WITHDRAWAL FAILED
//                    return Result.failed("g009999", errorMessage);
//                case "300003"://	存款 / 提款序号已存在	DEPOSIT/WITHDRAWAL SEQ ALREADY EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "300004"://	余额不足	INSUFFICIENT BALANCE
//                    return Result.failed("g009999", errorMessage);
//                case "300005"://	存款 / 提款金钥不正确	DEPOSIT/WITHDRAWAL KEY INCORRECT
//                    return Result.failed("g009999", errorMessage);
//                case "400001"://	存款 / 提款序号不存在	DEPOSIT/WITHDRAWAL SEQ NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "400002"://	存款 / 提款存在，但帐户不匹配	DEPOSIT/WITHDRAWAL TURNOVER EXISTS, BUT ACCOUNT NOT MATCH
//                    return Result.failed("g009999", errorMessage);
//                case "500001"://	投注限制无效	BET LIMIT INVALID
//                    return Result.failed("g009999", errorMessage);
//                case "500002"://	佣金组别不存在	COMM GROUP NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "500003"://	赔率表达不存在	ODDS STYLE NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "600001"://	投注限制不存在	BET LIMIT NOT EXIST
//                    return Result.failed("g009999", errorMessage);
//                case "800001"://	更新会员状态失败	UPDATE MEMBER STATUS ERROR
//                    return Result.failed("g009999", errorMessage);
//                case "800002"://	更新会员限额失败	UPDATE MEMBER LIMIT ERROR
//                    return Result.failed("g009999", errorMessage);
//                case "900001"://	非内部请求	NOT INTERNAL REQUEST
//                    return Result.failed("g091145", errorMessage);
                //        9999 失败。                                                Failed.
                default:
                    return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
            }
        }
}
