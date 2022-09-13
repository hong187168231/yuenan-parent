package com.indo.game.service.bti.impl;

import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.bti.BtiService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BtiServiceImpl implements BtiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;
    @Override
    public Result btiGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("bti体育log  btiGame loginUser:{}, ip:{}, platform:{}, parentName:{}, isMobileLogin:{}", loginUser,ip,platform,parentName,isMobileLogin);
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

//        BigDecimal balance = loginUser.getBalance();
//        //验证站点余额
//        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
//            logger.info("站点bti余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
        try {
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
                        lang = "zh";
                        break;
                    case "VN":
                        lang = "vi";
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
            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(GeneratorIdUtil.generateId());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);

                externalService.saveCptOpenMember(cptOpenMember);
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(OpenAPIProperties.BTI_API_URL + "/"+lang+"/sports/?operatorToken=" + cptOpenMember.getPassword());
                return Result.success(responseData);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(OpenAPIProperties.BTI_API_URL + "/"+lang+"/sports/?operatorToken=" + cptOpenMember.getPassword());
                return Result.success(responseData);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("btilogout {} btiGame account:{},btiCodeId:{}", ip, account, platform);
        try {
            // 游戏退出
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }


    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        104 新增会员资料错误,此帐号已被使用!!
            case "104":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
//        10404 帐号长度过长
            case "10404":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));

//        10405 帐号长度过短
            case "10405":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));

//        10406 密码长度过短
            case "10406":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10407  密码长度过长
            case "10407":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10409 姓名长度过长
            case "10409":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
// 10502  帐号名不得为空
            case "10502":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
// 10508  密码不得为空
            case "10508":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10509  姓名不得为空
            case "10509":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
// 10419  筹码格式错误(请用逗号隔开)
            case "10419":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10420  筹码个数错误(介于5-10个)
            case "10420":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10421  筹码种类错误
            case "10421":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10422  帐号只接受英文、数字、下划线与@
            case "10422":
                return Result.failed("g100106", MessageUtils.get("g100106",countryCode));
            // 10520  上层代理停用或停押
            case "10520":
                return Result.failed("g091035", MessageUtils.get("g091035",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
