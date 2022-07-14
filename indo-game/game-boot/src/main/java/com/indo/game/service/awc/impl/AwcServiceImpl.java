package com.indo.game.service.awc.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.dto.awc.AwcTransaction;
import com.indo.game.pojo.dto.awc.AwcApiResponseData;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.awc.AwcService;
import com.indo.common.utils.GameUtil;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * awc ae真人 游戏业务类
 *
 * @author eric
 */
@Service
public class AwcServiceImpl implements AwcService {

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
     * 登录游戏AWC-AE真人
     * @return loginUser 用户信息
     */
    @Override
    public Result awcGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName) {
        logger.info("awclog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")平台不存在");
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", "平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("(" + platform + ")游戏不存在");
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", "游戏未启用");
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", gamePlatform.getMaintenanceContent());
        }
//        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
//        if (null==memBaseinfo){
//            return Result.failed(loginUser.getAccount()+"用户不存在");
//        }
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点awc余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004","会员余额不足");
        }

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
                return createMemberGame(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin);
            } else {
                this.logout(loginUser,ip);
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                //登录
                return initGame(gameParentPlatform,gamePlatform, ip, cptOpenMember, isMobileLogin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }
    }

    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip){
        Map<String, String> trr = new HashMap<>();
        trr.put("userIds", loginUser.getAccount());

        AwcApiResponseData awcApiResponseData = null;
        try {
            awcApiResponseData = commonRequest(trr, OpenAPIProperties.AWC_API_URL_LOGIN+"/wallet/logout", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            if (null == awcApiResponseData ) {
                return Result.failed("g100104","网络繁忙，请稍后重试！");
            }
            if("0000".equals(awcApiResponseData.getStatus())){
                return Result.success(awcApiResponseData);
            }else {
                return errorCode(awcApiResponseData.getStatus(),awcApiResponseData.getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }

    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin) throws Exception {
        AwcApiResponseData awcApiResponseData = game(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin);
        if (null == awcApiResponseData ) {
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }
        if("0000".equals(awcApiResponseData.getStatus())){
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(awcApiResponseData.getUrl());
            return Result.success(responseData);
        }else {
            return errorCode(awcApiResponseData.getStatus(),awcApiResponseData.getDesc());
        }
    }
    /**
     * 创建玩家
     */
    private Result createMemberGame(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin) throws Exception {
        AwcApiResponseData awcApiResponseData = createMember(gameParentPlatform,gamePlatform, ip, cptOpenMember);
//        AwcApiResponseData awcApiResponseData = new AwcApiResponseData();
//        awcApiResponseData.setStatus("0000");
        if (null == awcApiResponseData ) {
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }
        if("0000".equals(awcApiResponseData.getStatus())||"1001".equals(awcApiResponseData.getStatus())){
            externalService.saveCptOpenMember(cptOpenMember);
            return initGame(gameParentPlatform,gamePlatform, ip, cptOpenMember,isMobileLogin);
        }else {
            return errorCode(awcApiResponseData.getStatus(),awcApiResponseData.getDesc());
        }
    }

    /***
     * 创建玩家
     * @param gamePlatform
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public AwcApiResponseData createMember(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember) {
        try {
            String time = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<>();
            trr.put("userId", cptOpenMember.getUserName());
            trr.put("currency", gameParentPlatform.getCurrencyType());//玩家货币代码
            trr.put("language", gameParentPlatform.getLanguageType());
            trr.put("userName", "");//玩家名称
//           platform: SEXYBCRT
//                   - gameType: LIVE
//                   - value (ID): {"limitId":[IDs]}
//            betLimit: {"SEXYBCRT":{"LIVE":{"limitId":[110901,110902]}}}
//            ※Each player allowed max 6 betLimit IDs.
//            ※每个玩家每个最多允许 6 组下注限红 ID
//            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(GamePlatform::getIsStart,"1");
//            wrapper.eq(GamePlatform::getParentName,gameParentPlatform.getPlatformCode());
//            List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
//            String betLimit = "";
//            for (int i=0;i<categoryList.size();i++){
//                GamePlatform gp = categoryList.get(i);
//                if(null!=gp.getBetLimit()&&!"".equals(gp.getBetLimit())) {
//                    betLimit += gp.getBetLimit();
//                    if (i != categoryList.size() - 1) {
//                        betLimit += ",";
//                    }
//                }
//            }
//            if(",".equals(betLimit.substring(betLimit.length()-1))){
//                betLimit = betLimit.substring(0,betLimit.length()-1);
//            }
//            trr.put("betLimit", "{"+betLimit+"}");//下注限红
            trr.put("betLimit", "{\"SEXYBCRT\":{\"LIVE\":{\"limitId\":[261101]}}}");//下注限红


            return commonRequest(trr, OpenAPIProperties.AWC_API_URL_LOGIN+"/wallet/createMember", cptOpenMember.getUserId(), ip, "createMember");
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
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
    public AwcApiResponseData game(GameParentPlatform gameParentPlatform,GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin) {
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("userId", cptOpenMember.getUserName());
//            true 行动设备登入
            if("1".equals(isMobileLogin)){
                trr.put("isMobileLogin", String.valueOf(true));
            }else {
//            false 桌面设备登入
                trr.put("isMobileLogin", String.valueOf(false));
            }
//            用于导回您指定的网站，需要设置 http:// 或 https://
//            Example 范例：http://www.google.com
            trr.put("externalURL", "");

            trr.put("language", gameParentPlatform.getLanguageType());
            String url = "/wallet/login";
            if(!gameParentPlatform.getPlatformCode().equals(gamePlatform.getParentName())) {
                url = "/wallet/doLoginAndLaunchGame";
                trr.put("gameCode", gamePlatform.getPlatformCode());//平台游戏代码
            }else {
                trr.put("gameForbidden", "");//指定对玩家隐藏游戏平台，您仅能透过 API 执行这个动作
            }
//                String str[] = gamePlatform.getPlatformCode().split("_");
//                trr.put("platform", str[0]);//游戏平台名称
                trr.put("platform", "SEXYBCRT");//游戏平台名称

//            GameCategory gameCategory = gameCategoryMapper.selectById(gamePlatform.getCategoryId());
//                trr.put("gameType", str[1]);//平台游戏类型
                trr.put("gameType", "LIVE");//平台游戏类型

//                List<GamePlatform> gamePlatformList = gameCommonService.getGamePlatformByParentName(gameParentPlatform.getPlatformCode());
//                List<String> codeList = new ArrayList<>();
//                Map<String, List<String>> gameForbiddenMap = new HashMap<>();
//
//                for (int i = 0; i < gamePlatformList.size(); i++) {
//                    GamePlatform gamePlatform1 = gamePlatformList.get(i);
//                    if (!gamePlatform.getPlatformCode().equals(gamePlatform1.getPlatformCode())) {
//                        String platformList[] = gamePlatform1.getPlatformCode().split("_");
//                        if (!gamePlatformList.contains(platformList[0])) {
//                            codeList.add(platformList[0]);
//                            List<String> typeList = gameForbiddenMap.get(platformList[0]);
//                            if (null == typeList) {
//                                typeList = new ArrayList<>();
//                                typeList.add(platformList[1]);
//
//                            } else {
//                                typeList.add(platformList[1]);
//                            }
//                            gameForbiddenMap.put(platformList[0], typeList);
//                        }
//                    }
//                }
//                JSONObject gameForbiddenStr = GameUtil.getJsonMap(gameForbiddenMap);
//                trr.put("gameForbidden", gameForbiddenStr.toString());//指定对玩家隐藏游戏平台，您仅能透过 API 执行这个动作

//           platform: SEXYBCRT
//                   - gameType: LIVE
//                   - value (ID): {"limitId":[IDs]}
//            betLimit: {"SEXYBCRT":{"LIVE":{"limitId":[110901,110902]}}}
//            ※Each player allowed max 6 betLimit IDs.
//            ※每个玩家每个最多允许 6 组下注限红 ID
            trr.put("betLimit", "");//下注限红

            return commonRequest(trr, OpenAPIProperties.AWC_API_URL_LOGIN+url, cptOpenMember.getUserId(), ip, "initGame");
        } catch (Exception e) {
            logger.error("awclog game error {} ", e);
            return null;
        }
    }

    @Override
    public void awcPullOrder(String platform) {
        try {
            // 设置传入的时间格式
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.ISO8601_DATE_FORMAT);
            // 指定一个日期
            // 对 calendar 设置为 date 所定的日期
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -3);
            String startTime = dateFormat.format(calendar.getTime());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            calendar1.add(Calendar.MINUTE, 1);
            String endTime = dateFormat.format(calendar1.getTime());


            commonAwcPullOrder(startTime, endTime, platform);
        } catch (Exception e) {
            logger.error("awclog aePullOrder error", e);
        }

    }

    /**
     * 公共获取记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    private void commonAwcPullOrder(String startTime, String endTime, String platform) {
        long start = System.currentTimeMillis();
        try {
            // 拼接参数
            TreeMap<String, String> trr = new TreeMap<>();
//            查询时间，使用 ISO 8601 格式
//            yyyy-MM-ddThh:mm:ss+|-hh:mm
//            Example 范例：2021-03-26T12:00:00+08:00
            trr.put("timeFrom", startTime);
            trr.put("endTime", endTime);
            trr.put("platform", platform);//游戏平台名称
//            若无带入参数则默认回传数值包含以下：
//            -1 Cancel bet 取消投注
//            1 Settled 已结账
//            2 Void 注单无效
//            9 Invalid 无效交易
            trr.put("status", "");
            trr.put("currency", "");//玩家货币代码
            trr.put("gameType", "");//平台游戏类型  Example 范例：LIVE
            trr.put("gameCode", "");//平台游戏代码 Example 范例：MX-LIVE-001

            // 获取游戏注单
            AwcApiResponseData result = commonRequest(trr, OpenAPIProperties.AWC_API_URL_LOGIN, 0, "127.0.0.1", "commonAePullOrder");
            if (null != result && "0000".equals(result.getStatus())) {
                List<AwcTransaction> list = (List<AwcTransaction>)result.getTransactions();

                if (null!=list && list.size() > 0) {
//                    awcTransactionMapper.insertBatch(list);
                }
            }
        } catch (Exception e) {
            logger.error("awclog commonAePullOrder error, startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, retryCount:{}", startTime, endTime, e);

            //retry
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                logger.error("awclog commonAePullOrder retry sleep occur error, startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, retryCount:{}", startTime, endTime, e);
            }
            commonAwcPullOrder(startTime, endTime,platform);
        }

        long end = System.currentTimeMillis();
        logger.info("awclog commonAePullOrder end. startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, totalRecord:{}, used times:{}ms", startTime, endTime, end - start);

    }


    /**
     * 公共请求
     */
    @Override
    public AwcApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("awclog {} commonRequest AE_AES_KEY:{},url:{},paramsMap:{}", userId, url, paramsMap);

        AwcApiResponseData awcApiResponse = null;
        paramsMap.put("cert", OpenAPIProperties.AWC_CERT);
        paramsMap.put("agentId", OpenAPIProperties.AWC_AGENTID);
        JSONObject sortParams = GameUtil.sortMap(paramsMap);
        logger.info("ug_api_request:"+sortParams);
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, paramsMap, type, userId);
        logger.info("acw_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            awcApiResponse = JSONObject.parseObject(resultString, AwcApiResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("awclog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, sortParams.toString(), resultString, JSONObject.toJSONString(awcApiResponse));
        }
        return awcApiResponse;
    }
    public Result  errorCode(String errorCode,String errorMessage){
//        9998	系统繁忙
        if ("9998".equals(errorCode)){
            return Result.failed("g000005",errorMessage);
        }else
//        0000	成功
        if ("0000".equals(errorCode)){
            return Result.failed("0000",errorMessage);
        }else
//        10	请输入所有数据
        if ("10".equals(errorCode)){
            return Result.failed("g090010",errorMessage);
        }else
//        11	您的代理底下没有此游戏
        if ("11".equals(errorCode)){
            return Result.failed("g090011",errorMessage);
        }else
//        1000  无效的使用者账号
        if ("1000".equals(errorCode)){
            return Result.failed("g100002",errorMessage);
        }else
//        1001	帐号已存在
        if ("1001".equals(errorCode)){
            return Result.failed("g100003",errorMessage);
        }else
//        1002	帐号不存在
        if ("1002".equals(errorCode)){
            return Result.failed("g010001",errorMessage);
        }else
//        1004	无效的货币
        if ("1004".equals(errorCode)){
            return Result.failed("g100001",errorMessage);
        }else
//        1005	语言不存在
        if ("1005".equals(errorCode)){
            return Result.failed("g091005",errorMessage);
        }else
//        1006	PT 设定为空
        if ("1006".equals(errorCode)){
            return Result.failed("g091006",errorMessage);
        }else
//        1007	PT 设定与上线冲突
        if ("1007".equals(errorCode)){
            return Result.failed("g091007",errorMessage);
        }else
//        1008	无效的 token
        if ("1008".equals(errorCode)){
            return Result.failed("g091008",errorMessage);
        }else
//        1009	无效时区
        if ("1009".equals(errorCode)){
            return Result.failed("g091009",errorMessage);
        }else
//        1010	无效的数量
        if ("1010".equals(errorCode)){
            return Result.failed("g091010",errorMessage);
        }else
//        1011	无效的交易代码
        if ("1011".equals(errorCode)){
            return Result.failed("g091011",errorMessage);
        }else
//        1012	有待处理的转帐
        if ("1012".equals(errorCode)){
            return Result.failed("g091012",errorMessage);
        }else
//        1013	帐号已锁
        if ("1013".equals(errorCode)){
            return Result.failed("g200003",errorMessage);
        }else
//        1014	帐号暂停
        if ("1014".equals(errorCode)){
            return Result.failed("g200002",errorMessage);
        }else
//        1016	交易代码已被执行过
        if ("1016".equals(errorCode)){
            return Result.failed("g091016",errorMessage);
        }else
//        1017	交易代码不存在
        if ("1017".equals(errorCode)){
            return Result.failed("g091017",errorMessage);
        }else
//        1018	余额不足
        if ("1018".equals(errorCode)){
            return Result.failed("g300004",errorMessage);
        }else
//        1019	没有资料
        if ("1019".equals(errorCode)){
            return Result.failed("g091019",errorMessage);
        }else
//        1024	无效的日期 (时间) 格式
        if ("1024".equals(errorCode)){
            return Result.failed("g091024",errorMessage);
        }else
//        1025	无效的交易状态
        if ("1025".equals(errorCode)){
            return Result.failed("g091025",errorMessage);
        }else
//        1026	无效的投注限制设定
        if ("1026".equals(errorCode)){
            return Result.failed("g091026",errorMessage);
        }else
//        1027	无效的认证码
        if ("1027".equals(errorCode)){
            return Result.failed("g091027",errorMessage);
        }else
//        1028	无法执行指定的行为，
        if ("1028".equals(errorCode)){
            return Result.failed("g091028",errorMessage);
        }else
//        1029	无效的 IP
//        通常发生于您的 IP 尚未加白
        if ("1029".equals(errorCode)){
            return Result.failed("g000003",errorMessage);
        }else
//        1030	使用无效的装置呼叫 (
        if ("1030".equals(errorCode)){
            return Result.failed("g091030",errorMessage);
        }else
//                1031	系统维护中
        if ("1031".equals(errorCode)){
            return Result.failed("g000001",errorMessage);
        }else
//                1032	重复登入
        if ("1032".equals(errorCode)){
            return Result.failed("g091032",errorMessage);
        }else
//                1033	无效的游戏代码
        if ("1033".equals(errorCode)){
            return Result.failed("g091033",errorMessage);
        }else
//                1034	您使用的时间参数不符
        if ("1034".equals(errorCode)){
            return Result.failed("g091034",errorMessage);
        }else
//                1035	无效的 Agent Id
        if ("1035".equals(errorCode)){
            return Result.failed("g091035",errorMessage);
        }else
//                1036	无效的参数
        if ("1036".equals(errorCode)){
            return Result.failed("g000007",errorMessage);
        }else
//                1037	错误的客户设定
//                通常可能发生于您的目标回调
        if ("1037".equals(errorCode)){
            return Result.failed("g091037",errorMessage);
        }else
//                1038	重复的交易
        if ("1038".equals(errorCode)){
            return Result.failed("g091038",errorMessage);
        }else
//                1039	无此交易
        if ("1039".equals(errorCode)){
            return Result.failed("g091039",errorMessage);
        }else
//                1040	请求逾时
        if ("1040".equals(errorCode)){
            return Result.failed("g091040",errorMessage);
        }else
//                1041	HTTP 状态错误
        if ("1041".equals(errorCode)){
            return Result.failed("g091041",errorMessage);
        }else
//                1042	HTTP 请求空白
        if ("1042".equals(errorCode)){
            return Result.failed("g091042",errorMessage);
        }else
//                1043	下注已被取消
        if ("1043".equals(errorCode)){
            return Result.failed("g091043",errorMessage);
        }else
//                1044	无效的下注
        if ("1044".equals(errorCode)){
            return Result.failed("g091044",errorMessage);
        }else
//                1045	帐便记录新增失败
        if ("1045".equals(errorCode)){
            return Result.failed("g091045",errorMessage);
        }else
//                1046	转帐失败！请立即联系
        if ("1046".equals(errorCode)){
            return Result.failed("g091046",errorMessage);
        }else
//                1047	游戏维护中
        if ("1047".equals(errorCode)){
            return Result.failed("g091047",errorMessage);
        }else
//                1056	[任一参数]为空值
        if ("1056".equals(errorCode)){
            return Result.failed("g091056",errorMessage);
        }else
//        9999	失败
            {
                return Result.failed("g009999",errorMessage);
            }


    }
}
