package com.indo.game.services.ae.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.common.exception.BadRequestException;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.CaipiaoTypeEnum;
import com.indo.game.common.enums.ChessBalanceTypeEnum;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.ae.AeGameMapper;
import com.indo.game.mapper.ae.AeRoomMapper;
import com.indo.game.pojo.dto.AeBetOrderDTO;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.ae.*;
import com.indo.game.services.*;
import com.indo.game.services.ae.AeService;
import com.indo.game.services.ae.AebetOrderService;
import com.indo.game.utils.AEUtil;
import com.indo.game.utils.SnowflakeIdWorker;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ae 游戏业务类
 *
 * @author eric
 */
@Service
public class AeServiceImpl implements AeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private AebetOrderService aebetOrderService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private SysChessBalanceService sysChessBalanceService;
    @Autowired
    private AeGameMapper aeGameMapper;
    @Autowired
    private AeRoomMapper aeRoomMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CptOpenMemberService cptOpenMemberService;

    private static Map<Integer, String> AE_GAME_ID_MAP = new HashMap<>();

    static {
        AE_GAME_ID_MAP.put(30011, "zjh");
        AE_GAME_ID_MAP.put(30012, "blackjack");
        AE_GAME_ID_MAP.put(30013, "tbniuniu");
        AE_GAME_ID_MAP.put(30014, "sangong");
        AE_GAME_ID_MAP.put(30015, "ebgang");
        AE_GAME_ID_MAP.put(30016, "paijiu");
        AE_GAME_ID_MAP.put(30017, "buyu");
        AE_GAME_ID_MAP.put(30018, "brniuniu");
        AE_GAME_ID_MAP.put(30019, "longhu");
        AE_GAME_ID_MAP.put(30020, "honghei");
        AE_GAME_ID_MAP.put(30021, "baijiale");
        AE_GAME_ID_MAP.put(30022, "bairendezhou");
        AE_GAME_ID_MAP.put(30023, "toubao");
        AE_GAME_ID_MAP.put(30024, "shuiguoji");
        AE_GAME_ID_MAP.put(30025, "benchibaoma");
        AE_GAME_ID_MAP.put(30026, "wxsaoleihb");
        AE_GAME_ID_MAP.put(30027, "mpniuniu");
    }

    /**
     * 登录游戏
     *
     * @param aeCodeId 彩票id  lottery_id
     * @return loginUser 用户信息
     */
    @Override
    public Result<String> aeGame(LoginInfo loginUser, Integer aeCodeId, String ip) {
        logger.info("aelog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), aeCodeId);
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(aeCodeId)) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        String gameId = AE_GAME_ID_MAP.get(aeCodeId);
        if (StringUtils.isEmpty(gameId)) {
            return Result.failed(MessageUtils.get("tgdne"));
        }
        //初次判断站点棋牌余额是否够该用户
        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        //验证站点棋牌余额
        if (!sysChessBalanceService.isBalanceInChess(ChessBalanceTypeEnum.AE.getCode(), balance)) {
            logger.info("站点AE棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }

        String initKey = "AE_GAME_LOGIN_" + loginUser.getId();
        RedisLock lock = new RedisLock(initKey, 0, Constants.AE_TIMEOUT_MSECS);
        Result<String> result = null;
        try {
            if (lock.lock()) {
                String key = Constants.AE_ACCOUNT_TYPE + "_" + loginUser.getId();
                //long total = redisTemplate.opsForValue().increment(key, 1);
                long total = RedisBaseUtil.increment(key, 1);
                RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
                if (total > 1) {
                    logger.error("aelog cyCallback[{}] ", loginUser.getId());
                    return Result.failed(MessageUtils.get("frequentoperation"));
                }

                // 验证且绑定（KY-CPT第三方会员关系）
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AE_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    cptOpenMember = new CptOpenMember();
                    cptOpenMember.setUserId(loginUser.getId().intValue());
                    cptOpenMember.setUsername(OpenAPIProperties.AE_USER_PREFIX + loginUser.getId());
                    cptOpenMember.setPassword(OpenAPIProperties.AE_POSSWORD_PREFIX + loginUser.getId());
                    cptOpenMember.setCreateTime(new Date());
                    cptOpenMember.setLoginTime(new Date());
                    cptOpenMember.setType(Constants.AE_ACCOUNT_TYPE);
                    externalService.saveCptOpenMember(cptOpenMember);
                } else {
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                }
                //登录
                String gameUrl = initGame(gameId, ip, cptOpenMember);
                if (StringUtils.isNotEmpty(gameUrl)) {
                    result = new Result<>();
                    result.setData(gameUrl);
                }
            } else {
                return Result.failed(MessageUtils.get("etgptal"));
            }
        } catch (Exception e) {
            return Result.failed(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 登录
     */
    private String initGame(String gameId, String ip, CptOpenMember cptOpenMember) throws Exception {
        AeApiResponse result = game(gameId, ip, cptOpenMember);
        if (null == result || !result.isSuccess()) {
            return null;
        }
        String gameUrl = null;
        AeApiResponseData<JSONObject> aeApiResponseData = result.getData();
        JSONObject loginResult = aeApiResponseData.getResult();
        if (null != loginResult && StringUtils.isNotEmpty(loginResult.getString("url"))) {
            gameUrl = loginResult.getString("url");
        }
        return gameUrl;
    }


    /**
     * 游戏初始化上分
     */
    @Async
    @Override
    public void initAccountInfo(LoginInfo loginUser, Integer aeCodeId, String ip) {
        String key = CaipiaoTypeEnum.AE_GAME.getTagType() + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("ky initAccountInfo isLocked {} return thread", key);
            return;
        }

        String initKey = Constants.AE_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            /**
             * 自动转入转出（进入游戏查询用户在开元棋牌的可下分余额，有余额则先转出）
             */
            boolean bool = lock.writeLock().tryLock(10, 30, TimeUnit.SECONDS);
            if (bool) {

                // 是否开售校验
                if (!gameCommonService.isGameEnabled(aeCodeId)) {
                    return;
                }
                String gameId = AE_GAME_ID_MAP.get(aeCodeId);
                if (StringUtils.isEmpty(gameId)) {
                    return;
                }
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AE_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    return;
                }
                aeGameInit(cptOpenMember, loginUser, ip);

                //标识有进入游戏
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_IN);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            }
        } catch (Exception e) {
            logger.error("aelog {} initAccountInfo occur error.  kindId:{}, ip:{}", loginUser.getId(), aeCodeId, ip, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 上分操作
     */

    public void aeGameInit(CptOpenMember cptOpenMember, LoginInfo loginUser, String ip) throws InterruptedException {

        MemBaseinfo memBaseinfo = memBaseinfoService.getUserByAccno(loginUser.getAccount());
        if (null == memBaseinfo) {
            logger.info("aelog {} aeGameInit is null", loginUser.getId());
            return;
        }

        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        if (balance.compareTo(BigDecimal.ZERO) < 1) {
            logger.info("aelog {} aeGameInit balance {} is ZERO", loginUser.getId(), balance);
            return;
        }

        //验证站点棋牌余额
        if (!verificationBalanceInChess(ChessBalanceTypeEnum.AE.getCode(), balance, loginUser)) {
            logger.info("站点AE棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            throw new BadRequestException(MessageUtils.get("tcgqifpccs"));
        }

        //生成单号
        String orderNo = SnowflakeIdWorker.createOrderSn() + Constants.AE_SF_ORDER_SUFFIX;

        //先扣款
        String remark = OpenAPIProperties.PLATFORM_NAME + MessageUtils.get("ttbtac")+"/" + orderNo;
        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AE_OUT.getValue(), balance, memBaseinfo, remark,
                cptOpenMember, Constants.AE_ACCOUNT_TYPE);

        logger.info("aelog {} aeGameInit balance {} is ZERO", loginUser.getId(), balance);
        AeApiResponse result = gameInit(balance, cptOpenMember, orderNo, ip);
        if (null == result || !result.isSuccess()) {
            int checkOrderNum = 0;
            while (checkOrderNum <= 3) {
                logger.info("aelog {} aeGameInit orderNo {} ", loginUser.getId(), orderNo);
                AeApiResponse status = gameOrderNo(cptOpenMember, orderNo, ip);
                logger.info("aelog {} aeGameInit orderNo {} result ", loginUser.getId(), orderNo, JSONObject.toJSONString(status));
                if (null != status && status.isSuccess()) {
                    AeApiResponseData<JSONObject> aeApiResponseData = status.getData();
                    JSONObject orderResult = aeApiResponseData.getResult();
                    if (orderResult.getInteger("order_status").equals(Constants.AE_ORDER_ZERO)) {
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    } else if (orderResult.getInteger("order_status").equals(Constants.AE_ORDER_ONE)) {
                        logger.info("aelog {} success orderNo {} ", loginUser.getId(), orderNo);
                        break;
                    } else {
                        //其他状态为上分失败 则分加回去
                        String content = MessageUtils.get("aecbtot") + OpenAPIProperties.PLATFORM_NAME + "/" + orderNo;
                        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AE_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AE_ACCOUNT_TYPE);
                        //站点棋牌余额加回去
                        sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AE.getCode(), balance);
                        break;
                    }
                    //异常返回null  继续请求  直到返回成功失败
                } else if (null == status) {
                    checkOrderNum++;
                    TimeUnit.SECONDS.sleep(2);
                }
            }
        }
    }

    /**
     * 验证站点棋牌余额是否足够
     *
     * @param chessCode
     * @param balance
     * @param loginUser
     * @return
     */
    private boolean verificationBalanceInChess(String chessCode, BigDecimal balance, LoginInfo loginUser) {
        //当前该站点要进入AE棋牌所有AE用户共享锁 公平锁
        //RedisLock lock = new RedisLock(chessCode + "_GAME_CHESS_BALANCE", 5000, 5000);
        RLock lock = redissonClient.getFairLock(chessCode + "_GAME_CHESS_BALANCE");
        //检查该站点购买的ae棋牌余额是否够 该用户上分
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
            logger.error("aelog {} initGame-> verificationBalanceInChess  error:{} ", loginUser.getId(), e.getMessage());
            throw new BadRequestException(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return false;
    }

    /***
     * 登录请求
     * @param gameId
     * @param ip
     * @param cptOpenMember
     * @return
     */
    public AeApiResponse game(String gameId, String ip, CptOpenMember cptOpenMember) {
        AeApiResponse result = new AeApiResponse();
        try {
            String time = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<>();
            trr.put("username", cptOpenMember.getUsername());
            trr.put("point", "0");
            trr.put("password", cptOpenMember.getPassword());
            trr.put("game_id", gameId);
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("ip", ip);
            trr.put("callback_url", "");
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            trr.put("timestamp", time);
            trr.put("canback", "0");
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);
            result = commonRequest(trr, cptOpenMember.getUsername(), OpenAPIProperties.AE_API_URL_LOGIN, cptOpenMember.getUserId(), ip, "initGame");
        } catch (Exception e) {
            logger.error("aelog game error {} ", e);
            return result;
        }
        return result;
    }

    /**
     * 上分请求
     */
    public AeApiResponse gameInit(BigDecimal balance, CptOpenMember cptOpenMember, String orderNo, String ip) {
        AeApiResponse result = new AeApiResponse();
        try {
            String timestamp = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<>();
            trr.put("username", cptOpenMember.getUsername());
            trr.put("orderid", orderNo);
            trr.put("password", cptOpenMember.getPassword());
            trr.put("point", balance.toString());
            trr.put("ip", ip);
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            trr.put("timestamp", timestamp);
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);
            logger.info("aelog {} start gameInit orderNo {} balance{}", cptOpenMember.getUserId(), orderNo, balance);
            result = commonRequest(trr, cptOpenMember.getUsername(), OpenAPIProperties.AE_API_URL_UPPER_POINTS, cptOpenMember.getUserId().intValue(), ip, "aeInit");
            logger.info("aelog {} end gameInit orderNo {} balance{} result{}", cptOpenMember.getUserId(), orderNo, balance, JSONObject.toJSONString(result));
        } catch (Exception e) {
            logger.error("aelog gameInit error {} orderNo{}", e, orderNo);
            return result;
        }
        return result;
    }

    /**
     * 查询订单
     */
    public AeApiResponse gameOrderNo(CptOpenMember cptOpenMember, String orderNo, String ip) {
        AeApiResponse result = new AeApiResponse();
        try {
            String timestamp = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<>();
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("orderid", orderNo);
            trr.put("timestamp", timestamp);
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);
            logger.info("aelog {} start gameOrderNo orderNo {} balance{}", cptOpenMember.getUserId(), orderNo);
            result = commonRequest(trr, cptOpenMember.getUsername(), OpenAPIProperties.AE_API_URL_CHECK_ORDERSTATUS, cptOpenMember.getUserId().intValue(), ip, "gameOrderNo");
            logger.info("aelog {} end gameOrderNo orderNo {} balance{} result{}", cptOpenMember.getUserId(), orderNo, JSONObject.toJSONString(result));
        } catch (Exception e) {
            logger.error("aelog gameOrderNo error {} orderNo{}", e);
            return result;
        }
        return result;
    }


    /**
     * 下分操作
     */
    public AeApiResponse gameExit(CptOpenMember cptOpenMember, String orderNo, String ip) {
        AeApiResponse result = new AeApiResponse();
        try {
            String timestamp = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<String, String>();
            trr.put("username", cptOpenMember.getUsername());
            trr.put("password", cptOpenMember.getPassword());
            trr.put("point", "");
            trr.put("orderid", orderNo);
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("timestamp", timestamp);
            trr.put("ip", ip);
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);
            logger.info("aelog {} start gameExit orderNo {} balance{}", cptOpenMember.getUserId(), orderNo);
            result = commonRequest(trr, cptOpenMember.getUsername(), OpenAPIProperties.AE_API_URL_LOWER_POINTS, cptOpenMember.getUserId().intValue(), ip, "aeExit");
            logger.info("aelog {} end gameExit orderNo {} balance{} result{}", cptOpenMember.getUserId(), orderNo, JSONObject.toJSONString(result));
        } catch (Exception e) {
            logger.error("aelog gameOrderNo error {} orderNo{}", e);
            return result;
        }
        return result;
    }


    /**
     * 下分操作
     */
    public AeApiResponse gameBalance(CptOpenMember cptOpenMember, String ip) {
        AeApiResponse result = new AeApiResponse();
        try {
            String timestamp = System.currentTimeMillis() / 1000 + "";
            Map<String, String> trr = new HashMap<String, String>();
            trr.put("username", cptOpenMember.getUsername());
            trr.put("password", cptOpenMember.getPassword());
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            trr.put("timestamp", timestamp);
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);
            logger.info("aelog {} start gameBalance balance{}", cptOpenMember.getUserId());
            result = commonRequest(trr, cptOpenMember.getUsername(), OpenAPIProperties.AE_API_URL_CHECK_PLAYER, cptOpenMember.getUserId().intValue(), ip, "aeExit");
            logger.info("aelog {} end gameBalance  balance{} result{}", cptOpenMember.getUserId(), JSONObject.toJSONString(result));
        } catch (Exception e) {
            logger.error("aelog gameOrderNo error {} orderNo{}", e);
            return result;
        }
        return result;
    }


    /**
     * AE下分
     */
    @Override
    public Result<String> aeSignOut(LoginInfo loginUser, String ip) {
        String key = Constants.AE_EXIT_KEY + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("aelog aeSignOut isLocked {} return thread", key);
            return Result.success();
        }

        RedisLock lock = new RedisLock("AE_GAME_EXIT_" + loginUser.getId(), 0, 3000);
        try {
            if (lock.lock()) {
                //根据id 类型 查询第三方用户信息
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.AE_ACCOUNT_TYPE);
                if (null == cptOpenMember) {
                    logger.info("aelog {} toAeSignOut exit cptOpenMember is null type:{}", loginUser.getId());
                    return Result.success();
                }
                Result<String> aeSignOut = toHandleAeSignOut(loginUser, ip, cptOpenMember, "aeSignOut");
                //标识游戏下分
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
                return aeSignOut;
            } else {
                logger.info("aelog {} aeSignOut request repeat, ip:{}", loginUser.getId(), ip);
                return Result.success();
            }
        } catch (Exception e) {
            logger.info("aelog {} aeSignOut error:{}, ip:{}", e.getMessage(), loginUser.getId(), ip);
            return Result.success();
        } finally {
            //主动下分的让其自然过期，过期时间内的重复请求直接丢弃
        }
    }


    /**
     * 下分逻辑
     */
    private Result<String> toHandleAeSignOut(LoginInfo loginUser, String ip, CptOpenMember cptOpenMember, String type) throws InterruptedException {
        Integer userId = loginUser.getId().intValue();
        logger.info("aelog {} toAeSignOut type:{}, ip:{}, changeBalance:{}", userId, type, ip);


        MemBaseinfo memBaseinfo = memBaseinfoService.getUserByAccno(loginUser.getAccount());
        if (null == memBaseinfo) {
            logger.info("aelog {} toAeSignOut exit cptOpenMember is null type:{}", userId, type);
            return Result.success();
        }
        //生成单号
        String orderNo = SnowflakeIdWorker.createOrderSn() + Constants.AE_XF_ORDER_SUFFIX;

        //查询是否有可下余额
        AeApiResponse result = gameBalance(cptOpenMember, ip);
        if (null != result && result.isSuccess()) {
            AeApiResponseData<JSONObject> data = result.getData();
            JSONObject pointInfo = data.getResult();
            BigDecimal balance = pointInfo.getBigDecimal("point");
            //余额大于0 执行下分
            if (balance.compareTo(BigDecimal.ZERO) == 1) {
                // 标识正常情况下退出循序
                AeApiResponse exitResult = gameExit(cptOpenMember, orderNo, ip);
                if (null == exitResult && !exitResult.isSuccess()) {
                    int checkOrderNum = 0;
                    while (checkOrderNum <= 3) {
                        logger.info("aelog {} aeGameInit orderNo {} ", loginUser.getId(), orderNo);
                        AeApiResponse status = gameOrderNo(cptOpenMember, orderNo, ip);
                        logger.info("aelog {} aeGameInit orderNo {} result ", loginUser.getId(), orderNo, JSONObject.toJSONString(status));
                        if (null != status && status.isSuccess()) {
                            AeApiResponseData<JSONObject> aeApiResponseData = status.getData();
                            JSONObject orderResult = aeApiResponseData.getResult();
                            if (orderResult.getInteger("order_status").equals(Constants.AE_ORDER_ZERO)) {
                                checkOrderNum++;
                                TimeUnit.SECONDS.sleep(2);
                            } else if (orderResult.getInteger("order_status").equals(Constants.AE_ORDER_ONE)) {
                                logger.info("aelog {} success orderNo {} ", loginUser.getId(), orderNo);
                                String remark = MessageUtils.get("aecbtot") + OpenAPIProperties.PLATFORM_NAME + "/" + orderNo;
                                gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AE_IN.getValue(), orderResult.getBigDecimal("point"), memBaseinfo, remark,
                                        cptOpenMember, Constants.AE_ACCOUNT_TYPE);
                                //站点棋牌余额加回去
                                sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AE.getCode(), orderResult.getBigDecimal("point"));
                                break;
                            }
                            //异常返回null  继续请求  直到返回成功失败
                        } else if (null == status) {
                            checkOrderNum++;
                            TimeUnit.SECONDS.sleep(2);
                        }
                    }
                } else {
                    AeApiResponseData<JSONObject> aeApiResponseData = exitResult.getData();
                    JSONObject balanceResult = aeApiResponseData.getResult();
                    String remark = MessageUtils.get("aecbtot") + OpenAPIProperties.PLATFORM_NAME + "/" + orderNo;
                    gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AE_IN.getValue(), balanceResult.getBigDecimal("point"), memBaseinfo, remark,
                            cptOpenMember, Constants.AE_ACCOUNT_TYPE);
                    //站点棋牌余额加回去
                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.AE.getCode(), balanceResult.getBigDecimal("point"));
                    return Result.success();
                }
            }
        }
        return Result.success();
    }

    @Override
    public void aePullOrder() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -3);
            String startTime = String.valueOf(calendar.getTimeInMillis() / 1000);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MINUTE, 1);
            String endTime = String.valueOf(calendar1.getTimeInMillis() / 1000);
            int pageNo = 1;
            int pageSize = 100;

            Map<String, String> aeGameMapList = aeGameList();
            Map<Integer, String> aeRoomMapList = aeRoomList();

            int totalRecord = commonAePullOrder(startTime, endTime, pageNo, pageSize, 0, aeGameMapList, aeRoomMapList);
            // 计算是否超过一页 则循环获取剩下页数据
            int totalPageNum = totalRecord / pageSize;
            totalPageNum += totalRecord % pageSize == 0 ? 0 : 1;
            if (totalPageNum <= 1) {
                return;
            }
            // 第一页已经获取 去除
            for (int i = pageNo + 1; i < totalPageNum; i++) {
                int currPageNo = i;
                new Thread(() -> {
                    commonAePullOrder(startTime, endTime, currPageNo, pageSize, 0, aeGameMapList, aeRoomMapList);
                }).start();
            }
        } catch (Exception e) {
            logger.error("aelog aePullOrder error", e);
        }

    }

    /**
     * 公共获取记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageNo    页码
     */
    private int commonAePullOrder(String startTime, String endTime, int pageNo,
                                  int pageSize, int retryCount, Map<
            String, String> aeGameMapList, Map<Integer, String> aeRoomMapList) {
        Integer totalRecord = 0;
        long start = System.currentTimeMillis();
        try {
            String timestamp = System.currentTimeMillis() / 1000 + "";
            // 拼接参数
            TreeMap<String, String> trr = new TreeMap<>();
            trr.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
            trr.put("start_date", startTime);
            trr.put("end_date", endTime);
            trr.put("page_num", String.valueOf(pageNo));
            trr.put("per_num", String.valueOf(pageSize));
            trr.put("timestamp", timestamp);
            trr.put("nonce", RandomUtil.getRandomOne(6) + "");
            String sign = AEUtil.createSign(trr, OpenAPIProperties.AE_MD5_KEY);
            trr.put("signature", sign);

            // 获取游戏注单
            AeApiResponse result = commonRequest(trr, "", OpenAPIProperties.AE_API_URL_GAMERECORD_SEARCH, 0, "127.0.0.1", "commonAePullOrder");
            if (null != result && result.isSuccess()) {
                AeApiResponseData<JSONObject> aeApiResponseData = result.getData();
                JSONObject aeApiOrderResult = aeApiResponseData.getResult();
                JSONArray arrayList = JSONArray.parseArray(aeApiOrderResult.getString("list"));
                if (null == arrayList || arrayList.size() == 0) {
                    return 0;
                }
                List<AeBetOrder> aeBetOrderList = new ArrayList<>();
                AeBetOrderDTO aeBetOrder;
                Map<String, String> queueHashMap = new HashMap<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    JSONObject list = (JSONObject) arrayList.get(i);
                    aeBetOrder = new AeBetOrderDTO();
                    String uname = list.getString("uname");
                    String battleid = list.getString("battleid");
                    String strNumber = uname + battleid;
                    if (queueHashMap.containsKey(strNumber)) {
                        continue;
                    }

                    queueHashMap.put(strNumber, strNumber);
                    aeBetOrder.setOrderNo(list.getString("orderid"));
                    String aeName = list.getString("uname");
                    aeBetOrder.setUname(aeName);
                    aeBetOrder.setGameId(list.getString("gameid"));
                    aeBetOrder.setBattleId(list.getString("battleid"));
                    aeBetOrder.setRoomId(list.getInteger("roomid"));
                    aeBetOrder.setBet(list.getBigDecimal("bet"));
                    aeBetOrder.setAllbet(list.getBigDecimal("allbet"));
                    aeBetOrder.setProfit(list.getBigDecimal("profit"));
                    aeBetOrder.setRevenue(list.getBigDecimal("revenue"));
                    aeBetOrder.setStime(DateUtils.formatDate(DateUtils.getDate(list.getLong("stime")),DateUtils.newFormat));
                    aeBetOrder.setEtime(DateUtils.formatDate(DateUtils.getDate(list.getLong("etime")),DateUtils.newFormat));
                    aeBetOrder.setPlayernum(list.getInteger("playernum"));
                    aeBetOrder.setResult(list.getString("result"));
                    aeBetOrder.setChairId(list.getInteger("chairid"));
                    aeBetOrder.setXiqian(list.getBigDecimal("xiqian"));
                    aeBetOrder.setGameName(aeGameMapList.get(list.getString("gameid")));
                    aeBetOrder.setRoomName(aeRoomMapList.get(list.getInteger("roomid")));
                    aeBetOrder.setUserId((Integer.parseInt(aeName.substring(OpenAPIProperties.AE_USER_PREFIX.length()))));
                    aeBetOrder.setCreateTime(new Date());

                    aeBetOrderList.add(aeBetOrder);
                }

                if (aeBetOrderList.size() > 0) {
                    aebetOrderService.insertBatch(aeBetOrderList);
                    synOrderBetList(aebetOrderService.queryAeList());
                }
                // 获取总记录数
                totalRecord = aeApiOrderResult.getInteger("total_count");
            }
        } catch (Exception e) {
            logger.error("aelog commonAePullOrder error, startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, retryCount:{}", startTime, endTime, pageNo, pageSize, retryCount, e);
            if (retryCount >= Constants.AE_MAX_RETRY_COUNT) {
                return totalRecord;
            }
            //retry
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                logger.error("aelog commonAePullOrder retry sleep occur error, startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, retryCount:{}", startTime, endTime, pageNo, pageSize, retryCount, e);
            }
            commonAePullOrder(startTime, endTime, pageNo, pageSize, retryCount + 1, aeGameMapList, aeRoomMapList);
        }

        totalRecord = null == totalRecord || totalRecord < 0 ? 0 : totalRecord;
        long end = System.currentTimeMillis();
        logger.info("aelog commonAePullOrder end. startTime:{}, endTime:{}, pageNo:{}, pageSize:{}, totalRecord:{}, used times:{}ms", startTime, endTime, pageNo, pageSize, totalRecord, end - start);

        return totalRecord;
    }


    @Async
    public void synOrderBetList(List<AeBetOrder> queryAeList) {
        CptOpenMember aeGameBO;
        try {
            if (queryAeList.size() > 0) {
                for (AeBetOrder aeBetOrderDO : queryAeList) {
                    aeGameBO = new CptOpenMember();
                    aeGameBO.setUserId(aeBetOrderDO.getUserId());
                    gameCommonService.syncCodeSize(aeGameBO, Constants.AE_ACCOUNT_TYPE, "AE", GoldchangeEnum.AE_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("注单同步打码量异常", e);
        }
    }

    /**
     * 公共请求
     */
    @Override
    public AeApiResponse commonRequest(Map<String, String> paramsMap, String
            aeAccount, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("aelog {} commonRequest AE_AES_KEY:{},url:{},paramsMap:{}", userId, OpenAPIProperties.AE_AES_KEY, url, paramsMap);
        JSONObject sortParams = AEUtil.sortMap(paramsMap);
        String params = AEUtil.encrypt(sortParams.toString(), OpenAPIProperties.AE_AES_KEY);
        AeApiResponse aeApiResponse = null;
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotEmpty(aeAccount)) {
            map.put("username", aeAccount);
        }
        map.put("operator_code", OpenAPIProperties.AE_OPERATOR_CODE);
        String name = System.currentTimeMillis() / 1000 + "";
        map.put("timestamp", name);
        map.put("parameter", params);
        String resultString = AEUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, "http", url, map, type, userId, paramsMap);
        if (StringUtils.isNotEmpty(resultString)) {
            aeApiResponse = JSONObject.parseObject(resultString, AeApiResponse.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("aelog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, aeApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url,
                    OpenAPIProperties.PROXY_HOST_NAME, sortParams.toString(), resultString, JSONObject.toJSONString(aeApiResponse));
        }
        return aeApiResponse;
    }


    private Map<String, String> aeGameList() {
        Map<String, String> map = new HashMap<>();
        List<AeGame> list = RedisBusinessUtil.getAllAeGame();
        if (null == list) {
            LambdaQueryWrapper<AeGame> wrapper = new LambdaQueryWrapper();
            list = aeGameMapper.selectList(wrapper);
            //redisTemplate.opsForValue().set(RedisKeys.AE_GANE_KEY, list);
            RedisBaseUtil.set(RedisKeys.AE_GANE_KEY, list);
        }

        if (list.size() > 0) {
            for (AeGame aeGame : list) {
                map.put(aeGame.getGameId(), aeGame.getGameName());
            }
        }
        return map;
    }


    private Map<Integer, String> aeRoomList() {
        Map<Integer, String> map = new HashMap<>();
        List<AeRoom> list = RedisBusinessUtil.getAllAeRoom();
        if (null == list) {
            AeRoomExample example = new AeRoomExample();
            LambdaQueryWrapper<AeRoom> wrapper = new LambdaQueryWrapper();
            list = aeRoomMapper.selectList(wrapper);
            //redisTemplate.opsForValue().set(RedisKeys.AE_ROOM_KEY, list);
            RedisBaseUtil.set(RedisKeys.AE_ROOM_KEY, list);
        }

        if (list.size() > 0) {
            for (AeRoom aeRoom : list) {
                map.put(aeRoom.getRoomId(), aeRoom.getRoomName());
            }
        }
        return map;
    }
}
