package com.indo.game.service.ug.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.utils.GameUtil;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.mapper.ug.UgInsBetMapper;
import com.indo.game.mapper.ug.UgSubBetMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.ug.*;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.entity.ug.InsBet;
import com.indo.game.pojo.entity.ug.SubBet;
import com.indo.game.pojo.vo.callback.ug.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ug.UgService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private GamePlatformMapper gamePlatformMapper;

    @Autowired
    private TxnsMapper txnsMapper;
    @Autowired
    private UgInsBetMapper ugInsBetMapper;
    @Autowired
    private UgSubBetMapper ugSubBetMapper;

    /**
     * 登录游戏
     * @return loginUser 用户信息
     */
    @Override
    public Result ugGame(LoginInfo loginUser, String ip,String platform,String WebType,String parentName) {
        logger.info("uglog ugGame {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if(null==gameParentPlatform){
            return Result.failed("("+parentName+")游戏平台不存在");
        }
        if ("0".equals(gameParentPlatform.getIsStart())) {
            return Result.failed("g"+"100101","游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001",gameParentPlatform.getMaintenanceContent());
        }
        GamePlatform gamePlatform = null;
        if(!platform.equals(parentName)) {
            gamePlatform = new GamePlatform();
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
            if (null == gamePlatform) {
                return Result.failed("("+platform+")平台游戏不存在");
            }
            if ("0".equals(gamePlatform.getIsStart())) {
                return Result.failed("g"+"100102","游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047",gamePlatform.getMaintenanceContent());
            }
        }
        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点ug余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                return restrictedPlayer(gameParentPlatform,loginUser,gamePlatform, ip, cptOpenMember,WebType);
            } else {
                Result result = this.logout(loginUser,ip);
                if(null!=result&&"00000".equals(result.getCode())) {
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                    //登录
                    return initGame(gameParentPlatform,loginUser, gamePlatform, ip, WebType);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }
    }

    /**
     * 注册会员
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(GameParentPlatform gameParentPlatform,LoginInfo loginUser,GamePlatform gamePlatform, String ip,CptOpenMember cptOpenMember,String WebType) {
        logger.info("uglog restrictedPlayer {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            UgRegisterPlayerJsonDTO ugRegisterPlayerJsonDTO = new UgRegisterPlayerJsonDTO();
            ugRegisterPlayerJsonDTO.setMemberAccount(loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            ugRegisterPlayerJsonDTO.setNickName(null==loginUser.getNickName()?"":loginUser.getNickName());//昵称,长度需要小于 50
            ugRegisterPlayerJsonDTO.setCurrency(gameParentPlatform.getCurrencyType());//货币代码
            String url = "";
            if(null!=OpenAPIProperties.UG_AGENT&&!"".equals(OpenAPIProperties.UG_AGENT)){
                ugRegisterPlayerJsonDTO.setAgentID(OpenAPIProperties.UG_AGENT);//代理编号
                url = OpenAPIProperties.UG_API_URL+"/SportApi/RegisterByAgent";

            }else {
                url = OpenAPIProperties.UG_API_URL+"/SportApi/Register";
            }

            UgApiResponseData ugApiResponse = commonRequest(ugRegisterPlayerJsonDTO, url, loginUser.getId().intValue(), ip, "restrictedPlayer");

            if (null == ugApiResponse ) {
                return Result.failed("g100104","网络繁忙，请稍后重试！");
            }
            if("000000".equals(ugApiResponse.getErrorCode())){
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(gameParentPlatform,loginUser,gamePlatform, ip,WebType);
            }else {
                return Result.failed("g"+ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("uglog restrictedPlayer game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform,LoginInfo loginUser,GamePlatform gamePlatform, String ip,String WebType) throws Exception {
        logger.info("uglog initGame Login {} initGame ugGame account:{}, ugCodeId:{},ip:{}", loginUser.getId(), loginUser.getNickName(),ip);
        try {
            UgLoginJsonDTO ugLoginJsonDTO = new UgLoginJsonDTO();
            ugLoginJsonDTO.setMemberAccount(loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            ugLoginJsonDTO.setWebType(null==WebType||"".equals(WebType)?"PC":WebType);//登录类型: PC \Smart \Wap；默认值：PC
            System.out.println("请求ip:"+ip);
            ugLoginJsonDTO.setLoginIP(ip);//登录 IP
            ugLoginJsonDTO.setLanguage(gameParentPlatform.getLanguageType());// string 否 语言文字代码；默认值：EN
//            ugLoginJsonDTO.setPageStyle("");// string 否 网站版面 SP1, SP2, SP3, SP4, SP5,SP6,SP7；默认值：SP1
            ugLoginJsonDTO.setOddsStyle(gamePlatform.getPlatformCode());// string 否 赔率样式代码(OddsStyle) ；默认值：MY
//            ugLoginJsonDTO.setHostUrl("");// string 否 对接商域名,如果有 cname 指向我们域名的可以传入该参数，指向的域名询问我们客服
//            ugLoginJsonDTO.setPUrl("");// string 否 对接商的首页的链接，仅 Smart 版有效
//            ugLoginJsonDTO.setBalance("");// decimal 否 用于登录后的余额展示，仅 H5 版有效
//            ugLoginJsonDTO.setCashBalance("");// decimal 否 用于登录后的现金余额展示，仅 H5 版有效

            UgApiResponseData ugApiResponse = commonRequest(ugLoginJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/Login", loginUser.getId().intValue(), ip, "Login");
            if("000000".equals(ugApiResponse.getErrorCode())){
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(ugApiResponse.getData());
                return Result.success(responseData);
            }else {
                return Result.failed("g"+ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("uglog initGame Login game error {} ", e);
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip){
        logger.info("uglog logout {} initGame ugGame account:{}, ugCodeId:{},ip:{}", loginUser.getId(), loginUser.getNickName(),ip);
        UgLogoutJsonDTO ugLogoutJsonDTO = new UgLogoutJsonDTO();
        ugLogoutJsonDTO.setMemberAccount(loginUser.getAccount());

        UgApiResponseData ugApiResponse = null;
        try {
            ugApiResponse = commonRequest(ugLogoutJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/Logout", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            if("000000".equals(ugApiResponse.getErrorCode())){
                return Result.success(ugApiResponse);
            }else {
                return Result.failed("g"+ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("uglog logout {} ", e);
            return Result.failed("g100104","网络繁忙，请稍后重试！");
        }

    }

    /**
     * 公共请求
     */
    public UgApiResponseData commonRequest(Object object, String url, Integer userId, String ip, String type) throws Exception {

        UgApiResponseData ugApiResponse = null;
        logger.info("uglog {} commonRequest ,url:{},paramsMap:{}", userId, url, object);
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
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("uglog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, ugApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, JSONObject.toJSONString(object), resultString, JSONObject.toJSONString(ugApiResponse));
        }
        return ugApiResponse;
    }

    @Override
    public void ugPullOrder(){

        UgApiTasksResponseData ugApiResponse = null;
        UgTasksJsonDTO ugTasksJsonDTO = new UgTasksJsonDTO();
        String sortNo = txnsMapper.getMaxSortNo("UG Sports");
        ugTasksJsonDTO.setSortNo(Long.valueOf(null!=sortNo&&!"".equals(sortNo)?sortNo:"0"));//排序编号 返回大于该排序编号的注单
        try {
            ugApiResponse = commonTasksRequest(ugTasksJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/GetBetSheetBySort",  "ugPullOrder");
            if("000000".equals(ugApiResponse.getErrorCode())){
                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode("UG Sports");
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
                    for(SubBetDto subBetDto:subBets){
                        SubBet subBet = new SubBet();
                        BeanUtils.copyProperties(subBetDto, subBet);
                        ugSubBetMapper.insert(subBet);
                    }
                    List< InsBetDto > insBets = sortBetDto.getInsBets();
                    for(InsBetDto insBetDto:insBets){
                        InsBet insBet = new InsBet();
                        BeanUtils.copyProperties(insBetDto, insBet);
                        ugInsBetMapper.insert(insBet);
                    }
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
        wrapper.eq(Txns::getPlatform, "UG Sports");
        List<Txns> txnsList = txnsMapper.selectList(wrapper);
        for (Txns txns:txnsList) {
        ugTasksJsonBetIdDTO.setBetID(txns.getPlatformTxId());
            try {
                ugApiResponse = commonTasksRequest(ugTasksJsonBetIdDTO, OpenAPIProperties.UG_API_URL + "/SportApi/GetBetSheetByBetID", "ugPullOrderByBetID");
                if ("000000".equals(ugApiResponse.getErrorCode())) {
                    GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode("UG Sports");
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
                        List<SubBetDto> subBets = sortBetDto.getSubBets();
                        for (SubBetDto subBetDto : subBets) {
                            SubBet subBet = new SubBet();
                            BeanUtils.copyProperties(subBetDto, subBet);
                            ugSubBetMapper.updateById(subBet);
                        }
                        List<InsBetDto> insBets = sortBetDto.getInsBets();
                        for (InsBetDto insBetDto : insBets) {
                            InsBet insBet = new InsBet();
                            BeanUtils.copyProperties(insBetDto, insBet);
                            ugInsBetMapper.updateById(insBet);
                        }
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
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, JSONObject.toJSONString(object), type);
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

}
