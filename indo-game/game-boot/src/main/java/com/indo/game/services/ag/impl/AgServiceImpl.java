package com.indo.game.services.ag.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Charsets;

import com.indo.common.constant.RedisKeys;
import com.indo.common.exception.BadRequestException;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.*;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.ag.*;
import com.indo.game.pojo.entity.*;
import com.indo.game.pojo.entity.ag.*;
import com.indo.game.services.*;
import com.indo.game.services.ag.AgService;
import com.indo.game.services.ag.AgbetOrderService;
import com.indo.game.utils.AGFTPUtil;
import com.indo.game.utils.AGUtil;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author eric
 * @Description: AG游戏业务
 */
@Service
public class AgServiceImpl implements AgService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenAPIProperties openAPIProperties;
    @Autowired
    private RedissonClient redissonClient;
    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private AgbetOrderService agbetOrderService;
    @Autowired
    private AgTransferLogMapper agTransferLogMapper;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private AgGameMapper agGameMapper;
    @Autowired
    private AgPayTypeMapper agPayTypeMapper;
    @Autowired
    private AgPlatformMapper agPlatformMapper;
    @Autowired
    private AgRoundMapper agRoundMapper;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService cptOpenMemberService;
    @Autowired
    private SysChessBalanceService sysChessBalanceService;

    @Override
    public Result<String> agJump(LoginInfo loginUser, String actype, String gameType, String ip) {
        logger.info("aglog:=========kaishi======");
        logger.info("aglog: {} agJump accno {} lotteryId {}", loginUser.getId(), loginUser.getAccount(), CaipiaoTypeEnum.AG_GAME.getTagType());
        //获取彩票id
        Integer lotteryId = CaipiaoTypeEnum.AG_GAME.getIntegerTagType();
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        //验证站点棋牌余额
        if (!sysChessBalanceService.isBalanceInChess(ChessBalanceTypeEnum.AG.getCode(), balance)) {
            logger.info("站点AG棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }
        // 使用公平锁
        String key = Constants.AG_ACCOUNT_TYPE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("aglog agJump isLocked {} return thread", key);
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.AG_ACCOUNT_TYPE + "_" + loginUser.getId() + gameType;
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            // 验证会员是否存在
            Result<String> result = null;
            if (bool) {
                // 创建试玩账号参数
                HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                hashMap.put(Constants.AG_API_PARAM_ACTYPE, actype);
                //币种
                //hashMap.put(Constants.AG_API_PARAM_CUR, CurEnum.CNY.getKeyName());
                hashMap.put(Constants.AG_API_PARAM_CUR, CurEnum.VND.getKeyName());
                //国际语言编码
                hashMap.put(Constants.AG_API_PARAM_LANG,"8");
                logger.info("aglog:=========888888======");
                hashMap.put(Constants.AG_API_PARAM_METHOD, Constants.AG_METHOD_LG);
                hashMap.put(Constants.AG_API_PARAM_CAGENT, OpenAPIProperties.AG_CAGENT_VALUE);
                // 跳转AG参数
                hashMap.put(Constants.AG_API_PARAM_GAME_TYPE, gameType);
                hashMap.put(Constants.AG_API_PARAM_MH5, "y");
                hashMap.put(Constants.AG_API_PARAM_SID, OpenAPIProperties.AG_CAGENT_VALUE + AGUtil.randomUID());
                // 试玩账号
                if ("0".equals(actype)) {
                    // 检测账号
                    String testPlay = OpenAPIProperties.AG_CAGENT_VALUE + RandomUtil.getRandomOne(6);
                    hashMap.put(Constants.AG_API_PARAM_PASSWORD, testPlay);
                    hashMap.put(Constants.AG_API_PARAM_LOGINNAME, testPlay);
                    String resultLG = null;
                    try {
                        resultLG = commonInvoking(loginUser.getId().intValue(), hashMap, OpenAPIProperties.AG_API_URL, ip, "agJump");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!resultLG.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
                        logger.error("aglog commonInvoking {} 试玩账号连接异常", resultLG);
                        return null;
                    }
                    // 获取跳转链接
                    hashMap.remove(Constants.AG_API_PARAM_METHOD);
                    result = new Result<String>();
                    result.setData(commonInvoking(loginUser.getId().intValue(), hashMap, OpenAPIProperties.AG_FORWARD_URL, ip, "agJump"));
                    return result;
                }
                // 真钱账号(支持转账)
                if ("1".equals(actype)) {
                    logger.info("aglog agJump run start ... userId {} account {} type{}", loginUser.getId(), loginUser.getNickName(), Constants.AG_ACCOUNT_TYPE);
                    String agPassword = null;
                    String agLoginName = null;

                    CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AG_ACCOUNT_TYPE);
                    if (cptOpenMember == null) {
                        cptOpenMember = new CptOpenMember();
                        agLoginName = openAPIProperties.platformUserPrefix + loginUser.getId();
                        agPassword = AGUtil.MD5Encode(agLoginName + Constants.AG_USER_MD5_KEY, Charsets.UTF_8.name()).substring(0, 16);
                        cptOpenMember.setUserId(loginUser.getId().intValue());
                        cptOpenMember.setUsername(agLoginName);
                        cptOpenMember.setPassword(agPassword);
                        cptOpenMember.setCreateTime(new Date());
                        cptOpenMember.setLoginTime(new Date());
                        cptOpenMember.setType(Constants.AG_ACCOUNT_TYPE);
                        externalService.saveCptOpenMember(cptOpenMember);
                    } else {
                        agLoginName = cptOpenMember.getUsername();
                        agPassword = cptOpenMember.getPassword();
                        CptOpenMember updateCptOpenMember = new CptOpenMember();
                        updateCptOpenMember.setId(cptOpenMember.getId());
                        updateCptOpenMember.setLoginTime(new Date());
                        externalService.updateCptOpenMember(updateCptOpenMember);
                    }
                    // 检测账号
                    hashMap.put(Constants.AG_API_PARAM_PASSWORD, agPassword);
                    hashMap.put(Constants.AG_API_PARAM_LOGINNAME, agLoginName);
                    String resultLG = commonInvoking(loginUser.getId().intValue(), hashMap, OpenAPIProperties.AG_API_URL, ip, "agJump");

                    if (!resultLG.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
                        logger.error("aglog commonInvoking {} 真钱账号连接失败", resultLG);
                        return null;
                    }
                    // 获取跳转链接
                    hashMap.remove(Constants.AG_API_PARAM_METHOD);
                    hashMap.put(Constants.AG_API_PARAM_DM, OpenAPIProperties.AG_DM);
                    hashMap.put(Constants.AG_API_PARAM_SID,
                            OpenAPIProperties.AG_CAGENT_VALUE + AGUtil.buildBillNo(loginUser.getId().intValue()));
                    result = new Result<String>();
                    result.setData(commonInvoking(loginUser.getId().intValue(), hashMap, OpenAPIProperties.AG_FORWARD_URL, ip, "agJump"));
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("aglog {} agJump occur error,  account:{} actype:{} gameType:{}", loginUser.getId().intValue(), loginUser.getNickName(), actype, gameType, e);
            return Result.failed(MessageUtils.get("thesystemisbusy"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Async
    @Override
    public void initAndStartGanmeAG(LoginInfo loginUser, String actype, String gameType, String ip) {
        // 是否开售校验
        Integer lotteryId = CaipiaoTypeEnum.AG_GAME.getIntegerTagType();
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return;
        }
        String key = OpenAPIProperties.AG_CAGENT_VALUE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("aglog initAndStartGanmeAG isLocked {} return thread", key);
            return;
        }

        String initKey = Constants.AG_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            if (bool) {
                HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AG_ACCOUNT_TYPE);
                // 查询CPT会员
                hashMap.put(Constants.AG_API_PARAM_ACTYPE, actype);
                hashMap.put(Constants.AG_API_PARAM_PASSWORD, cptOpenMember.getPassword());
                hashMap.put(Constants.AG_API_PARAM_LOGINNAME, cptOpenMember.getUsername());
                hashMap.put(Constants.AG_API_PARAM_CUR, CurEnum.CNY.getKeyName());
                hashMap.put(Constants.AG_API_PARAM_CAGENT, OpenAPIProperties.AG_CAGENT_VALUE);
                MemBaseinfo xiazhuren = memBaseinfoService.getUserByAccno(loginUser.getAccount());
                if (xiazhuren == null) {
                    logger.error("aglog initAndStartGanmeAG appMember is null:{}", xiazhuren);
                    return;
                }
//                if (xiazhuren.getGoldnum().compareTo(BigDecimal.ZERO) < 1) {
//                    logger.error("aglog initAndStartGanmeAG appMember balance {} is lt=; zero" + xiazhuren.getGoldnum());
//                    return;
//                }
//                BigDecimal money = xiazhuren.getGoldnum().setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal money = BigDecimal.valueOf(xiazhuren.getBalance());
                //验证站点棋牌余额
                if (!verificationBalanceInChess(ChessBalanceTypeEnum.AG.getCode(), money, loginUser)) {
                    logger.info("站点AG棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), money);
                    //站点棋牌余额不足
                    throw new BadRequestException(MessageUtils.get("tcgqifpccs"));
                }

                // CPT用户金额转入AG
                hashMap.put(Constants.AG_API_PARAM_TYPE, Constants.AG_IN);
                this.transferCredit(money, xiazhuren, hashMap, actype, -1,
                        openAPIProperties.platformName + MessageUtils.get("ttbta")+"/", ip, cptOpenMember);

                //标识有进入游戏
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_IN);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            }
        } catch (Exception e) {
            logger.error("aglog  {} initAndStartGanmeAG occur error  account:{} actype:{} gameType:{}", loginUser.getId(), loginUser.getNickName(), actype, gameType, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 验证站点棋牌余额是否足够
     */
    private boolean verificationBalanceInChess(String chessCode, BigDecimal balance, LoginInfo loginUser) {
        //当前该站点要进入AG棋牌所有AG用户共享锁 公平锁
        RLock lock = redissonClient.getFairLock(chessCode + "_GAME_CHESS_BALANCE");
        //检查该站点购买的ag棋牌余额是否够 该用户上分
        try {
            // 尝试加锁，最多等待10秒，上锁以后5秒自动解锁
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                if (sysChessBalanceService.isBalanceInChess(chessCode, balance)) {
                    //站点棋牌余额够 则先减去 balance
                    int i = sysChessBalanceService.subtractBalance(chessCode, balance);
                    if (i > 0) {
                        return true;
                    }
                }
            } else {
                throw new BadRequestException(MessageUtils.get("tnibptal"));
            }
        } catch (Exception e) {
            logger.error("aglog {} initAndStartGanmeAG-> verificationBalanceInChess  error:{} ", loginUser.getId(), e.getMessage());
            throw new BadRequestException(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public Result<String> exit(LoginInfo loginUser, String ip) {
        logger.info("aglog {} AGExit   account{} IP {} ", loginUser.getId(), loginUser.getNickName(), ip);
        String key = Constants.AG_EXIT_KEY + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("aglog {} agJump  keyislock:{}", loginUser.getId(), key);
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.AG_ACCOUNT_TYPE + "_" + loginUser.getId() + loginUser.getNickName();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            if (bool) {
                // 真钱账号
                String actype = "1";
                HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AG_ACCOUNT_TYPE);
                if (cptOpenMember != null && Constants.THIRD_GAME_IN.equals(cptOpenMember.getLayerNo())) {
                    hashMap.put(Constants.AG_API_PARAM_ACTYPE, actype);
                    hashMap.put(Constants.AG_API_PARAM_LOGINNAME, cptOpenMember.getUsername());
                    hashMap.put(Constants.AG_API_PARAM_PASSWORD, cptOpenMember.getPassword());
                    hashMap.put(Constants.AG_API_PARAM_CUR, CurEnum.CNY.getKeyName());
                    hashMap.put(Constants.AG_API_PARAM_CAGENT, OpenAPIProperties.AG_CAGENT_VALUE);
                    try {
                        hashMap.put(Constants.AG_API_PARAM_METHOD, Constants.AG_METHOD_GB);
                        String resultGB = commonInvoking(loginUser.getId().intValue(), hashMap, OpenAPIProperties.AG_API_URL, ip, "exit");
                        logger.info("aglog {} AGExit  method {}  resultGB {}", loginUser.getId(), Constants.AG_METHOD_GB, resultGB);
                        BigDecimal balanceAG = AGUtil.checkResponseBalance(resultGB);
                        MemBaseinfo xiazhuren = new MemBaseinfo();
                        xiazhuren.setId(loginUser.getId());

                        // ag余额转出至CPT
                        if (balanceAG != null && balanceAG.compareTo(BigDecimal.ZERO) == 1) {
                            logger.info("aglog {} AGExit  account{} IP {} method {} ", loginUser.getId(), loginUser.getNickName(), ip,
                                    Constants.AG_OUT);
                            hashMap.put(Constants.AG_API_PARAM_TYPE, Constants.AG_OUT);
                            this.transferCredit(balanceAG, xiazhuren, hashMap, actype, 1,
                                    MessageUtils.get("abtot") + openAPIProperties.platformName + "/", ip, cptOpenMember);

                            return Result.success(null);
                        }
                    } catch (Exception e) {
                        logger.error("aglog {} AGExit exit occur error, account:{} actype:{}", loginUser.getId(), loginUser.getNickName(), actype, e);
                        return Result.failed(MessageUtils.get("exitwithanerror"));
                    }
                }
            }
            return Result.success(null);
        } catch (Exception e) {
            logger.error("aglog {} exit occur error, account:{}", loginUser.getId(), loginUser.getNickName(), e);
            return Result.failed(MessageUtils.get("exitwithanerror"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * AG-CPT余额转入 转出
     */
    private void transferCredit(BigDecimal balanceAG, MemBaseinfo xiazhuren, HashMap<Object, Object> hashMap,
                                String actype, int type, String remark, String ip, CptOpenMember cptOpenMember) throws Exception {
        // 预备转账
        Integer uid = xiazhuren.getId().intValue();
        String billNo = OpenAPIProperties.AG_CAGENT_VALUE + AGUtil.buildBillNo(uid);
        logger.info("aglog {}  transferCredit, account{}, billNo{}", uid, cptOpenMember.getUsername(), billNo);
        String resultTC = this.transferCreditPrepare(uid, billNo, hashMap, balanceAG, ip);
        if (!resultTC.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
            logger.error("aglog {} request api to tc is error！ resultTC:{}", uid, resultTC);
            return;
        }

        //-1 是进入AG ; 1是退出AG
        if (type == -1) {
            //进入游戏先减款
            gameCommonService.inOrOutBalanceCommon(type, balanceAG, xiazhuren, remark + billNo,
                    cptOpenMember, Constants.AG_ACCOUNT_TYPE);
        }

        int num = 0;
        while (num <= 3) {
            // 标识正常情况下退出循序
            // 确认转账
            logger.info("aglog {} resultTCC开始 account{}, billNo{}", uid, cptOpenMember.getUsername(), billNo);
            String resultTCC = this.transferCreditComfirm(uid, billNo, hashMap, ip);
            logger.info("aglog {}  resultTCC结束 resultTCC {} account{}, billNo{}", cptOpenMember.getUsername(), resultTCC, uid, billNo);
            // 确认转账未成功---》进行查询
            if (!resultTCC.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
                //不成功，等待2秒再来查询这个billNo订单
                TimeUnit.SECONDS.sleep(2);
                // 查询转账订单
                String resultQOS = this.checkOrderState(uid, billNo, actype, ip);
                logger.info("aglog APIResponseInfoEnum.SUCCESS {}", resultQOS);
                if (org.apache.commons.lang3.StringUtils.isEmpty(resultQOS)) {
                    num++;
                    TimeUnit.SECONDS.sleep(2);
                }
                // 如果网络异常进行则进行重试（重试10次 / 50秒）
                if (resultQOS.equalsIgnoreCase(APIResponseInfoEnum.NETWORK_ERROR.getKeyValue())) {
                    num++;
                    TimeUnit.SECONDS.sleep(2);
                }
                // 如果订单未处理 等待五秒继续走确认转账流程 重试5次
                if (resultQOS.equals(APIResponseInfoEnum.ORDER_NOT_PROCESSED.getKeyName())) {
                    num++;
                    TimeUnit.SECONDS.sleep(2);
                }
                // 如果订单处理成功确认转账
                if (resultQOS.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
                    if (type == 1) {
                        gameCommonService.inOrOutBalanceCommon(type, balanceAG, xiazhuren, remark + billNo,
                                cptOpenMember, Constants.AG_ACCOUNT_TYPE);
                        //站点棋牌余额加回去
                        sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AG.getCode(), balanceAG);
                        //标识游戏下分
                        editGame(cptOpenMember);
                        break;
                    } else {
                        logger.info("aglog resultQOS.error1 {}", type);
                        break;
                    }
                } else {
                    if (type == -1) {
                        //上分失败
                        logger.info("aglog {} resultQOS error{}, billNo{}", uid, cptOpenMember.getUsername(), billNo);
                        String content = MessageUtils.get("abtot") + openAPIProperties.platformName + "/" + billNo;
                        gameCommonService.inOrOutBalanceCommon(type, balanceAG, xiazhuren, content + billNo,
                                cptOpenMember, Constants.AG_ACCOUNT_TYPE);
                        //站点棋牌余额加回去
                        sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AG.getCode(), balanceAG);
                        //标识游戏下分
                        editGame(cptOpenMember);
                        break;
                    } else {
                        logger.info("aglog resultQOS.error2 {}", type);
                        num++;
                    }
                }
            } else {
                if (type == 1) {
                    logger.info("aglog APIResponseInfoEnum.SUCCESS.");
                    // 确认转账API调用成功
                    gameCommonService.inOrOutBalanceCommon(type, balanceAG, xiazhuren, remark + billNo,
                            cptOpenMember, Constants.AG_ACCOUNT_TYPE);
                    //站点棋牌余额加回去
                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AG.getCode(), balanceAG);
                    //标识游戏下分
                    editGame(cptOpenMember);
                    break;
                }
                break;
            }
        }
        logger.info("aglog {} transferCredit, account{}, billNo{}", uid, cptOpenMember.getUsername(), billNo);
    }


    /**
     * 预备转账
     */
    private String transferCreditPrepare(Integer uid, String billNo, HashMap<Object, Object> hashMap, BigDecimal changeBalance, String ip) throws Exception {
        hashMap.put(Constants.AG_API_PARAM_METHOD, Constants.AG_METHOD_TC);
        hashMap.put(Constants.AG_API_PARAM_BILLNO, billNo);
        hashMap.put(Constants.AG_API_PARAM_CREDIT, changeBalance);
        return commonInvoking(uid, hashMap, OpenAPIProperties.AG_API_URL, ip, "transferCreditPrepare");
    }

    /**
     * 确认转账
     */
    private String transferCreditComfirm(Integer uid, String billNo, HashMap<Object, Object> hashMap, String ip) throws Exception {
        hashMap.put(Constants.AG_API_PARAM_METHOD, Constants.AG_METHOD_TCC);
        // 代表预备转账校验成功
        hashMap.put(Constants.AG_API_PARAM_FLAG, "1");
        String resultTCC = commonInvoking(uid, hashMap, OpenAPIProperties.AG_API_URL, ip, "transferCreditComfirm");
        // 确认转账接口日志
        return resultTCC;
    }

    /**
     * 查询转账订单状态
     *
     * @throws @throws Exception
     */
    private String checkOrderState(Integer uid, String billNo, String actype, String ip) throws Exception {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        hashMap.put(Constants.AG_API_PARAM_ACTYPE, actype);
        hashMap.put(Constants.AG_API_PARAM_CUR, CurEnum.CNY.getKeyName());
        hashMap.put(Constants.AG_API_PARAM_METHOD, Constants.AG_METHOD_QOS);
        hashMap.put(Constants.AG_API_PARAM_CAGENT, OpenAPIProperties.AG_CAGENT_VALUE);
        hashMap.put(Constants.AG_API_PARAM_BILLNO, billNo);
        return commonInvoking(uid, hashMap, OpenAPIProperties.AG_API_URL, ip, "checkOrderState");
    }

    /**
     * 递归重试 每次间隔五秒
     *
     * @param num           起始
     * @param numberOfTimes 次数
     * @param state         接口返回状态
     */
    private void retryCheckOrder(Integer uid, String billNo, String actype, int num, int numberOfTimes, String state, String ip) throws Exception {
        if (num < numberOfTimes && !state.equals(APIResponseInfoEnum.SUCCESS.getKeyName())) {
            logger.info("aglog {} retryCheckOrder ,重试：{}", uid, DateUtils.currentTime(null) + "每五秒重试：" + num);
            ++num;
            if (num > 0) {
                TimeUnit.SECONDS.sleep(5);
                state = this.checkOrderState(uid, billNo, actype, ip);
            }
            retryCheckOrder(uid, billNo, actype, num, numberOfTimes, state, ip);
        }
    }


    /**
     * -ag接口统一调用方法
     */
    @Override
    public String commonInvoking(Integer uid, HashMap<Object, Object> hashMap, String apiURL, String ip, String type) {
        String resultInfo = null;
        try {
            System.out.println("接口调用参数："+hashMap.toString().replace("{", "").replace("}", "").replace(",", "/\\\\/"));
            String params = AGUtil.encryptDES(hashMap.toString().replace("{", "").replace("}", "").replace(",", "/\\\\/"),
                    OpenAPIProperties.AG_DES_KEY);
            System.out.println("DES加密后接口调用参数："+params);
            String key = AGUtil.MD5Encode(params + OpenAPIProperties.AG_MD5_KEY, Charsets.UTF_8.name());
            String url = apiURL + params + "&key=" + key;
            if (apiURL.contains("forwardGame")) {
                logger.info("AGUrl info [{}]", url);
                return url;
            }
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setReadTimeout(60000);
            requestFactory.setConnectTimeout(60000);
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT)));
            RestTemplate client = new RestTemplate(requestFactory);
            HttpHeaders headers = new HttpHeaders();
            HttpMethod method = HttpMethod.GET;
            // 设置AG代理编号
            headers.add("User-Agent", "WEB_LIB_GI_" + hashMap.get(Constants.AG_API_PARAM_CAGENT));
            ResponseEntity<String> response = client.exchange(url, method, new HttpEntity<>(headers), String.class);
            String repXML = response.getBody();
            // 记录日志
            logger.info("aglog {} commonInvoking recordLog method:{},type:{}, url:{}, ip:{}, response:{},accountType:{}",
                    uid, String.valueOf(hashMap.get(Constants.AG_API_PARAM_METHOD)), type, apiURL, ip, repXML, Constants.AG_ACCOUNT_TYPE);
            // 解析
            StringReader sr = new StringReader(repXML);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            resultInfo = doc.getElementsByTagName("result").item(0).getAttributes().getNamedItem("info").getTextContent();
        } catch (Exception e) {
            logger.error("aglog {} commonInvoking connect error resultInfo:{},exception:{}", uid, resultInfo, e);
            //连接异常另起线程直至成功
            return resultInfo;
        }

        return resultInfo;

    }

    @Override
    public void pullAGHunterXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_HUNTER_DIR, redisTemplate, true);
                Constants.AG_FTP_HUNTER_DIR, true);
        List<String> lastList = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_HUNTER_DIR, redisTemplate, false);
                Constants.AG_FTP_HUNTER_DIR, false);
        list.addAll(lastList);
        try {
            List<AgFishBetOrder> betOrderList = new ArrayList<AgFishBetOrder>();
            for (String info : list) {
                NamedNodeMap map = ftpUtil.getXmlAttr(info);
                if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                    if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {
                        String dataType = map.getNamedItem("dataType").getTextContent();
                        // 下注记录
                        AgFishBetOrder agFishBetOrder = new AgFishBetOrder();
                        if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_HSR)) {
                            agFishBetOrder.setDataType(dataType);
                            buildDataFishBR(agFishBetOrder, map);
                            betOrderList.add(agFishBetOrder);
                            // 如果大于50条，则按照200条一次批量插入
                            if (betOrderList.size() >= 200) {
                                agbetOrderService.insertFishBatch(betOrderList);
                                betOrderList = new ArrayList<AgFishBetOrder>();
                                synFishOrderBetList(agbetOrderService.batchFishOrderList());
                            }
                        }

                    }
                }
            }
            if (betOrderList.size() > 0) {
                agbetOrderService.insertFishBatch(betOrderList);
                synFishOrderBetList(agbetOrderService.batchFishOrderList());
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void pullAGNextHunterXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_HUNTER_DIR, redisTemplate, false);
                Constants.AG_FTP_HUNTER_DIR, false);
        list.addAll(list);
        try {
            if (list.size() > 0) {
                List<AgFishBetOrder> betOrderList = new ArrayList<AgFishBetOrder>();
                for (String info : list) {
                    NamedNodeMap map = ftpUtil.getXmlAttr(info);
                    if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                        if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {
                            String dataType = map.getNamedItem("dataType").getTextContent();
                            // 下注记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_HSR)) {
                                AgFishBetOrder agFishBetOrder = new AgFishBetOrder();
                                agFishBetOrder.setDataType(dataType);
                                buildDataFishBR(agFishBetOrder, map);
                                betOrderList.add(agFishBetOrder);
                                // 如果大于50条，则按照200条一次批量插入
                                if (betOrderList.size() >= 200) {
                                    agbetOrderService.insertFishBatch(betOrderList);
                                    betOrderList = new ArrayList<AgFishBetOrder>();
                                }
                            }

                        }
                    }
                }
                if (betOrderList.size() > 0) {
                    agbetOrderService.insertFishBatch(betOrderList);

                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 同步打码量
     */
    @Async
    public void synFishOrderBetList(List<AgFishBetOrder> batchFishOrderBetList) {
        try {
            if (batchFishOrderBetList.size() > 0) {
                CptOpenMember agGameBO;
                for (AgFishBetOrder agFishBetOrderDO : batchFishOrderBetList) {
                    agGameBO = new CptOpenMember();
                    agGameBO.setUserId(agFishBetOrderDO.getMemberId().intValue());
                    gameCommonService.syncCodeSize(agGameBO, Constants.AG_ACCOUNT_TYPE, "AG", GoldchangeEnum.AG_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("同步AG捕鱼打码量异常", e);
        }

    }

    /**
     * 同步打码量
     */
    @Async
    public void synOrderBetList(List<AgBetOrder> batchOrderBetList) {
        try {
            if (batchOrderBetList.size() > 0) {
                CptOpenMember agGameBO;
                for (AgBetOrder agBetOrderDO : batchOrderBetList) {
                    agGameBO = new CptOpenMember();
                    agGameBO.setUserId(agBetOrderDO.getUserId().intValue());
                    gameCommonService.syncCodeSize(agGameBO, Constants.AG_ACCOUNT_TYPE, "AG", GoldchangeEnum.AG_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("同步AG视讯打码量异常", e);
        }
    }


    @Override
    @Transactional
    public void pullAGFTPXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_DEF_DIR, redisTemplate, true);
                Constants.AG_FTP_DEF_DIR, true);
        List<String> lastList = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_LAST_DIR, redisTemplate, false);
                Constants.AG_FTP_LAST_DIR, false);
        list.addAll(lastList);
        try {
            if (list.size() > 0) {
                List<AgBetOrder> betOrderList = new ArrayList<AgBetOrder>();
                HashMap<String, BigDecimal> queueHashMap = new HashMap<String, BigDecimal>();
                Map<String, String> agGameList = agGameList();
                Map<Integer, String> agPayNameList = agPayNameList();
                Map<String, String> agPlatformList = agPlatformList();
                Map<String, String> agRoundList = agRoundList();
                for (String info : list) {
                    NamedNodeMap map = ftpUtil.getXmlAttr(info);
                    if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                        if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {

                            String dataType = map.getNamedItem("dataType").getTextContent();
                            // 下注记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_BR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_EBR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_LBR)) {
                                AgBetOrder agBetOrder = new AgBetOrder();
                                agBetOrder.setDataType(dataType);
                                agBetOrder.setCreateTime(new Date());
                                buildDataBR(agBetOrder, map, agGameList, agPayNameList, agPlatformList, agRoundList);
                                betOrderList.add(agBetOrder);
                                // 累计玩家投注额度
                                String account = agBetOrder.getPlayerName();
                                BigDecimal amount = agBetOrder.getValidBetAmount() == null ? BigDecimal.ZERO
                                        : agBetOrder.getValidBetAmount();
                                if (queueHashMap.get(account) == null) {
                                    queueHashMap.put(account, new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                                queueHashMap.replace(account, queueHashMap.get(account).add(amount));
                                // 如果大于50条，则按照200条一次批量插入
                                if (betOrderList.size() >= 200) {
                                    agbetOrderService.insertBatch(betOrderList);
                                    betOrderList = new ArrayList<AgBetOrder>();
                                    synOrderBetList(agbetOrderService.batchOrderBetList());
                                }
                            }
                            // 转账记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_TR)) {
                                AgTransferLog agTransferLog = new AgTransferLog();
                                agTransferLog.setCreateTime(new Date());
                                agTransferLog.setDataType(dataType);
                                buildDataTR(agTransferLog, map);
                                LambdaQueryWrapper<AgTransferLog> wrapper = new LambdaQueryWrapper();
                                wrapper.eq(AgTransferLog::getTransferId,agTransferLog.getTransferId());

                                int count = agTransferLogMapper.selectCount(wrapper);
                                if (count <= 0) {
                                    agTransferLogMapper.insert(agTransferLog);
                                }
                            }
                        }
                    }
                }
                if (betOrderList.size() > 0) {
                    agbetOrderService.insertBatch(betOrderList);
                    synOrderBetList(agbetOrderService.batchOrderBetList());
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void pullAGAginXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_DEF_DIR, redisTemplate, false);
                Constants.AG_FTP_DEF_DIR, false);
        try {
            if (list.size() > 0) {
                List<AgBetOrder> betOrderList = new ArrayList<AgBetOrder>();
                HashMap<String, BigDecimal> queueHashMap = new HashMap<String, BigDecimal>();
                Map<String, String> agGameList = agGameList();
                Map<Integer, String> agPayNameList = agPayNameList();
                Map<String, String> agPlatformList = agPlatformList();
                Map<String, String> agRoundList = agRoundList();
                for (String info : list) {
                    NamedNodeMap map = ftpUtil.getXmlAttr(info);
                    if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                        if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {

                            String dataType = map.getNamedItem("dataType").getTextContent();
                            // 下注记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_BR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_EBR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_LBR)) {
                                AgBetOrder agBetOrder = new AgBetOrder();
                                agBetOrder.setDataType(dataType);
                                agBetOrder.setCreateTime(new Date());
                                buildDataBR(agBetOrder, map, agGameList, agPayNameList, agPlatformList, agRoundList);
                                betOrderList.add(agBetOrder);
                                // 累计玩家投注额度
                                String account = agBetOrder.getPlayerName();
                                BigDecimal amount = agBetOrder.getValidBetAmount() == null ? BigDecimal.ZERO
                                        : agBetOrder.getValidBetAmount();
                                if (queueHashMap.get(account) == null) {
                                    queueHashMap.put(account, new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                                queueHashMap.replace(account, queueHashMap.get(account).add(amount));
                                // 如果大于50条，则按照200条一次批量插入
                                if (betOrderList.size() >= 200) {
                                    agbetOrderService.insertBatch(betOrderList);
                                    betOrderList = new ArrayList<AgBetOrder>();
                                    synOrderBetList(agbetOrderService.batchOrderBetList());
                                }
                            }
                            // 转账记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_TR)) {
                                AgTransferLog agTransferLog = new AgTransferLog();
                                agTransferLog.setCreateTime(new Date());
                                agTransferLog.setDataType(dataType);
                                buildDataTR(agTransferLog, map);
                                LambdaQueryWrapper<AgTransferLog> wrapper = new LambdaQueryWrapper();
                                wrapper.eq(AgTransferLog::getTransferId,agTransferLog.getTransferId());

                                int count = agTransferLogMapper.selectCount(wrapper);
                                if (count <= 0) {
                                    agTransferLogMapper.insert(agTransferLog);
                                }
                            }
                        }
                    }
                }
                if (betOrderList.size() > 0) {
                    agbetOrderService.insertBatch(betOrderList);
                    synOrderBetList(agbetOrderService.batchOrderBetList());
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void pullAGYOPLAYXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_YOPLAY_DIR, redisTemplate, true);
                Constants.AG_FTP_YOPLAY_DIR, true);
        try {
            if (list.size() > 0) {
                List<AgBetOrder> betOrderList = new ArrayList<AgBetOrder>();
                HashMap<String, BigDecimal> queueHashMap = new HashMap<String, BigDecimal>();
                Map<String, String> agGameList = agGameList();
                Map<Integer, String> agPayNameList = agPayNameList();
                Map<String, String> agPlatformList = agPlatformList();
                Map<String, String> agRoundList = agRoundList();
                for (String info : list) {
                    NamedNodeMap map = ftpUtil.getXmlAttr(info);
                    if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                        if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {

                            String dataType = map.getNamedItem("dataType").getTextContent();
                            // 下注记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_BR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_EBR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_LBR)) {
                                AgBetOrder agBetOrder = new AgBetOrder();
                                agBetOrder.setDataType(dataType);
                                agBetOrder.setCreateTime(new Date());
                                buildDataBR(agBetOrder, map, agGameList, agPayNameList, agPlatformList, agRoundList);
                                betOrderList.add(agBetOrder);
                                // 累计玩家投注额度
                                String account = agBetOrder.getPlayerName();
                                BigDecimal amount = agBetOrder.getValidBetAmount() == null ? BigDecimal.ZERO
                                        : agBetOrder.getValidBetAmount();
                                if (queueHashMap.get(account) == null) {
                                    queueHashMap.put(account, new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                                queueHashMap.replace(account, queueHashMap.get(account).add(amount));
                                // 如果大于50条，则按照200条一次批量插入
                                if (betOrderList.size() >= 200) {
                                    agbetOrderService.insertBatch(betOrderList);
                                    synOrderBetList(agbetOrderService.batchOrderBetList());
                                    betOrderList = new ArrayList<AgBetOrder>();
                                }
                            }
                            // 转账记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_TR)) {
                                AgTransferLog agTransferLog = new AgTransferLog();
                                agTransferLog.setCreateTime(new Date());
                                agTransferLog.setDataType(dataType);
                                buildDataTR(agTransferLog, map);
                                LambdaQueryWrapper<AgTransferLog> wrapper = new LambdaQueryWrapper();
                                wrapper.eq(AgTransferLog::getTransferId,agTransferLog.getTransferId());

                                int count = agTransferLogMapper.selectCount(wrapper);
                                if (count <= 0) {
                                    agTransferLogMapper.insert(agTransferLog);
                                }
                            }
                        }
                    }
                }
                if (betOrderList.size() > 0) {
                    agbetOrderService.insertBatch(betOrderList);
                    synOrderBetList(agbetOrderService.batchOrderBetList());
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void pullNextAGYOPLAYXml() {
        AGFTPUtil ftpUtil = new AGFTPUtil();
        List<String> list = ftpUtil.lineNumberReader(OpenAPIProperties.AG_FTP_USER_NAME,
                OpenAPIProperties.AG_FTP_PASSWORD, OpenAPIProperties.AG_FTP_IP, OpenAPIProperties.AG_FTP_PORT,
                //Constants.AG_FTP_YOPLAY_DIR, redisTemplate, false);
                Constants.AG_FTP_YOPLAY_DIR, false);
        try {
            if (list.size() > 0) {
                List<AgBetOrder> betOrderList = new ArrayList<AgBetOrder>();
                HashMap<String, BigDecimal> queueHashMap = new HashMap<String, BigDecimal>();
                Map<String, String> agGameList = agGameList();
                Map<Integer, String> agPayNameList = agPayNameList();
                Map<String, String> agPlatformList = agPlatformList();
                Map<String, String> agRoundList = agRoundList();
                for (String info : list) {
                    NamedNodeMap map = ftpUtil.getXmlAttr(info);
                    if (map != null && map.getNamedItem("dataType") != null && map.getNamedItem("playerName") != null) {
                        if (map.getNamedItem("playerName").toString().contains(openAPIProperties.platformAgPrefix)) {

                            String dataType = map.getNamedItem("dataType").getTextContent();
                            // 下注记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_BR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_EBR)
                                    || dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_LBR)) {
                                AgBetOrder agBetOrder = new AgBetOrder();
                                agBetOrder.setDataType(dataType);
                                agBetOrder.setCreateTime(new Date());
                                buildDataBR(agBetOrder, map, agGameList, agPayNameList, agPlatformList, agRoundList);
                                betOrderList.add(agBetOrder);
                                // 累计玩家投注额度
                                String account = agBetOrder.getPlayerName();
                                BigDecimal amount = agBetOrder.getValidBetAmount() == null ? BigDecimal.ZERO
                                        : agBetOrder.getValidBetAmount();
                                if (queueHashMap.get(account) == null) {
                                    queueHashMap.put(account, new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                                queueHashMap.replace(account, queueHashMap.get(account).add(amount));
                                // 如果大于50条，则按照200条一次批量插入
                                if (betOrderList.size() >= 200) {
                                    agbetOrderService.insertBatch(betOrderList);
                                    synOrderBetList(agbetOrderService.batchOrderBetList());
                                    betOrderList = new ArrayList<AgBetOrder>();
                                }
                            }
                            // 转账记录
                            if (dataType.equalsIgnoreCase(Constants.AG_XML_DATA_TYPE_TR)) {
                                AgTransferLog agTransferLog = new AgTransferLog();
                                agTransferLog.setCreateTime(new Date());
                                agTransferLog.setDataType(dataType);
                                buildDataTR(agTransferLog, map);
                                LambdaQueryWrapper<AgTransferLog> wrapper = new LambdaQueryWrapper();
                                wrapper.eq(AgTransferLog::getTransferId,agTransferLog.getTransferId());

                                int count = agTransferLogMapper.selectCount(wrapper);
                                if (count <= 0) {
                                    agTransferLogMapper.insert(agTransferLog);
                                }
                            }
                        }
                    }
                }
                if (betOrderList.size() > 0) {
                    agbetOrderService.insertBatch(betOrderList);
                    synOrderBetList(agbetOrderService.batchOrderBetList());
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("pullAGFTPXml occur error.", e);
            throw new RuntimeException(e.getMessage());
        }

    }


    private String commonCheckIsNull(Node node) {
        String resultStr = null;
        if (node != null) {
            resultStr = node.getTextContent();
        }
        return resultStr;
    }


    private void buildDataFishBR(AgFishBetOrder agFishBetOrder, NamedNodeMap map) {
        String checkString = null;
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("ID")))) {
            agFishBetOrder.setHsrId(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("playerName")))) {
            agFishBetOrder.setMemberId(Long.valueOf(checkString.substring(checkString.lastIndexOf("V") + 1)));
            agFishBetOrder.setPlayerName(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("tradeNo")))) {
            agFishBetOrder.setTradeNo(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("platformType")))) {
            agFishBetOrder.setPlatformType(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("sceneId")))) {
            agFishBetOrder.setSceneId(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("type")))) {
            agFishBetOrder.setType(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("SceneStartTime")))) {
            agFishBetOrder.setSceneStartTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("SceneEndTime")))) {
            agFishBetOrder.setSceneStartTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("Roomid")))) {
            agFishBetOrder.setRoomId(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("Roombet")))) {
            agFishBetOrder.setRoombet(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("Cost")))) {
            agFishBetOrder.setCost(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("Earn")))) {
            agFishBetOrder.setEarn(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("Jackpotcomm")))) {
            agFishBetOrder.setJackPotComm(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("transferAmount")))) {
            agFishBetOrder.setTransferAmount(new BigDecimal(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("previousAmount")))) {
            agFishBetOrder.setPreviousAmount(new BigDecimal(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("currentAmount")))) {
            agFishBetOrder.setCurrentAmount(new BigDecimal(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("exchangeRate")))) {
            agFishBetOrder.setExchangeRate(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("currency")))) {
            agFishBetOrder.setCurrency(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("flag")))) {
            agFishBetOrder.setFlag(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("IP")))) {
            agFishBetOrder.setIp(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("creationTime")))) {
            agFishBetOrder.setCreationTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("gameCode")))) {
            agFishBetOrder.setGameCode(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("deviceType")))) {
            agFishBetOrder.setGameName(MessageUtils.get("fishingking"));
            agFishBetOrder.setGameCode(checkString);
        }
    }

    private void buildDataBR(AgBetOrder agBetOrder, NamedNodeMap map, Map<String, String> agGameList, Map<Integer, String> agPayNameList, Map<String, String> agPlatformList, Map<String, String> agRoundList) {

        String checkString = null;
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("billNo")))) {
            agBetOrder.setBillNo(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("playerName")))) {
            agBetOrder.setUserId(Integer.parseInt(checkString.substring(checkString.lastIndexOf("V") + 1)));
            agBetOrder.setPlayerName(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("agentCode")))) {
            agBetOrder.setAgentCode(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("gameCode")))) {
            agBetOrder.setGameCode(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("netAmount")))) {
            agBetOrder.setNetAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("betTime")))) {
            agBetOrder.setBetTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("gameType")))) {
            agBetOrder.setGameName(agGameList.get(checkString));
            agBetOrder.setGameType(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("betAmount")))) {
            agBetOrder.setBetAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("validBetAmount")))) {
            agBetOrder.setValidBetAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("flag")))) {
            agBetOrder.setFlag(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("playType")))) {
            agBetOrder.setPayName(agPayNameList.get(Integer.parseInt(checkString)));
            agBetOrder.setPlayType(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("currency")))) {
            agBetOrder.setCurrency(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("tableCode")))) {
            agBetOrder.setTableCode(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("loginIP")))) {
            agBetOrder.setLoginIp(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("recalcuTime")))) {
            agBetOrder.setRecalcuTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("platformType")))) {
            agBetOrder.setPlatformName(agPlatformList.get(checkString));
            agBetOrder.setPlatformType(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("remark")))) {
            agBetOrder.setRemark(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("round")))) {
            agBetOrder.setRoundName(agRoundList.get(checkString));
            agBetOrder.setRound(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("result")))) {
            agBetOrder.setResult(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("beforeCredit")))) {
            agBetOrder.setBeforeCredit(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("deviceType")))) {
            agBetOrder.setDeviceType(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("gameCategory")))) {
            agBetOrder.setGameCategory(Integer.parseInt(checkString));
        }
    }

    private void buildDataTR(AgTransferLog agTransferLog, NamedNodeMap map) {
        String checkString = null;
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("ID")))) {
            agTransferLog.setProjectId(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("agentCode")))) {
            agTransferLog.setAgentCode(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("transferId")))) {
            agTransferLog.setTransferId(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("tradeNo")))) {
            agTransferLog.setTradeNo(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("platformType")))) {
            agTransferLog.setPlatformType(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("playerName")))) {
            agTransferLog.setPlayerName(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("transferType")))) {
            agTransferLog.setTransferType(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("transferAmount")))) {
            agTransferLog.setTransferAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("previousAmount")))) {
            agTransferLog.setPreviousAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("currentAmount")))) {
            agTransferLog.setCurrentAmount(new BigDecimal(checkString).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("currency")))) {
            agTransferLog.setCurrency(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("exchangeRate")))) {
            agTransferLog.setExchangeRate(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("IP")))) {
            agTransferLog.setIp(checkString);
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("flag")))) {
            agTransferLog.setFlag(Integer.parseInt(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("creationTime")))) {
            agTransferLog.setCreationTime(DateUtils.parseDateLongFormat(checkString));
        }
        if (StringUtils.isNotBlank(checkString = commonCheckIsNull(map.getNamedItem("gameCode")))) {
            agTransferLog.setGameCode(checkString);
        }
    }


    private Map<String, String> agGameList() {
        Map<String, String> map = new HashMap<>();
        List<AgGame> list = RedisBusinessUtil.getAllAgGame();
        if (null == list) {
            AgGameExample example = new AgGameExample();
            list = agGameMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.AG_GANE_KEY, list);
            RedisBaseUtil.set(RedisKeys.AG_GANE_KEY, list);
        }

        if (list.size() > 0) {
            for (AgGame agGame : list) {
                map.put(agGame.getGameType(), agGame.getGameType());
            }
        }
        return map;
    }


    private Map<Integer, String> agPayNameList() {
        Map<Integer, String> map = new HashMap<>();
        List<AgPayType> list = RedisBusinessUtil.getAllAgPlayType();
        if (null == list) {
            AgPayTypeExample example = new AgPayTypeExample();
            list = agPayTypeMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.AG_PAY_KEY, list);
            RedisBaseUtil.set(RedisKeys.AG_PAY_KEY, list);
        }
        if (list.size() > 0) {
            for (AgPayType agPayType : list) {
                map.put(agPayType.getPayType(), agPayType.getPayName());
            }
        }
        return map;
    }


    private Map<String, String> agPlatformList() {
        Map<String, String> map = new HashMap<>();
        List<AgPlatform> list = RedisBusinessUtil.getAllGgPlatform();
        if (null == list) {
            AgPlatformExample example = new AgPlatformExample();
            list = agPlatformMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.AG_PLATFORM_KEY, list);
            RedisBaseUtil.set(RedisKeys.AG_PLATFORM_KEY, list);
        }
        if (list.size() > 0) {
            for (AgPlatform agPlatform : list) {
                map.put(agPlatform.getPlatformType(), agPlatform.getPlatformName());
            }
        }
        return map;
    }


    private Map<String, String> agRoundList() {
        Map<String, String> map = new HashMap<>();
        List<AgRound> list = RedisBusinessUtil.getAllAgRound();
        if (null == list) {
            AgRoundExample example = new AgRoundExample();
            list = agRoundMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.AG_ROUND_KEY, list);
            RedisBaseUtil.set(RedisKeys.AG_ROUND_KEY, list);
        }
        if (list.size() > 0) {
            for (AgRound agRound : list) {
                map.put(agRound.getRound(), agRound.getRoundName());
            }
        }
        return map;
    }

    //公共修改
    private void editGame(CptOpenMember cptOpenMember) {
        CptOpenMember gameOpenMember = new CptOpenMember();
        gameOpenMember.setId(cptOpenMember.getId());
        gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
        cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
    }
}
