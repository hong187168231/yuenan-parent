package com.indo.game.services.mg.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.indo.common.constant.RedisKeys;
import com.indo.common.exception.BadRequestException;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.common.enums.CaipiaoTypeEnum;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.ChessBalanceTypeEnum;
import com.indo.game.common.enums.CurEnum;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.mg.MgGameMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.MemBaseinfo;
import com.indo.game.pojo.entity.mg.MgBetOrder;
import com.indo.game.pojo.entity.mg.MgGame;
import com.indo.game.pojo.entity.mg.MgGameExample;
import com.indo.game.services.*;
import com.indo.game.services.mg.MgService;
import com.indo.game.services.mg.MgbetOrderService;
import com.indo.game.utils.MgUtil;
import com.indo.game.utils.SnowflakeIdWorker;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MgServiceImpl implements MgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedissonClient redissonClient;
    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private OpenAPIProperties openAPIProperties;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private MgbetOrderService mgbetOrderService;
    @Autowired
    private MgGameMapper mgGameMapper;
    @Autowired
    private CptOpenMemberService cptOpenMemberService;
    @Autowired
    private SysChessBalanceService sysChessBalanceService;


    @Override
    public Result<String> mgGame(LoginInfo loginUser, Integer gameCode) {
        logger.info("mglog {} mgGame account:{}, mgCodeId:{}", loginUser.getId(), loginUser.getNickName(), gameCode);
        // 是否开售校验
        Map<Integer, String> map = mgGameList();
        if (!map.containsKey(gameCode)) {
            logger.info("没有此游戏配置Code{}", gameCode);
            return Result.failed(MessageUtils.get("tgdne"));
        }
        if (!gameCommonService.isGameEnabled(CaipiaoTypeEnum.MG_GAME.getTagType())) {
            logger.info("此游戏禁用 Code{}", gameCode);
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        //验证站点棋牌余额
        if (!sysChessBalanceService.isBalanceInChess(ChessBalanceTypeEnum.MG.getCode(), balance)) {
            logger.info("站点MG棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }
        String key = Constants.MG_ACCOUNT_TYPE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("mglog cyCallback[{}] ", loginUser.getId());
            return Result.failed(MessageUtils.get("sdctf"));
        } //else {
        //RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        //}

        String initKey = Constants.MG_INIT_KEY + loginUser.getId() + gameCode;
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            //缺少检查是否开启

            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            Result<String> result = null;
            if (bool) {
                // 验证且绑定（KY-CPT第三方会员关系）
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.MG_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    cptOpenMember = new CptOpenMember();
                    String account = openAPIProperties.platformUserPrefix + loginUser.getId();
                    cptOpenMember.setUsername(account);
                    JSONObject createResult = gameCreate(loginUser, cptOpenMember);
                    if (null != createResult && null != createResult.get("data")) {

                        cptOpenMember.setUserId(loginUser.getId().intValue());
                        cptOpenMember.setCreateTime(new Date());
                        cptOpenMember.setLoginTime(new Date());
                        cptOpenMember.setType(Constants.MG_ACCOUNT_TYPE);
                        externalService.saveCptOpenMember(cptOpenMember);
                    }
                } else {
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                }

                //代理端口
                JSONObject resultLaunch = gameLaunch(loginUser, cptOpenMember, map.get(gameCode));
                if (null != resultLaunch && null != resultLaunch.get("data")) {
                    result = new Result<String>();
                    result.setData(resultLaunch.getJSONObject("data").getString("game_url"));
                }
            } else {
                logger.info("mglog {} mgGame exit no take lock. ", loginUser.getId());
                return result;
            }
            return result;
        } catch (Exception e) {
            logger.error("mglog {} mgGame occur error.  account:{}, GameCode:{}, ip:{}", loginUser.getId(), loginUser.getNickName(), gameCode, e);
            return Result.failed(MessageUtils.get("thesystemisbusy"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Async
    @Override
    public void initAccountInfo(LoginInfo loginUser, Integer gameCode) {
        Map<Integer, String> map = mgGameList();
        if (!map.containsKey(gameCode)) {
            logger.info("没有此游戏配置Code{}", gameCode);
            return;
        }
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(CaipiaoTypeEnum.MG_GAME.getTagType())) {
            logger.info("此游戏禁用 Code{}", gameCode);
            return;
        }

        String key = Constants.MG_INIT_TIME_KEY + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("mglog initAccountInfo isLocked {} return thread", key);
            return;
        } //else {
        // RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        //}
        String initKey = Constants.KY_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            /**
             * 自动转入转出（进入游戏查询用户在开元棋牌的可下分余额，有余额则先转出）
             */
            boolean bool = lock.writeLock().tryLock(10, 30, TimeUnit.SECONDS);
            if (bool) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.MG_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.error("mglog {} initAccountInfo  type {} is not find info", loginUser.getId(),
                            Constants.MG_ACCOUNT_TYPE);
                    return;
                }
                // 自动上分
                autoSF(loginUser, cptOpenMember);

                //标识有进入游戏
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_IN);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            } else {
                logger.info("kylog {} exit no take lock.", loginUser.getId());
            }

        } catch (Exception e) {
            logger.error("mglog {} initAccountInfo error", loginUser.getId(), e);
        } finally {
            lock.writeLock().unlock();
        }

    }

    private void autoSF(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        try {
            MemBaseinfo xiazhuren = memBaseinfoService.getUserByAccno(loginUser.getAccount());
            if (xiazhuren == null) {
                logger.error("mglog {}  autoSF appMember is null", loginUser.getId());
                return;
            }
            if (xiazhuren.getGoldnum().compareTo(BigDecimal.ZERO) < 1) {
                logger.error("mglog {} autoSF appMember balance {} is lt=; zero", xiazhuren.getGoldnum(), loginUser.getId());
                return;
            }
            BigDecimal balance = xiazhuren.getGoldnum();
            //验证站点棋牌余额
            if (!verificationBalanceInChess(ChessBalanceTypeEnum.MG.getCode(), balance, loginUser)) {
                logger.info("站点MG棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
                //站点棋牌余额不足
                throw new BadRequestException(MessageUtils.get("tcgqifpccs"));
            }
            //生成单号
            String orderNo = Constants.GAME_MG_SF + SnowflakeIdWorker.createOrderSn();
            //先扣款
            String remark = OpenAPIProperties.PLATFORM_NAME + MessageUtils.get("btotm") + "/" + orderNo;
            gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.MG_OUT.getValue(), balance, xiazhuren, remark,
                    cptOpenMember, Constants.MG_ACCOUNT_TYPE);

            JSONObject result = gameTransferIn(orderNo, balance, loginUser, cptOpenMember);
            if (null == result || null == result.get("data")) {
                int checkOrderNum = 0;
                while (checkOrderNum <= 3) {
                    JSONObject orderResult = gamePlayerGet(orderNo, loginUser, cptOpenMember);
                    if (null != orderResult) {
                        if (null != orderResult.get("data") && StringUtils.isNotEmpty(orderResult.getString("error"))) {
                            String content = MessageUtils.get("mbtot") + OpenAPIProperties.PLATFORM_NAME + orderNo;
                            gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.MG_IN.getValue(), balance, xiazhuren, content,
                                    cptOpenMember, Constants.MG_ACCOUNT_TYPE);
                            //站点棋牌余额加回去
                            sysChessBalanceService.addBalance(ChessBalanceTypeEnum.MG.getCode(), balance);
                            break;
                        } else {
                            checkOrderNum++;
                            TimeUnit.SECONDS.sleep(2);
                        }
                    } else {
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("mglog {} initAccountInfo error", loginUser.getId(), e);
        }

    }

    /**
     * 验证站点棋牌余额是否足够
     */
    private boolean verificationBalanceInChess(String chessCode, BigDecimal balance, LoginInfo loginUser) {
        //当前该站点要进入MG棋牌所有MG用户共享锁 公平锁
        //RedisLock lock = new RedisLock(chessCode + "_GAME_CHESS_BALANCE", 5000, 5000);
        RLock lock = redissonClient.getFairLock(chessCode + "_GAME_CHESS_BALANCE");
        //检查该站点购买的MG棋牌余额是否够 该用户上分
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
            logger.error("mglog {} initAccountInfo-> verificationBalanceInChess  error:{} ", loginUser.getId(), e.getMessage());
            throw new BadRequestException(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * 下分
     *
     * @param ip 用户请求的ip
     */
    @Override
    public Result<String> mgExit(LoginInfo loginUser, String ip) {

        logger.info("mglog {} exit   account{} ip {}", loginUser.getId(), loginUser.getNickName(), ip);
        String key = Constants.MG_EXIT_TIME_KEY + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("mglog aeSignOut isLocked {} return thread", key);
            return Result.success();
        } //else {
        //RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        //}

        RedisLock lock = new RedisLock(Constants.MG_EXIT_KEY + loginUser.getId(), 0, 3000);
        try {
            if (lock.lock()) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.MG_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.info("mglog {} mgSignOut exit cptOpenMember is null ", loginUser.getId());
                    return Result.success();
                }

                Result<String> aeSignOut = mgSignOut(loginUser, cptOpenMember, ip);

                //标识游戏下分
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
                return aeSignOut;

            } else {
                logger.info("mgExit {} aeSignOut request repeat, ip:{}", loginUser.getId(), ip);
                return Result.success();
            }
        } catch (Exception e) {
            logger.info("mgExit {} aeSignOut  ip:{}", loginUser.getId(), ip, e);
            return Result.success();
        } finally {
            //主动下分的让其自然过期，过期时间内的重复请求直接丢弃
        }
    }


    private Result<String> mgSignOut(LoginInfo loginUser, CptOpenMember cptOpenMember, String ip) {
        Integer userId = loginUser.getId().intValue();
        //根据id 类型 查询第三方用户信息
        try {

            MemBaseinfo memBaseinfo = memBaseinfoService.getUserByAccno(loginUser.getAccount());
            if (null == memBaseinfo) {
                logger.info("mglog {} mgSignOut exit cptOpenMember is null ", userId);
                return Result.success();
            }
            //生成单号
            String orderNo = Constants.GAME_MG_XF + SnowflakeIdWorker.createOrderSn();

            JSONObject result = gameBalance(loginUser, cptOpenMember);
            if (null != result && null != result.get("data")) {
                BigDecimal balance = result.getJSONObject("data").getBigDecimal("balance");
                if (balance.compareTo(BigDecimal.ZERO) == 1) {
                    JSONObject transferOut = gameTransferOut(orderNo, balance, loginUser, cptOpenMember);
                    if (null == transferOut || null == transferOut.get("data")) {
                        int checkOrderNum = 0;
                        while (checkOrderNum <= 3) {
                            JSONObject orderResult = gamePlayerGet(orderNo, loginUser, cptOpenMember);
                            if (null != orderResult) {
                                if (null != orderResult.get("data")) {
                                    String content = MessageUtils.get("mbtot") + OpenAPIProperties.PLATFORM_NAME + "/" + orderNo;
                                    gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.MG_IN.getValue(), balance, memBaseinfo, content,
                                            cptOpenMember, Constants.MG_ACCOUNT_TYPE);
                                    //站点棋牌余额加回去
                                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.MG.getCode(), balance);
                                    break;
                                }
                            } else {
                                checkOrderNum++;
                                TimeUnit.SECONDS.sleep(2);
                            }
                        }
                    } else {
                        String content = MessageUtils.get("mbtot") + OpenAPIProperties.PLATFORM_NAME + "/" + orderNo;
                        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.MG_IN.getValue(), balance, memBaseinfo, content,
                                cptOpenMember, Constants.MG_ACCOUNT_TYPE);
                        //站点棋牌余额加回去
                        sysChessBalanceService.addBalance(ChessBalanceTypeEnum.MG.getCode(), balance);
                        return Result.success();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("mglog {} mgSignOut ");
        }
        return Result.success();
    }


    @Override
    public void pullMgBetOrder() {
        try {
            JSONObject object = gameOrderRecord();
            if (null != object && null != object.get("data")) {
                JSONArray arrayList = JSONArray.parseArray(object.getJSONObject("data").getString("betlogs"));
                if (null == arrayList || arrayList.size() == 0) {
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<MgBetOrder> mgBetOrderList = new ArrayList<>();
                MgBetOrder mgBetOrder;
                Map<String, String> mgListMap = mgGameOrderList();
                for (int i = 0; i < arrayList.size(); i++) {
                    mgBetOrder = new MgBetOrder();
                    JSONObject list = (JSONObject) arrayList.get(i);
                    mgBetOrder.setPlayerName(list.getString("player_name"));
                    mgBetOrder.setVendorCode(list.getString("vendor_code"));
                    mgBetOrder.setGameCode(list.getString("game_code"));
                    mgBetOrder.setParentBetId(list.getString("parent_bet_id"));
                    mgBetOrder.setBetId(list.getString("bet_id"));
                    mgBetOrder.setTransType(list.getString("trans_type"));
                    mgBetOrder.setCurrency(list.getString("currency"));
                    mgBetOrder.setWalletCode(list.getString("wallet_code"));
                    mgBetOrder.setBetAmount(list.getBigDecimal("bet_amount"));
                    mgBetOrder.setWinAmount(list.getBigDecimal("win_amount"));
                    mgBetOrder.setTraceid(list.getString("traceId"));
                    mgBetOrder.setCreatedAt(sdf.parse(sdf.format(list.getTimestamp("created_at"))));
                    String playerName = list.getString("player_name");
                    mgBetOrder.setUserId(Long.valueOf(playerName.substring(playerName.lastIndexOf("V") + 1)));
                    mgBetOrder.setGameName(mgListMap.get(list.getString("game_code")));
                    mgBetOrderList.add(mgBetOrder);
                }
                if (mgBetOrderList.size() > 0) {
                    mgbetOrderService.insertBatch(mgBetOrderList);
                    //redisTemplate.opsForValue().set(RedisKeys.LIVE_MG_ORDER_RECORD_KEY, object.getJSONObject("data").getString("last_record_time"));
                    RedisBaseUtil.set(RedisKeys.INDO_MG_ORDER_RECORD_KEY, object.getJSONObject("data").getString("last_record_time"));
                    synMgOrderList(mgbetOrderService.queryMgOrderList());
                }
            }
        } catch (Exception e) {
            logger.error("pullMgBetOrder error ", e);
        }
    }


    /**
     * 同步打码量
     */
    @Async
    public void synMgOrderList(List<MgBetOrder> queryMgOrderList) {
        try {
            if (queryMgOrderList.size() > 0) {
                CptOpenMember mgGameBO;
                for (MgBetOrder mgBetOrderDO : queryMgOrderList) {
                    mgGameBO = new CptOpenMember();
                    mgGameBO.setUserId(mgBetOrderDO.getUserId().intValue());
                    gameCommonService.syncCodeSize(mgGameBO, Constants.MG_ACCOUNT_TYPE, MessageUtils.get("MGElectronics"), GoldchangeEnum.MG_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("MG同步打码量异常", e);
        }
    }


    private Map<Integer, String> mgGameList() {
        Map<Integer, String> map = new HashMap<>();
        List<MgGame> list = RedisBusinessUtil.getAllMgGame();
        if (null == list) {
            MgGameExample example = new MgGameExample();
            list = mgGameMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.MG_GANE_KEY, list);
            RedisBaseUtil.set(RedisKeys.MG_GANE_KEY, list);
        }

        if (list.size() > 0) {
            for (MgGame mgGame : list) {
                map.put(mgGame.getLotteryTag(), mgGame.getGameCode());
            }
        }
        return map;
    }


    private Map<String, String> mgGameOrderList() {
        //redisTemplate.delete(RedisKeys.MG_GANE_NAME_KEY);
        RedisBaseUtil.delete(RedisKeys.MG_GANE_NAME_KEY);
        Map<String, String> map = new HashMap<>();
        List<MgGame> list = RedisBusinessUtil.getAllMgGameOrder();
        if (null == list) {
            MgGameExample example = new MgGameExample();
            list = mgGameMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.MG_GANE_NAME_KEY, list);
            RedisBaseUtil.set(RedisKeys.MG_GANE_NAME_KEY, list);
        }

        if (list.size() > 0) {
            for (MgGame mgGame : list) {
                map.put(mgGame.getGameCode(), mgGame.getGameName());
            }
        }
        return map;
    }


    /**
     * 进入游戏接口
     *
     * @return 结果
     */
    public JSONObject gameCreate(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("player_name", cptOpenMember.getUsername());
            map.put("currency", CurEnum.CNY.getKeyName());
            logger.info("mglog {} gameCreate  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_CREATE, map, loginUser, "gameCreate");
            logger.info("mglog {} gameCreate  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameCreate  error", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 启动游戏接口
     *
     * @return 结果
     */
    public JSONObject gameLaunch(LoginInfo loginUser, CptOpenMember cptOpenMember, String gameCode) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("game_code", gameCode);
            map.put("player_name", cptOpenMember.getUsername());
            map.put("nickname", "MG" + loginUser.getId());
            map.put("language", Constants.MG_LANGUAGE);
            logger.info("mglog {} gameLaunch  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_LAUNCH, map, loginUser, "gameLaunch");
            logger.info("mglog {} gameLaunch  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameLaunch  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 查询余额接口
     *
     * @return 结果
     */
    public JSONObject gameBalance(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("player_name", cptOpenMember.getUsername());
            map.put("wallet_code", Constants.MG_WALLET_CODE);
            logger.info("mglog {} gameBalance  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_GETPLAYERBALANCE, map, loginUser, "gameBalance");
            logger.info("mglog {} gameBalance  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameLaunch  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 上分游戏接口
     *
     * @return 结果
     */
    public JSONObject gameTransferIn(String orderNo, BigDecimal amount, LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("player_name", cptOpenMember.getUsername());
            map.put("amount", amount.toString());
            map.put("traceId", orderNo);
            map.put("wallet_code", Constants.MG_WALLET_CODE);
            logger.info("mglog {} gameTransferIn  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_TRANSFERIN, map, loginUser, "game");
            logger.info("mglog {} gameTransferIn  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameTransferIn  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 下分游戏接口
     *
     * @return 结果
     */
    public JSONObject gameTransferOut(String orderNo, BigDecimal amount, LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("player_name", cptOpenMember.getUsername());
            map.put("amount", amount.toString());
            map.put("traceId", orderNo);
            map.put("wallet_code", Constants.MG_WALLET_CODE);
            logger.info("mglog {} gameTransferOut  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_TRANSFEROUT, map, loginUser, "game");
            logger.info("mglog {} gameTransferOut  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameTransferOut  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 查询余额接口
     *
     * @return 结果
     */
    public JSONObject gamePlayerGet(String orderNo, LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("player_name", cptOpenMember.getUsername());
            map.put("traceId", orderNo);
            map.put("timestamp_digit", Constants.MG_TIMESTAMP_DIGIT);
            map.put("wallet_code", Constants.MG_WALLET_CODE);
            logger.info("mglog {} gamePlayerGet  prams {}", loginUser.getId(), map);
            result = commonRequest(OpenAPIProperties.MG_OFFLINE_PLAYER_GET, map, loginUser, "game");
            logger.info("mglog {} gamePlayerGet  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameLaunch  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 查询余额接口
     *
     * @return 结果
     */
    public JSONObject gameOrderRecord() {
        JSONObject result = null;
        try {
            //String versionTime = (String) redisTemplate.opsForValue().get(RedisKeys.LIVE_MG_ORDER_RECORD_KEY);
            String versionTime = RedisBaseUtil.get(RedisKeys.INDO_MG_ORDER_RECORD_KEY);
            Map<String, String> map = new HashMap<>();
            if (StringUtils.isEmpty(versionTime)) {
                versionTime = "0";
            }
            map.put("row_version", versionTime);
            map.put("count", Constants.MG_COUNT);
            map.put("timestamp_digit", Constants.MG_TIMESTAMP_DIGIT);
            map.put("vendor_code", Constants.MG_VENDOR_CODE);
            logger.info("mglog  gameOrderRecord  prams {}", map);
            result = commonRequest(OpenAPIProperties.MG_API_URL_RECORD_GET, map, new LoginInfo(), "game");
            logger.info("mglog  gameOrderRecord  prams {} result{}", map, result);

        } catch (Exception e) {
            logger.error("mglog  gameOrderRecord  ", e);
            return result;
        }
        return result;
    }


    /**
     * 公共请求接口
     */
    private JSONObject commonRequest(String pathUrl, Map<String, String> map, LoginInfo loginUser, String type) {
        map.put("secret_key", OpenAPIProperties.MG_API_SECRET_KEY);
        map.put("operator_token", OpenAPIProperties.MG_API_OPERATOR_TOKEN);
        JSONObject jsonObject = null;
        try {
            String mgApiResponse = MgUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, "http", pathUrl, map, type, map);
            if (StringUtils.isNotEmpty(mgApiResponse)) {
                return jsonObject.parseObject(mgApiResponse);
            }
        } catch (Exception e) {
            logger.error("mglog {}:{} doProxyPostJson occur error:{},  proxyHost:{}, proxyPort:{}, ",
                    loginUser.getId(), type, e.getMessage(), OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, e);
            return jsonObject;
        }
        return jsonObject;
    }


}
