package com.indo.game.service.sbo.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.mapper.game.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.mapper.sbo.GameAgentMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.sbo.SboPlayerLoginJsonDTO;
import com.indo.game.pojo.dto.sbo.SboPlayerLogoutJsonDTO;
import com.indo.game.pojo.dto.sbo.SboRegisterPlayerJsonDTO;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.entity.sbo.GameAgent;
import com.indo.game.pojo.vo.callback.sbo.SboApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.sbo.SboService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * awc ae真人 游戏业务类
 *
 * @author
 */
@Service
public class SboServiceImpl implements SboService {

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
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameAgentMapper gameAgentMapper;
    @Autowired
    private GameLogoutService gameLogoutService;
    /**
     * 登录游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result sboGame(LoginInfo loginUser, String ip, String platform,String parentName,String loginType,String countryCode) {
        logger.info("sbolog  sboGame loginUser:{}, ip:{}, platform:{}, parentName:{}, loginType:{}", loginUser,ip,platform,parentName,loginType);
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
//        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
//            logger.info("站点sbo余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                return restrictedPlayer(gameParentPlatform,loginUser, gamePlatform, ip, cptOpenMember,loginType,countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                // 先退出
//                this.logout(loginUser,ip,countryCode);

                    //登录
                return initGame(gameParentPlatform,loginUser, gamePlatform, ip,loginType,countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 注册会员
     *
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(GameParentPlatform gameParentPlatform,LoginInfo loginUser, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String loginType,String countryCode) {

        try {
            LambdaQueryWrapper<GameAgent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GameAgent::getParentName, gameParentPlatform.getPlatformCode());
            GameAgent gameAgent = gameAgentMapper.selectOne(wrapper);
            if(null==gameAgent){
                return Result.failed("g000007",MessageUtils.get("g000007",countryCode));
            }

            SboRegisterPlayerJsonDTO sboRegisterPlayerJsonDTO = new SboRegisterPlayerJsonDTO();
            sboRegisterPlayerJsonDTO.setUsername(loginUser.getAccount());
            sboRegisterPlayerJsonDTO.setAgent(gameAgent.getUsername());//代理

            logger.info("SBO体育注册会员restrictedPlayer输入 apiUrl:{}, params:{}, loginUser:{}, ip:{}", OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/register-player.aspx", JSONObject.toJSONString(sboRegisterPlayerJsonDTO),loginUser,ip);

            SboApiResponseData sboApiResponse = commonRequest(sboRegisterPlayerJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/register-player.aspx", loginUser.getId().intValue(), ip, "restrictedPlayer");
            logger.info("SBO体育注册会员返回 sboApiResponse:{}", sboApiResponse);
            if (null == sboApiResponse) {
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
            }
            if ("0".equals(sboApiResponse.getError().getId()) || "4103".equals(sboApiResponse.getError().getId())) {
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(gameParentPlatform,loginUser, gamePlatform, ip,loginType,countryCode);
            } else {
                return errorCode(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg(),countryCode);
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,LoginInfo loginUser, GamePlatform gamePlatform, String ip,String loginType,String countryCode) throws Exception {
        try {
            SboPlayerLoginJsonDTO sboPlayerLoginJsonDTO = new SboPlayerLoginJsonDTO();
            sboPlayerLoginJsonDTO.setUsername(loginUser.getAccount());
//            if(!OpenAPIProperties.SBO_IS_PLATFORM_LOGIN.equals("Y")) {
//                sboPlayerLoginJsonDTO.setPortfolio(gamePlatform.getPlatformCode());
//            }
            sboPlayerLoginJsonDTO.setPortfolio("SportsBook");
            sboPlayerLoginJsonDTO.setIsWapSports(false);
            logger.info("SBO登录initGamer输入 apiUrl:{}, params:{}, loginUser:{}, ip:{}", OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/login.aspx", JSONObject.toJSONString(sboPlayerLoginJsonDTO),loginUser,ip);
            SboApiResponseData sboApiResponse = commonRequest(sboPlayerLoginJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/login.aspx", loginUser.getId().intValue(), ip, "PlayerLogin");
            logger.info("SBO登录initGamer返回 sboApiResponse:{}", sboApiResponse);
            if (null == sboApiResponse) {
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
            }
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
                        lang = "zh-cn";
                        break;
                    case "VN":
                        lang = "vi-vn";
                        break;
                    case "TW":
                        lang = "zh-tw";
                        break;
                    case "TH":
                        lang = "th-th";
                        break;
                    case "ID":
                        lang = "id-id";
                        break;
                    case "MY":
                        lang = "my-mm";
                        break;
                    case "KR":
                        lang = "ko-kr";
                        break;
                    case "JP":
                        lang = "ja-jp";
                        break;
                    default:
                        lang = gameParentPlatform.getLanguageType();
                        break;
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            if ("0".equals(sboApiResponse.getError().getId())) {
                ApiResponseData responseData = new ApiResponseData();
                StringBuilder urlStr = new StringBuilder();
                https://{response-url}&lang=en&oddstyle=MY&theme=sbo&oddsmode=double&device=d
                urlStr.append("https://").append(sboApiResponse.getUrl());
                urlStr.append("&lang=").append(lang);
                urlStr.append("&oddstyle=EU");
                urlStr.append("&theme=sbo&oddsmode=double&device=").append(loginType);
                responseData.setPathUrl((String) urlStr.toString());
                return Result.success(responseData);
            } else {
                return errorCode(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg(),countryCode);
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode) {
        SboPlayerLogoutJsonDTO sboPlayerLogoutJsonDTO = new SboPlayerLogoutJsonDTO();
        sboPlayerLogoutJsonDTO.setUsername(account);

        SboApiResponseData sboApiResponse = null;
        try {
            logger.info("SBO登出玩家输入 apiUrl:{}, params:{}, loginUser:{}, ip:{}", OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/logout.aspx", JSONObject.toJSONString(sboPlayerLogoutJsonDTO),account,ip);
            sboApiResponse = commonRequest(sboPlayerLogoutJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/logout.aspx", 0, ip, "logout");
            logger.info("SBO登出玩家返回 sboApiResponse:{}", sboApiResponse);
            if (null == sboApiResponse) {
                return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
            }
            if ("0".equals(sboApiResponse.getError().getId())) {
                return Result.success(sboApiResponse);
            } else {
                return errorCode(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104",MessageUtils.get("g100104",countryCode));
        }

    }

    /**
     * 公共请求
     */
    public SboApiResponseData commonRequest(Object obj, String url, Integer userId, String ip, String type) throws Exception {

        SboApiResponseData sboApiResponse = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, url, JSONObject.toJSONString(obj), type, userId);
        if (StringUtils.isNotEmpty(resultString)) {
            sboApiResponse = JSONObject.parseObject(resultString, SboApiResponseData.class);
        }

