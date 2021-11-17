package com.indo.game.services.ky.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.constant.RedisKeys;
import com.indo.common.exception.BadRequestException;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.CaipiaoTypeEnum;
import com.indo.game.common.enums.ChessBalanceTypeEnum;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.ky.KyKindMapper;
import com.indo.game.mapper.ky.KyServerMapper;
import com.indo.game.pojo.dto.KYNoteList;
import com.indo.game.pojo.dto.KYResultInfoDTO;
import com.indo.game.pojo.entity.*;
import com.indo.game.pojo.entity.ky.*;
import com.indo.game.services.*;
import com.indo.game.services.ky.KYService;
import com.indo.game.services.ky.KybetOrderService;
import com.indo.game.utils.KYUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 开元棋牌业务类
 */
@Service
public class KYServiceImpl implements KYService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenAPIProperties openAPIProperties;
    @Autowired
    private RedissonClient redissonClient;
    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private KybetOrderService kybetOrderService;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private KyKindMapper kyKindMapper;
    @Autowired
    private KyServerMapper kyServerMapper;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService cptOpenMemberService;
    @Autowired
    private SysChessBalanceService sysChessBalanceService;

    @Override
    public Result<String> kyGame(LoginInfo loginUser, String kindId, String ip) {
        logger.info("kylog {} kyGame：Come here numbers  account {}", loginUser.getId(), loginUser.getNickName());
        // 是否开售校验
        Integer lotteryId = Integer.parseInt(kindId);
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        //验证站点棋牌余额
        if (!sysChessBalanceService.isBalanceInChess(ChessBalanceTypeEnum.KY.getCode(), balance)) {
            logger.info("站点开元棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }
        String key = Constants.KY_ACCOUNT_TYPE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("kylog cyCallback[{}] ", loginUser.getId());
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.KY_ACCOUNT_TYPE + "_" + loginUser.getId() + kindId;
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            Result<String> result = null;
            if (bool) {
                String kyAccount = null;
                // 验证且绑定（KY-CPT第三方会员关系）
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.KY_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    cptOpenMember = new CptOpenMember();
                    kyAccount = openAPIProperties.platformUserPrefix + loginUser.getId();
                    cptOpenMember.setUserId(loginUser.getId().intValue());
                    cptOpenMember.setUsername(kyAccount);
                    cptOpenMember.setCreateTime(new Date());
                    cptOpenMember.setLoginTime(new Date());
                    cptOpenMember.setType(Constants.KY_ACCOUNT_TYPE);
                    externalService.saveCptOpenMember(cptOpenMember);
                } else {
                    kyAccount = cptOpenMember.getUsername();
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                }
                //代理端口
                String agent = OpenAPIProperties.KY_AGENT;
                String orderId = KYUtil.buildOrderId(agent, loginUser.getNickName());
                KYResultInfoDTO kyResultDTO = game(agent, kyAccount, "0", orderId, OpenAPIProperties.KY_API_URL, ip, OpenAPIProperties.KY_LINECODE, loginUser.getId().intValue(), kindId);
                logger.info("kylog {} kyGame conn result url:{}", loginUser.getId(), kyResultDTO.getD().getUrl());
                if (kyResultDTO != null && kyResultDTO.getD().getCode().equals(0)) {
                    result = new Result<String>();
                    result.setData(kyResultDTO.getD().getUrl() + "&backUrl=0&jumpType=1");
                }
            } else {
                logger.info("kylog {} kyGame exit no take lock. ", loginUser.getId());
            }
            return result;
        } catch (Exception e) {
            logger.error("kylog {} kyGame occur error.  account:{}, kindId:{}, ip:{}", loginUser.getId(), loginUser.getNickName(), kindId, ip, e);
            return Result.failed(MessageUtils.get("thesystemisbusy"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Async
    @Override
    public void initAccountInfo(LoginInfo loginUser, String ip, String kindId) {
        logger.info("kylog {} InitAccountInfo：Come here numbers  kindId {}", loginUser.getId(), kindId);
        // 是否开售校验
        Integer lotteryId = Integer.parseInt(kindId);
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return;
        }
        String key = CaipiaoTypeEnum.KY_GAME.getTagType() + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("ky initAccountInfo isLocked {} return thread", key);
            return;
        }

        String initKey = Constants.KY_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            /**
             * 自动转入转出（进入游戏查询用户在开元棋牌的可下分余额，有余额则先转出）
             */
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            if (bool) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.KY_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.error("kylog {} initAccountInfo  type {} is not find info", loginUser.getId(),
                            Constants.KY_ACCOUNT_TYPE);
                    return;
                }
                String agent = OpenAPIProperties.KY_AGENT;
                String apiUrl = OpenAPIProperties.KY_API_URL;
                Integer uid = cptOpenMember.getUserId();
                String account = cptOpenMember.getUsername();

                KYResultInfoDTO kyResultDTO = getBalance(agent, cptOpenMember.getUsername(), OpenAPIProperties.KY_API_URL, uid, ip);
                if (kyResultDTO != null && kyResultDTO.getD().getCode().equals(0) && kyResultDTO.getD().getMoney().compareTo(BigDecimal.ZERO) == 1) {
                    // 自动下分
                    autoXiaFen(loginUser, agent, apiUrl, cptOpenMember, account, kyResultDTO, ip);
                }

                // 自动上分
                autoSF(loginUser, agent, apiUrl, cptOpenMember, account, ip);

                //标识有进入游戏
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_IN);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            } else {
                logger.info("kylog {} exit no take lock.", loginUser.getId());
            }
        } catch (Exception e) {
            logger.error("kylog {} initAccountInfo occur error.  kindId:{}, ip:{}", loginUser.getId(), kindId, ip, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 上分流程代码
     */
    private void autoSF(LoginInfo loginUser, String agent, String apiUrl, CptOpenMember cptOpenMember, String account, String ip) {
        Long userId = loginUser.getId();
        Integer uid = cptOpenMember.getUserId();
        try {
            // 自动上分
            boolean flagSF = true;
            String orderId = KYUtil.buildOrderId(agent, account);
            MemBaseinfo xiazhuren = memBaseinfoService.getUserByAccno(loginUser.getAccount());
            if (xiazhuren == null) {
                logger.error("kylog {}  autoSF appMember is null", userId);
                throw new BadRequestException(MessageUtils.get("tbdne"));
            }
            if (xiazhuren.getGoldnum().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("kylog {} autoSF appMember balance {} is lt=; zero" + xiazhuren.getGoldnum(), userId);
                throw new BadRequestException(MessageUtils.get("ibpr"));
            }

            //先扣款
            String money = String.valueOf(xiazhuren.getGoldnum());
            //验证站点棋牌余额
            if (!verificationBalanceInChess(ChessBalanceTypeEnum.KY.getCode(), new BigDecimal(money), loginUser)) {
                logger.info("站点开元棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), new BigDecimal(money));
                //站点棋牌余额不足
                throw new BadRequestException(MessageUtils.get("tcgqifpccs"));
            }

            gameCommonService.inOrOutBalanceCommon(-1, xiazhuren.getGoldnum(),
                    xiazhuren, openAPIProperties.platformName +  MessageUtils.get("btotk") + "/" + orderId,
                    cptOpenMember, Constants.KY_ACCOUNT_TYPE);

            logger.info("kylog {} autoSF： orderId {} ", userId, orderId);
            KYResultInfoDTO sfInfo = sf(agent, account, money, orderId, apiUrl, uid, ip);
            // 如果接口无返回，检测此上分订单
            logger.info("kylog {} autoSF：{} userId orderId {} sfInfo{} ", userId, orderId, JSONObject.toJSONString(sfInfo));
            if (sfInfo == null) {
                KYResultInfoDTO orderDTO;
                int checkOrderNum = 0;
                while (checkOrderNum <= 3) {
                    // 查询订单状态（-1:不存在、0:成功、2:失败、3:处理中）
                    orderDTO = getOrder(agent, orderId, apiUrl, uid, ip);
                    //返回异常为空则继续请求
                    if (null == orderDTO) {
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    }
                    // 订单处理成功
                    if (orderDTO != null && orderDTO.getD().getCode().equals(0) && orderDTO.getD().getStatus().equals(0)) {
                        logger.info("kylog {} autoSF order susses");
                        break;
                    }
                    // 订单处理失败
                    if (orderDTO != null && orderDTO.getD().getCode().equals(0) && orderDTO.getD().getStatus().equals(2)) {
                        logger.info("kylog {} autoSF order handing fail");
                        // KY转出到CPT
                        gameCommonService.inOrOutBalanceCommon(1, new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP),
                                xiazhuren, MessageUtils.get("kcbtot") + openAPIProperties.platformName + "/" + orderId,
                                cptOpenMember, Constants.KY_ACCOUNT_TYPE);
                        //站点棋牌余额加回去
                        sysChessBalanceService.addBalance(ChessBalanceTypeEnum.KY.getCode(), new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP));
                        break;
                    }
                    // 订单不存在
                    if (orderDTO != null && orderDTO.getD().getCode().equals(0)
                            && orderDTO.getD().getStatus().equals(-1)) {
                        logger.info("kylog {} autoSF order handing fail");
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    }
                    // 订单处理中2S调用查询接口，直到订单状态为成功或者失败
                    if (orderDTO != null && orderDTO.getD().getCode().equals(0)
                            && orderDTO.getD().getStatus().equals(3)) {
                        logger.info("kylog {} autoSF order handing fail");
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    }

                }
            } else {
                // 上分失败
                if (!sfInfo.getD().getCode().equals(0)) {
                    // KY转出到CPT
                    gameCommonService.inOrOutBalanceCommon(1, new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP),
                            xiazhuren, MessageUtils.get("kcbtot") + openAPIProperties.platformName + "/" + orderId,
                            cptOpenMember, Constants.KY_ACCOUNT_TYPE);
                    //站点棋牌余额加回去
                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.KY.getCode(), new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP));
                }

            }
        } catch (Exception e) {
            logger.error("kylog {} autoSF occur error.  account:{}, url:{}, agent:{}, uid:{}, ip:{}", userId, account, apiUrl, agent, uid, ip, e);
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
        //当前该站点要进入KY棋牌所有KY用户共享锁 公平锁
        //RedisLock lock = new RedisLock(chessCode + "_GAME_CHESS_BALANCE", 5000, 5000);
        RLock lock = redissonClient.getFairLock(chessCode + "_GAME_CHESS_BALANCE");
        //检查该站点购买的ky棋牌余额是否够 该用户上分
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
            logger.error("kylog {} InitAccountInfo-> verificationBalanceInChess  error:{} ", loginUser.getId(), e.getMessage());
            throw new BadRequestException(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * 下分流程代码
     */
    private void autoXiaFen(LoginInfo loginUser, String agent, String apiUrl, CptOpenMember cptOpenMember, String account,
                            KYResultInfoDTO kyResultDTO, String ip) throws Exception, InterruptedException {
        String kyXFDTO = JSONObject.toJSONString(kyResultDTO);
        Long userId = loginUser.getId();
        Integer uid = cptOpenMember.getUserId();
        MemBaseinfo memBaseinfo = memBaseinfoService.getUserByAccno(loginUser.getAccount());
        if (null == memBaseinfo) {
            throw new RuntimeException(MessageUtils.get("user")+"：" + cptOpenMember.getUserId() + MessageUtils.get("doesnotexist"));
        }
        String orderId = KYUtil.buildOrderId(agent, account);
        long start = System.currentTimeMillis();

        //最大重试次数
        int maxRetry = 3;
        // 下分
        String money = String.valueOf(kyResultDTO.getD().getMoney());
        logger.info("kylog {} autoXiaFen  orderId {}  money {} ", uid, orderId, money);
        KYResultInfoDTO xfInfo = xiafen(agent, account, money, orderId, apiUrl, uid, ip);
        logger.info("kylog {} autoXiaFen return orderId{} KYResultInfoDTO {}", uid, orderId, JSONObject.toJSONString(xfInfo));
        // 如果接口无返回，检测此下分订单
        if (xfInfo == null) {
            logger.info("kylog {} error autoXiaFen xifen orderId{} KYResultInfoDTO {}", uid, orderId, JSONObject.toJSONString(xfInfo));
            KYResultInfoDTO orderDTO;
            int checkOrderNum = 0;
            while (checkOrderNum <= maxRetry) {
                // 查询订单状态（-1:不存在、0:成功、2:失败、3:处理中）
                logger.info("kylog {} autoXiaFen error  orderId{} ", uid, orderId);
                orderDTO = getOrder(agent, orderId, apiUrl, uid, ip);
                logger.info("kylog {}  autoXiaFen getOrder return orderId{} orderDTO {}", uid, orderId, JSONObject.toJSONString(orderDTO));
                // 订单处理成功
                if (orderDTO != null && orderDTO.isSuccess()) {
                    // KY转出到CPT
                    gameCommonService.inOrOutBalanceCommon(1, new BigDecimal(money), memBaseinfo, MessageUtils.get("kcbtot") +
                                    openAPIProperties.platformName + "/" + orderId,
                            cptOpenMember, Constants.KY_ACCOUNT_TYPE);
                    //站点棋牌余额加回去
                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.KY.getCode(),
                            new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                }
                // 订单处理失败
                if (orderDTO != null && orderDTO.isFailure()) {
                    break;
                }
                // 订单不存在（每5S查询一次，3次后仍然没有，就再次用此订单号下分）
                if (orderDTO != null && orderDTO.isOrderNotExist()) {
                    logger.info("kylog {} autoXiaFen order not exist, sleep retry.  account:{}, ip:{}, kyXFDTO:{}", userId, account, ip, kyXFDTO);
                    checkOrderNum++;
                    TimeUnit.SECONDS.sleep(2);
                }
                // 订单处理中5S调用查询接口，直到订单状态为成功或者失败
                if (orderDTO != null && orderDTO.isOrderHanding()) {
                    logger.info("kylog {}, used  ms", uid);
                    checkOrderNum++;
                    TimeUnit.SECONDS.sleep(2);
                }
            }
        } else {
            logger.info("kylog {} autoXiaFen ,orderId:{}  KYResultInfoDTO {}", uid, orderId, JSONObject.toJSONString(xfInfo));
            // 正常下分
            if (xfInfo.getD().getCode().equals(0)) {
                gameCommonService.inOrOutBalanceCommon(1, new BigDecimal(money), memBaseinfo, MessageUtils.get("kcbtot") +
                                openAPIProperties.platformName + "/" + orderId,
                        cptOpenMember, Constants.KY_ACCOUNT_TYPE);
                //站点棋牌余额加回去
                sysChessBalanceService.addBalance(ChessBalanceTypeEnum.KY.getCode(),
                        new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        long end = System.currentTimeMillis();
        logger.info("kylog {} autoXiaFen  account:{}, ip:{}, kyXFDTO:{} used times:{}", orderId, userId, account, ip, kyXFDTO, end - start);
    }

    /**
     * 统一请求
     *
     * @return String
     */
    @Override
    public KYResultInfoDTO get(String url, Integer uid, String method, String ip, boolean isLog, String type) {
        KYResultInfoDTO kyResultDTO = null;
        try {
            String responseInfo = KYUtil.httpGet(url, OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, type);
            System.out.println("开元返回:"+responseInfo);
            kyResultDTO = JSONObject.parseObject(responseInfo, KYResultInfoDTO.class);
            // 记录日志
            logger.info("kylog {} get kylog  method:{},url:{},responseInfo:{},ip:{},KY_ACCOUNT_TYPE:{}", uid, method, url,
                    responseInfo, ip, Constants.KY_ACCOUNT_TYPE);
        } catch (Exception e) {
            logger.error("kylog {} get  ky invoking kyResultDTO:{} ,exception:{} ", uid, kyResultDTO, e);
            return kyResultDTO;
        }
        logger.info("kylog {} get  result  KYResultInfoDTO {}", uid, kyResultDTO);
        return kyResultDTO;
    }

    /**
     * 进入游戏接口
     *
     * @param agent    代理帐号
     * @param account  帐号
     * @param money    余额
     * @param orderId  订单号
     * @param apiUrl   api接口URL
     * @param ip       请求IP地址
     * @param lineCode lineCode
     * @return 结果
     */
    public KYResultInfoDTO game(String agent, String account, String money, String orderId, String apiUrl, String ip,
                                String lineCode, Integer uid, String KindID) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=0&account=" + account + "&money=" + money + "&orderid=" + orderId + "&ip=" + ip
                + "&lineCode=" + lineCode;
        if (StringUtils.isNotBlank(KindID)) {
            params += "&KindID=" + KindID;
        }
        System.out.println("开元请求连接:"+params.trim());
        String param = KYUtil.AESEncrypt(params.trim(), OpenAPIProperties.KY_DES_KEY);
        logger.info("kyInit param  {}", param);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        StringBuilder builder = new StringBuilder();
        String postUrl = builder.append(apiUrl).append("?agent=").append(agent).append("&timestamp=").append(time)
                .append("&param=").append(param).append("&key=").append(key).append("&backUrl=0").append("&jumpType=1")
                .toString();

        return get(postUrl, uid, "0", ip, true, "game");
    }

    /**
     * 查询可下分余额
     *
     * @param agent   代理编号
     * @param account 会员帐号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO getBalance(String agent, String account, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=1&account=" + account;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "1", ip, true, "getBalance");
    }

    /**
     * 上分接口
     *
     * @param agent   代理帐号
     * @param account 帐号
     * @param money   余额
     * @param orderid 订单号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO sf(String agent, String account, String money, String orderid, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=2&account=" + account + "&money=" + money + "&orderid=" + orderid;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "2", ip, true, "sf");
    }

    /**
     * 下分接口
     *
     * @param agent   代理帐号
     * @param account 帐号
     * @param money   余额
     * @param orderid 订单号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO xiafen(String agent, String account, String money, String orderid, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=3&account=" + account + "&money=" + money + "&orderid=" + orderid;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "3", ip, true, "xiafen");
    }

    /**
     * 订单查询
     *
     * @param agent   代理帐号
     * @param orderid 订单号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO getOrder(String agent, String orderid, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=4&orderid=" + orderid;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "4", ip, true, "getOrder");
    }

    /**
     * 玩家是否在线查询
     *
     * @param agent   代理帐号
     * @param account 帐号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO getState(String agent, String account, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=5&account=" + account;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "5", ip, true, "getState");
    }

    /**
     * 获取游戏结果数据接口
     *
     * @param agent     代理帐号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param apiUrl    api接口URL
     * @return 结果
     */
    public KYResultInfoDTO getRecord(String agent, String startTime, String endTime, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=6&startTime=" + startTime + "&endTime=" + endTime;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "6", ip, false, "getRecord");
    }

    /**
     * 查询玩家总分
     *
     * @param agent   代理帐号
     * @param account 帐号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO getAllBalance(String agent, String account, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=7&account=" + account;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "7", ip, true, "getAllBalance");
    }

    /**
     * 踢玩家下线
     *
     * @param agent   代理帐号
     * @param account 帐号
     * @param apiUrl  API接口
     */
    public KYResultInfoDTO kick(String agent, String account, String apiUrl, Integer uid, String ip) throws Exception {
        String time = System.currentTimeMillis() + "";
        String params = "s=8&account=" + account;
        String param = KYUtil.AESEncrypt(params, OpenAPIProperties.KY_DES_KEY);
        String key = KYUtil.MD5(agent + time + OpenAPIProperties.KY_MD5_KEY);
        String postUrl = apiUrl.concat("?agent=").concat(agent).concat("&timestamp=").concat(time).concat("&param=").concat(param).concat("&key=").concat(key);
        return get(postUrl, uid, "8", ip, true, "kick");
    }

    /**
     * 下分
     *
     * @param ip 用户请求的ip
     */
    @Override
    public Result<String> exit(LoginInfo loginUser, String ip) {
        logger.info("kylog {} exit   account{} ip {}", loginUser.getId(), loginUser.getNickName(), ip);
        String initKey = Constants.KY_ACCOUNT_TYPE + "_" + loginUser.getId() + loginUser.getNickName();
        RedisLock lock = new RedisLock(initKey, 0, 3000);
        try {
            if (lock.lock()) {
                // 查询用户
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.KY_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.error("kylog {} exit exitcptOpenMember is null {}", loginUser.getId(), cptOpenMember);
                    return Result.success();
                }
                //代理端口
                String agent = OpenAPIProperties.KY_AGENT;
                //url地址
                String apiUrl = OpenAPIProperties.KY_API_URL;
                Integer uid = cptOpenMember.getUserId();
                String kyAccount = cptOpenMember.getUsername();

                //查询可下分余额，走代理
                KYResultInfoDTO kyResultDTO = getBalance(agent, cptOpenMember.getUsername(), OpenAPIProperties.KY_API_URL, uid, ip);
                logger.info("kylog {} exit kyResultDTO {}", uid, JSONObject.toJSONString(kyResultDTO));
                if (kyResultDTO != null && kyResultDTO.getD().getCode().equals(0) && kyResultDTO.getD().getMoney().compareTo(BigDecimal.ZERO) == 1) {
                    // 自动下分
                    autoXiaFen(loginUser, agent, apiUrl, cptOpenMember, kyAccount, kyResultDTO, ip);

                    //标识游戏下分
                    CptOpenMember gameOpenMember = new CptOpenMember();
                    gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
                    gameOpenMember.setId(cptOpenMember.getId());
                    cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
                    return Result.success();
                }
            } else {
                logger.info("kylog {} exit no take lock. ", loginUser.getId());
            }
            return Result.success();
        } catch (Exception e) {
            logger.error("kylog {} exit occur error.  account:{}, ip:{}", loginUser.getId(), loginUser.getNickName(), ip, e);
            return Result.success();
        } finally {
            //主动下分的让其自然过期，过期时间内的重复请求直接丢弃
        }
    }


    /**
     * 注单是以游戏派奖时间为准；拉取当前时间 1 分钟之前 数据；建议拉取区间为 1-5 分钟，最大不能超过 60 分钟。我方注单每 30 秒更新一次，建议 每隔至少 30 秒拉取一次，因为
     * 30 秒内多次拉取的注单也是重复注单。并强制限制至少每隔 10 秒才能拉取一次注单。）
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void pullKYBetOrder() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -3);
            calendar.set(Calendar.SECOND, 0);
            String startTime = String.valueOf(calendar.getTimeInMillis());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MINUTE, 1);
            calendar1.set(Calendar.SECOND, 0);
            String endTime = String.valueOf(calendar1.getTimeInMillis());

            Calendar thisCalendar = Calendar.getInstance();
            thisCalendar.set(Calendar.SECOND, 0);
            //redisTemplate.opsForHash().put("KY_ORDER_SELECT",
            RedisBaseUtil.hSet("KY_ORDER_SELECT",
                    DateUtils.formatDate(thisCalendar.getTime(), DateUtils.newFormat), 1);
            KYResultInfoDTO record = getRecord(OpenAPIProperties.KY_AGENT, startTime, endTime, OpenAPIProperties.KY_API_GET_RECORDURL, 0, "ky_system_task");
            if (record != null && record.getD().getCode().equals(0)) {
                Map<Integer, String> kindListMap = kyKindList();
                Map<Integer, String> serverListMap = kyServerList();
                List<KyBetOrder> betOrderList = new ArrayList<KyBetOrder>();
                List<KYNoteList> noteLists = record.getD().getList();
                Map<String, String> queueHashMap = new HashMap<String, String>();
                noteLists.forEach(note -> {
                    String[] accounts = note.getAccounts();
                    for (int i = 0; i < accounts.length; i++) {
                        KyBetOrder betOrder = new KyBetOrder();
                        String gameID = note.getGameID()[i];
                        String account = note.getAccounts()[i];
                        String strNumber = account + gameID;
                        if (queueHashMap.containsKey(strNumber)) {
                            continue;
                        } else {
                            queueHashMap.put(account + gameID, gameID);
                        }
                        betOrder.setGameId(gameID);
                        betOrder.setCreateTime(new Date());
                        betOrder.setAccount(account);
                        betOrder.setAllBet(note.getAllBet()[i]);
                        betOrder.setProfit(note.getProfit()[i]);
                        betOrder.setRevenue(note.getRevenue()[i]);
                        betOrder.setKindId(note.getKindID()[i]);
                        betOrder.setChairId(note.getChairID()[i]);
                        betOrder.setTableId(note.getTableID()[i]);
                        betOrder.setLineCode(note.getLineCode()[i]);
                        betOrder.setServerId(note.getServerID()[i]);
                        betOrder.setCardValue(note.getCardValue()[i]);
                        betOrder.setChannelId(note.getChannelID()[i]);
                        betOrder.setCellScore(note.getCellScore()[i]);
                        betOrder.setUserCount(note.getUserCount()[i]);
                        betOrder.setGameStartTime(note.getGameStartTime()[i]);
                        betOrder.setGameEndTime(note.getGameEndTime()[i]);
                        betOrder.setKindName(kindListMap.get(note.getKindID()[i]));
                        betOrder.setServerName(serverListMap.get(note.getServerID()[i]));
                        betOrder.setUserId(Integer.parseInt(account.substring(account.lastIndexOf("V") + 1)));
                        betOrderList.add(betOrder);
                    }
                });
                logger.info("kylog pullKYBetOrder betOrderList {}", betOrderList);
                if (betOrderList.size() > 0) {
                    kybetOrderService.insertBatch(betOrderList);
                }
            }
        } catch (Exception e) {
            logger.error("kylog pullKYBetOrder occur error.", e);
        }
    }

    /**
     * 同步平常漏掉的数据
     */
    @Transactional
    @Override
    public void pullKYBetOrderOfMissing() throws ParseException {
        for (int j = 2; j <= 60; j++) {
            try {
                Calendar thisCalendar = Calendar.getInstance();
                thisCalendar.set(Calendar.SECOND, 0);
                thisCalendar.add(Calendar.MINUTE, -1 * j);
                //Object value = redisTemplate.opsForHash().get("KY_ORDER_SELECT",
                Object value = RedisBaseUtil.hGet("KY_ORDER_SELECT",
                        DateUtils.formatDate(thisCalendar.getTime(), DateUtils.newFormat));
                if (value != null) {
                    continue;
                } else {
                    //redisTemplate.opsForHash().put("KY_ORDER_SELECT",
                    RedisBaseUtil.hSet("KY_ORDER_SELECT",
                            DateUtils.formatDate(thisCalendar.getTime(), DateUtils.newFormat), 1);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MINUTE, -1 * j);
                String endTime = String.valueOf(calendar.getTimeInMillis());
                calendar.add(Calendar.MINUTE, -1 * j + 1);
                String startTime = String.valueOf(calendar.getTimeInMillis());
                logger.info("kylog pullKYBetOrderOfMissing 同步平常漏掉的数据参数信息：{},{}", startTime, endTime);
                KYResultInfoDTO record = getRecord(OpenAPIProperties.KY_AGENT, startTime, endTime, OpenAPIProperties.KY_API_GET_RECORDURL, 0, "ky_system_task");
                logger.info("kylog pullKYBetOrderOfMissing 同步平常漏掉的数据参数信息 返回信息 record {}", record);
                if (record != null && record.getD().getCode().equals(0)) {
                    Map<Integer, String> kindListMap = kyKindList();
                    Map<Integer, String> serverListMap = kyServerList();
                    List<KYNoteList> noteLists = record.getD().getList();
                    List<KyBetOrder> betOrderList = new ArrayList<KyBetOrder>();

                    Map<String, String> queueHashMap = new HashMap<String, String>();
                    noteLists.forEach(note -> {
                        String[] accounts = note.getAccounts();
                        for (int i = 0; i < accounts.length; i++) {
                            KyBetOrder betOrder = new KyBetOrder();
                            String account = note.getAccounts()[i];
                            String gameID = note.getGameID()[i];
                            String strNumber = account + gameID;
                            if (queueHashMap.containsKey(strNumber)) {
                                continue;
                            } else {
                                queueHashMap.put(account + gameID, gameID);
                            }
                            betOrder.setGameId(gameID);
                            betOrder.setAccount(account);
                            betOrder.setCreateTime(new Date());
                            betOrder.setAllBet(note.getAllBet()[i]);
                            betOrder.setKindId(note.getKindID()[i]);
                            betOrder.setProfit(note.getProfit()[i]);
                            betOrder.setRevenue(note.getRevenue()[i]);
                            betOrder.setTableId(note.getTableID()[i]);
                            betOrder.setChairId(note.getChairID()[i]);
                            betOrder.setLineCode(note.getLineCode()[i]);
                            betOrder.setServerId(note.getServerID()[i]);
                            betOrder.setCardValue(note.getCardValue()[i]);
                            betOrder.setCellScore(note.getCellScore()[i]);
                            betOrder.setChannelId(note.getChannelID()[i]);
                            betOrder.setUserCount(note.getUserCount()[i]);
                            betOrder.setGameEndTime(note.getGameEndTime()[i]);
                            betOrder.setGameStartTime(note.getGameStartTime()[i]);
                            betOrder.setKindName(kindListMap.get(note.getKindID()[i]));
                            betOrder.setServerName(serverListMap.get(note.getServerID()[i]));
                            betOrder.setUserId(Integer.parseInt(account.substring(account.lastIndexOf("V") + 1)));
                            betOrderList.add(betOrder);
                        }
                    });
                    logger.info("betOrderList {}", betOrderList);
                    if (betOrderList.size() > 0) {
                        kybetOrderService.insertBatch(betOrderList);
                        synKyOrderList(kybetOrderService.queryKyOrderList());
                    }
                }
            } catch (Exception e) {
                logger.error("pullKYBetOrder occur error.", e);
            }
        }

        //Map<String, String> redisMap = redisTemplate.opsForHash().entries("KY_ORDER_SELECT");
        Map<String, String> redisMap = RedisBaseUtil.hEntries("KY_ORDER_SELECT");
        List<String> dateList = new ArrayList<>();
        for (String key : redisMap.keySet()) {
            Date date = DateUtils.parseDateNoTime(key, DateUtils.newFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            if (date.getTime() < calendar.getTime().getTime()) {
                dateList.add(key);
            }
        }
        String[] dateArray = new String[dateList.size()];
        dateList.toArray(dateArray);
        //redisTemplate.opsForHash().delete("KY_ORDER_SELECT", dateArray);
        RedisBaseUtil.hDelete("KY_ORDER_SELECT", dateArray);

    }


    @Async
    public void synKyOrderList(List<KyBetOrder> queryKyOrderList) {
        try {
            if (queryKyOrderList.size() > 0) {
               CptOpenMember kyGameBO;
                for (KyBetOrder kyBetOrderDO : queryKyOrderList) {
                    kyGameBO = new CptOpenMember();
                    kyGameBO.setUserId(kyBetOrderDO.getUserId().intValue());
                    //同步打码量
                    gameCommonService.syncCodeSize(kyGameBO, Constants.KY_ACCOUNT_TYPE, "KY", GoldchangeEnum.KY_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("KY同步打码量异常", e);
        }
    }

    private Map<Integer, String> kyKindList() {
        Map<Integer, String> map = new HashMap<>();
        List<KyKind> list = RedisBusinessUtil.getAllKyKind();
        if (null == list) {
            KyKindExample example = new KyKindExample();
            list = kyKindMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.KY_KIND_KEY, list);
            RedisBaseUtil.set(RedisKeys.KY_KIND_KEY, list);
        }

        if (list.size() > 0) {
            for (KyKind kind : list) {
                map.put(kind.getKindId(), kind.getKindName());
            }
        }
        return map;
    }


    private Map<Integer, String> kyServerList() {
        Map<Integer, String> map = new HashMap<>();
        List<KyServer> list = RedisBusinessUtil.getAllKyServer();
        if (null == list) {
            KyServerExample example = new KyServerExample();
            list = kyServerMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.KY_SERVER_KEY, list);
            RedisBaseUtil.set(RedisKeys.KY_SERVER_KEY, list);
        }
        if (list.size() > 0) {
            for (KyServer kyServer : list) {
                map.put(kyServer.getServerId(), kyServer.getServerName());
            }
        }
        return map;
    }

}