        return sboApiResponse;
    }

    public Result  errorCode(String errorCode,String errorMessage,String countryCode){
//        1	无效的Company Key
        if ("1".equals(errorCode)){
            return Result.failed("g000004",MessageUtils.get("g000004",countryCode));
        }else
//        2	无效的请求格式
        if ("2".equals(errorCode)){
            return Result.failed("g091041",MessageUtils.get("g091041",countryCode));
        }else
//        3	内部错误
        if ("3".equals(errorCode)){
            return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
        }else
//        4	无效的会员名称
            if ("4".equals(errorCode)){
                return Result.failed("g100002",MessageUtils.get("g100002",countryCode));
            }else
//        5	无效的国家
            if ("5".equals(errorCode)){
                return Result.failed("g100105",MessageUtils.get("g100105",countryCode));
            }else
//        6	无效的语言
            if ("6".equals(errorCode)){
                return Result.failed("g091005",MessageUtils.get("g091005",countryCode));
            }else
//        13	请选择安全系数更高的账号。建议您使用字母、数字和 '_' 的组合
            if ("13".equals(errorCode)){
                return Result.failed("g100106",MessageUtils.get("g100106",countryCode));
            }else
//        3101	无效的币别
            if ("3101".equals(errorCode)){
                return Result.failed("g100005",MessageUtils.get("g100005",countryCode));
            }else
//        3102	无效的主题Id
            if ("3102".equals(errorCode)){
                return Result.failed("g091048",MessageUtils.get("g091048",countryCode));
            }else
//        3104	创建代理失败
            if ("3104".equals(errorCode)){
                return Result.failed("g091036",MessageUtils.get("g091036",countryCode));
            }else
//        3201	更新状态失败
            if ("3201".equals(errorCode)){
                return Result.failed("g091049",MessageUtils.get("g091049",countryCode));
            }else
//        3202	无效的会员名称-更新状态
            if ("3202".equals(errorCode)){
                return Result.failed("g010001",MessageUtils.get("g010001",countryCode));
            }else
//        3203	已更新状态
            if ("3203".equals(errorCode)){
                return Result.failed("g091050",MessageUtils.get("g091050",countryCode));
            }else
//        3204	无效的状态
            if ("3204".equals(errorCode)){
                return Result.failed("g091025",MessageUtils.get("g091025",countryCode));
            }else
//        3205	无效的日期
            if ("3205".equals(errorCode)){
                return Result.failed("g091024",MessageUtils.get("g091024",countryCode));
            }else
//        3206	无效的单笔注单最低限额
            if ("3206".equals(errorCode)){
                return Result.failed("g091051",MessageUtils.get("g091051",countryCode));
            }else
//        3207	无效的单笔注单最高限额
            if ("3207".equals(errorCode)){
                return Result.failed("g091052",MessageUtils.get("g091052",countryCode));
            }else
//        3208	无效的单场比赛最高限额
            if ("3208".equals(errorCode)){
                return Result.failed("g091053",MessageUtils.get("g091053",countryCode));
            }else
//        3209	无效的真人赌场下注设定
            if ("3209".equals(errorCode)){
                return Result.failed("g091054",MessageUtils.get("g091054",countryCode));
            }else
//        3303	用户不存在
            if ("3303".equals(errorCode)){
                return Result.failed("g010001",MessageUtils.get("g010001",countryCode));
            }else
//        4101	代理不存在
            if ("4101".equals(errorCode)){
                return Result.failed("g091035",MessageUtils.get("g091035",countryCode));
            }else
//        4102	创建会员失败
            if ("4102".equals(errorCode)){
                return Result.failed("g100004",MessageUtils.get("g100004",countryCode));
            }else
//        4103	会员名称存在
            if ("".equals(errorCode)){
                return Result.failed("g100003",MessageUtils.get("g100003",countryCode));
            }else
//        4106	代理帐号为关闭状态
            if ("4106".equals(errorCode)){
                return Result.failed("g091055",MessageUtils.get("g091055",countryCode));
            }else
//        4107	請創建在代理而非下线底下
            if ("4107".equals(errorCode)){
                return Result.failed("g091057",MessageUtils.get("g091057",countryCode));
            }else
//        4201	验证失败
            if ("4201".equals(errorCode)){
                return Result.failed("g100107",MessageUtils.get("g100107",countryCode));
            }else
//        4401	无效的交易Id
            if ("4401".equals(errorCode)){
                return Result.failed("g091011",MessageUtils.get("g091011",countryCode));
            }else
//        4402	无效的交易金额.
//            比如:输入金额为 负值 或
//        输入金额含有 超过两位小数 (范例: 19.217 和 19.2245 均会报错)
            if ("4402".equals(errorCode)){
                return Result.failed("g300001",MessageUtils.get("g300001",countryCode));
            }else
//        4403	交易失败
            if ("4403".equals(errorCode)){
                return Result.failed("g091018",MessageUtils.get("g091018",countryCode));
            }else
//        4404	重复使用相同的交易Id
            if ("4404".equals(errorCode)){
                return Result.failed("g091038",MessageUtils.get("g091038",countryCode));
            }else
//        4501	余额不足
            if ("4501".equals(errorCode)){
                return Result.failed("g300004",MessageUtils.get("g300004",countryCode));
            }else
//        4502	于余额不足导致的回滚(Rollback Transaction)交易
            if ("4502".equals(errorCode)){
                return Result.failed("g300006",MessageUtils.get("g300006",countryCode));
            }else
//        4601	检查交易状态失败
            if ("4601".equals(errorCode)){
                return Result.failed("g091018",MessageUtils.get("g091018",countryCode));
            }else
//        4602	未找到任何交易
            if ("4602".equals(errorCode)){
                return Result.failed("g091017",MessageUtils.get("g091017",countryCode));
            }else
//        4701	获得余额失败
            if ("4701".equals(errorCode)){
                return Result.failed("g300007",MessageUtils.get("g300007",countryCode));
            }else
//        6101	获取客户报表失败
            if ("6101".equals(errorCode)){
                return Result.failed("g091058",MessageUtils.get("g091058",countryCode));
            }else
//        6102	获取客户注单失败
            if ("6102".equals(errorCode)){
                return Result.failed("g091059",MessageUtils.get("g091059",countryCode));
            }else
//        6666	没有此下注纪录
            if ("6666".equals(errorCode)){
                return Result.failed("g091017",MessageUtils.get("g091017",countryCode));
            }else
//        9527	无效的运动类型
            if ("9527".equals(errorCode)){
                return Result.failed("g100111",MessageUtils.get("g100111",countryCode));
            }else
//        9528	无效的盘口
            if ("9528".equals(errorCode)){
                return Result.failed("g100110",MessageUtils.get("g100110",countryCode));
            }else
//        9720	提款请求次数太过频繁
            if ("9720".equals(errorCode)){
                return Result.failed("g100109",MessageUtils.get("g100109",countryCode));
            }else
//        9721	无效的密码格式
            if ("9721".equals(errorCode)){
                return Result.failed("g100108",MessageUtils.get("g100108",countryCode));
            }else
//        9999	失败
        {
            return Result.failed("g009999",MessageUtils.get("g009999",countryCode));
        }
    }

}
