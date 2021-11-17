package com.indo.game.services.db.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.indo.common.constant.RedisKeys;
import com.indo.common.exception.BadRequestException;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.ChessBalanceTypeEnum;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.db.DbGameMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.MemBaseinfo;
import com.indo.game.pojo.entity.db.DbBetOrder;
import com.indo.game.pojo.entity.db.DbGame;
import com.indo.game.pojo.entity.db.DbGameExample;
import com.indo.game.services.*;
import com.indo.game.services.db.DbService;
import com.indo.game.services.db.JdbBetOrderService;
import com.indo.game.utils.DbUtil;
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
public class DbServiceImpl implements DbService {

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
    private DbGameMapper dbGameMapper;

    @Autowired
    private JdbBetOrderService jdbBetOrderService;

    @Autowired
    private CptOpenMemberService cptOpenMemberService;

    @Autowired
    private SysChessBalanceService sysChessBalanceService;

    @Override
    public Result<String> dbGame(LoginInfo loginUser, Integer gameCode, Integer gameType) {
        logger.info("dblog {} mgGame account:{}, mgCodeId:{}", loginUser.getId(), loginUser.getNickName(), gameCode);
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(gameCode)) {
            logger.info("此游戏禁用 Code{}", gameCode);
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
        BigDecimal balance = memBaseinfoService.getBalanceById(loginUser.getId().intValue());
        balance = null == balance ? BigDecimal.ZERO : balance;
        //验证站点棋牌余额
        if (!sysChessBalanceService.isBalanceInChess(ChessBalanceTypeEnum.JDB.getCode(), balance)) {
            logger.info("站点JDB棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }
        String key = Constants.DB_ACCOUNT_TYPE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("dblog cyCallback[{}] ", loginUser.getId());
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.DB_INIT_KEY + loginUser.getId() + gameCode;
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(1, 30, TimeUnit.SECONDS);
            Result<String> result = null;
            if (bool) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.DB_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    cptOpenMember = new CptOpenMember();
                    String account = openAPIProperties.PLATFORM_NAME + loginUser.getId();
                    cptOpenMember.setUsername(account);
                    JSONObject createResult = gameCreate(loginUser, cptOpenMember);
                    if (null != createResult && null != createResult.get("status") && "0000".equals(createResult.get("status"))) {
                        cptOpenMember.setUserId(loginUser.getId().intValue());
                        cptOpenMember.setCreateTime(new Date());
                        cptOpenMember.setLoginTime(new Date());
                        cptOpenMember.setType(Constants.DB_ACCOUNT_TYPE);
                        externalService.saveCptOpenMember(cptOpenMember);
                    } else {
                        return result;
                    }
                } else {
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                }
                JSONObject resultLaunch;
                resultLaunch = gameToken(loginUser, cptOpenMember, gameCode);
                //代理端口
                if (null != resultLaunch && null != resultLaunch.get("status") && "0000".equals(resultLaunch.get("status"))) {
                    result = new Result<String>();
                    result.setData(resultLaunch.getString("path"));
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
        // 是否开售校验
        if (!gameCommonService.isGameEnabled(gameCode)) {
            logger.info("此游戏禁用 Code{}", gameCode);
            return;
        }

        String key = Constants.DB_INIT_TIME_KEY + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("dblog initAccountInfo isLocked {} return thread", key);
            return;
        }

        String initKey = Constants.DB_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            /**
             * 自动转入转出（进入游戏查询用户在开元棋牌的可下分余额，有余额则先转出）
             */
            boolean bool = lock.writeLock().tryLock(10, 30, TimeUnit.SECONDS);
            if (bool) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.DB_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.error("mglog {} initAccountInfo  type {} is not find info", loginUser.getId(),
                            Constants.DB_ACCOUNT_TYPE);
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
                logger.info("dblog {} exit no take lock.", loginUser.getId());
            }

        } catch (Exception e) {
            logger.error("dblog {} initAccountInfo error", loginUser.getId(), e);
        } finally {
            lock.writeLock().unlock();
        }

    }

    private void autoSF(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        try {
            MemBaseinfo xiazhuren = memBaseinfoService.getUserByAccno(loginUser.getAccount());
            if (xiazhuren == null) {
                logger.error("dblog {}  autoSF appMember is null", loginUser.getId());
                return;
            }
            if (xiazhuren.getGoldnum().compareTo(BigDecimal.ZERO) < 1) {
                logger.error("dblog {} autoSF appMember balance {} is lt=; zero", xiazhuren.getGoldnum(), loginUser.getId());
                return;
            }
            //用户余额
            BigDecimal balance = xiazhuren.getGoldnum();

            //验证站点棋牌余额
            if (!verificationBalanceInChess(ChessBalanceTypeEnum.JDB.getCode(), balance, loginUser)) {
                logger.info("站点JDB棋牌余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
                //站点棋牌余额不足
                throw new BadRequestException(MessageUtils.get("tcgqifpccs"));
            }

            //生成单号
            String orderNo = SnowflakeIdWorker.createOrderSn();
            //先扣款
            String remark = openAPIProperties.PLATFORM_NAME + MessageUtils.get("ttbotf")+"/" + orderNo;
            gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.DB_OUT.getValue(), balance, xiazhuren, remark,
                    cptOpenMember, Constants.DB_ACCOUNT_TYPE);

            JSONObject result = gameTransferIn(orderNo, balance, loginUser, cptOpenMember);
            if (null == result || null == result.get("status")) {
                int checkOrderNum = 0;
                while (checkOrderNum <= 3) {
                    JSONObject orderResult = gameQeryOrder(orderNo, loginUser, cptOpenMember);
                    if (null != orderResult && null != orderResult.get("status") && "0000".equals(orderResult.get("status"))) {
                        if (null != orderResult.get("data") && orderResult.getJSONArray("data").getJSONObject(0).getBigDecimal("amount").compareTo(BigDecimal.ZERO) < 1) {
                            String content = MessageUtils.get("tofbt")+"/" + openAPIProperties.PLATFORM_NAME + orderNo;
                            gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.DB_IN.getValue(), balance, xiazhuren, content,
                                    cptOpenMember, Constants.DB_ACCOUNT_TYPE);
                            //站点棋牌余额加回去
                            sysChessBalanceService.addBalance(ChessBalanceTypeEnum.JDB.getCode(), balance);
                        } else {
                            break;
                        }
                    } else {
                        checkOrderNum++;
                        TimeUnit.SECONDS.sleep(2);
                    }
                }
            } else {
                if (!"0000".equals(result.get("status"))) {
                    String content = MessageUtils.get("tofbt")+"/" + openAPIProperties.PLATFORM_NAME + orderNo;
                    gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.DB_IN.getValue(), balance, xiazhuren, content,
                            cptOpenMember, Constants.DB_ACCOUNT_TYPE);
                    //站点棋牌余额加回去
                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.JDB.getCode(), balance);
                }
            }
        } catch (Exception e) {
            logger.error("DBlog {} initAccountInfo error", loginUser.getId(), e);
        }
    }

    /**
     * 验证站点棋牌余额是否足够
     */
    private boolean verificationBalanceInChess(String chessCode, BigDecimal balance, LoginInfo loginUser) {
        //当前该站点要进入JDB棋牌所有JDB用户共享锁 公平锁
        RLock lock = redissonClient.getFairLock(chessCode + "_GAME_CHESS_BALANCE");
        //检查该站点购买的db棋牌余额是否够 该用户上分
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
            logger.error("dblog {} initAccountInfo-> verificationBalanceInChess  error:{} ", loginUser.getId(), e.getMessage());
            throw new BadRequestException(MessageUtils.get("tnibptal"));
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * 创建账号
     *
     * @return 结果
     */
    public JSONObject gameCreate(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_CREATE);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT); //代理账号
            map.put("uid", cptOpenMember.getUsername());
            map.put("name", cptOpenMember.getUsername());
            map.put("credit_allocated", "0");
            logger.info("mglog {} gameCreate  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameCreate");
            logger.info("mglog {} gameCreate  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("mglog {} gameCreate  error", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 获取TOKEN
     *
     * @return 结果
     */
    public JSONObject gameToken(LoginInfo loginUser, CptOpenMember cptOpenMember, Integer gameCode) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_TOKEN);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("uid", cptOpenMember.getUsername().toLowerCase());
            map.put("lang", "ch");
            map.put("gType", "7");
            map.put("mType", gameCode + "");
            map.put("remark", "");
            map.put("windowMode", "2");
            map.put("isAPP", "true");
            map.put("lobbyURL", "");
            map.put("moreGame", "0");
            map.put("mute", "0");
            map.put("cardGameGroup", "");
            map.put("isShowDollarSign", "false");
            logger.info("dblog {} gameToken  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameToken");
            logger.info("dblog {} gameToken  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameToken  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 获取试玩TOKEN
     *
     * @return 结果
     */
    public JSONObject gameDemoToken(LoginInfo loginUser, CptOpenMember cptOpenMember, Integer gameCode) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_DEMO_GAME_TOKEN);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("uid", cptOpenMember.getUsername().toLowerCase());
            map.put("lang", "ch");
            map.put("gType", "7");
            map.put("remark", "");
            map.put("mType", gameCode + "");
            map.put("windowMode", "2");
            map.put("lobbyURL", "");
            map.put("isAPP", "true");
            map.put("moreGame", "0");
            map.put("mute", "0");
            map.put("isShowDollarSign", "false");
            map.put("cardGameGroup", "");
            logger.info("dblog {} gameDemoToken  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameDemoToken");
            logger.info("dblog {} gameDemoToken  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameDemoToken  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 查询玩家数据
     *
     * @return 结果
     */
    public JSONObject gameBalance(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_QUERY);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("uid", cptOpenMember.getUsername().toLowerCase());
            logger.info("dblog {} gameBalance  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameBalance");
            logger.info("dblog {} gameBalance  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameLaunch  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 下分
     *
     * @param ip 用户请求的ip
     */
    @Override
    public Result<String> dbExit(LoginInfo loginUser, String ip) {
        logger.info("dblog {} dbExit   account{} ip {}", loginUser.getId(), loginUser.getNickName(), ip);
        String key = Constants.DB_EXIT_TIME_KEY + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("dblog dbExit isLocked {} return thread", key);
            return Result.success();
        }

        RedisLock lock = new RedisLock(Constants.DB_EXIT_KEY + loginUser.getId(), 0, 3000);
        try {
            if (lock.lock()) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.DB_ACCOUNT_TYPE);
                if (null == cptOpenMember) {
                    logger.info("dblog {} mgSignOut exit cptOpenMember is null ", loginUser.getId());
                    return Result.success();
                }


                Result<String> aeSignOut = dbSignOut(loginUser, cptOpenMember, ip);
                //标识游戏下分
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
                return aeSignOut;

            } else {
                logger.info("dblog {} dbExit request repeat, ip:{}", loginUser.getId(), ip);
                return Result.success();
            }
        } catch (Exception e) {
            logger.info("dblog {} dbExit  ip:{}", loginUser.getId(), ip, e);
            return Result.success();
        } finally {
            //主动下分的让其自然过期，过期时间内的重复请求直接丢弃
        }
    }

    @Override
    public void pullDbBetOrder() {
        try {
            JSONObject object = gameQeryOrder();
            if (null != object && null != object.get("status") && "0000".equals(object.get("status"))) {
                JSONArray arrayList = object.getJSONArray("data");
                if (null == arrayList || arrayList.size() == 0) {
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<DbBetOrder> dbBetOrderList = new ArrayList<>();
                DbBetOrder dbBetOrder;
                Map<Integer, String> dbListMap = dbGameOrderList();
                for (int i = 0; i < arrayList.size(); i++) {
                    dbBetOrder = new DbBetOrder();
                    JSONObject list = (JSONObject) arrayList.get(i);
                    String playerName = list.getString("playerId");
                    dbBetOrder.setSeqNo(list.getLong("seqNo"));
                    dbBetOrder.setPlayerId(playerName);
                    dbBetOrder.setGameDate(sdf.parse(sdf.format(list.getTimestamp("gameDate"))));
                    dbBetOrder.setgType(list.getInteger("gType"));
                    dbBetOrder.setmType(list.getInteger("mtype"));
                    dbBetOrder.setRoomType(list.getInteger("roomType"));
                    dbBetOrder.setCurrency(list.getString("currency"));
                    dbBetOrder.setBet(list.getBigDecimal("bet"));
                    dbBetOrder.setWin(list.getBigDecimal("win"));
                    dbBetOrder.setTotal(list.getBigDecimal("total"));
                    dbBetOrder.setDenom(list.getBigDecimal("denom"));
                    dbBetOrder.setBeforeBalance(list.getBigDecimal("beforeBalance"));
                    dbBetOrder.setAfterBalance(list.getBigDecimal("afterBalance"));
                    dbBetOrder.setLastModifyTime((sdf.parse(sdf.format(list.getTimestamp("lastModifyTime")))));
                    dbBetOrder.setPlayerIp(list.getString("playerIp"));
                    dbBetOrder.setClientType(list.getString("clientType"));
                    dbBetOrder.setMemberId(Long.valueOf(playerName.substring(openAPIProperties.PLATFORM_NAME.length())));
                    dbBetOrder.setGameName(dbListMap.get(list.getInteger("mtype")));
                    dbBetOrderList.add(dbBetOrder);
                }
                if (dbBetOrderList.size() > 0) {
                    jdbBetOrderService.insertBatch(dbBetOrderList);
                    synJdbOrderList(jdbBetOrderService.queryJdbOrderList());
                }
            }
        } catch (Exception e) {
            logger.error("pullMgBetOrder error ", e);
        }
    }

    @Async
    public void synJdbOrderList(List<DbBetOrder> queryJdbOrderList) {
        try {
            if (queryJdbOrderList.size() > 0) {
                CptOpenMember jdbGameBO;
                for (DbBetOrder dbBetOrder : queryJdbOrderList) {
                    jdbGameBO = new CptOpenMember();
                    jdbGameBO.setUserId(dbBetOrder.getMemberId().intValue());
                    gameCommonService.syncCodeSize(jdbGameBO, Constants.DB_ACCOUNT_TYPE, MessageUtils.get("JDBfishing"), GoldchangeEnum.JDB_BET_ORDER.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("JDB同步打码量异常", e);
        }
    }


    /**
     * 下分业务操作
     */
    private Result<String> dbSignOut(LoginInfo loginUser, CptOpenMember cptOpenMember, String ip) {
        Integer userId = loginUser.getId().intValue();
        //根据id 类型 查询第三方用户信息
        try {
            MemBaseinfo memBaseinfo = memBaseinfoService.getUserByAccno(loginUser.getAccount());
            if (null == memBaseinfo) {
                logger.info("dblog {} mgSignOut exit cptOpenMember is null ", userId);
                return Result.success();
            }
            //生成单号
            gamExitUser(loginUser, cptOpenMember);
            String orderNo = SnowflakeIdWorker.createOrderSn();

            JSONObject result = gameBalance(loginUser, cptOpenMember);
            if (null != result && null != result.get("status") && result.getJSONArray("data").getJSONObject(0).getBigDecimal("balance").compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal balance = result.getJSONArray("data").getJSONObject(0).getBigDecimal("balance");
                if (balance.compareTo(BigDecimal.ZERO) == 1) {
                    JSONObject transferOut = gameTransferOut(orderNo, balance, loginUser, cptOpenMember);
                    if (null == transferOut || null == transferOut.get("status")) {
                        int checkOrderNum = 0;
                        while (checkOrderNum <= 3) {
                            JSONObject orderResult = gameQeryOrder(orderNo, loginUser, cptOpenMember);
                            if (null != orderResult) {
                                if (null != orderResult.get("status") && "0000".equals(orderResult.get("status"))) {
                                    String content = MessageUtils.get("tofbt") + openAPIProperties.PLATFORM_NAME + "/" + orderNo;
                                    gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.MG_IN.getValue(), balance, memBaseinfo, content,
                                            cptOpenMember, Constants.DB_ACCOUNT_TYPE);
                                    //站点棋牌余额加回去
                                    sysChessBalanceService.addBalance(ChessBalanceTypeEnum.JDB.getCode(), balance);
                                    break;
                                }
                            } else {
                                checkOrderNum++;
                                TimeUnit.SECONDS.sleep(2);
                            }
                        }
                    } else {
                        if ("0000".equals(transferOut.get("status"))) {
                            String content = MessageUtils.get("tofbt") + openAPIProperties.PLATFORM_NAME + "/" + orderNo;
                            gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.DB_IN.getValue(), balance, memBaseinfo, content,
                                    cptOpenMember, Constants.DB_ACCOUNT_TYPE);
                            //站点棋牌余额加回去
                            sysChessBalanceService.addBalance(ChessBalanceTypeEnum.JDB.getCode(), balance);
                            return Result.success();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("mglog {} mgSignOut ");
        }
        return Result.success();
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
            map.put("action", Constants.DB_GAME_MONEY);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("uid", cptOpenMember.getUsername().toLowerCase());
            map.put("serialNo", orderNo);
            map.put("allCashOutFlag", "0");
            map.put("amount", amount + "");
            map.put("remark", MessageUtils.get("transferin"));
            logger.info("dblog {} gameTransferIn  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameTransferIn");
            logger.info("dblog {} gameTransferIn  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameTransferIn  ", loginUser.getId(), e);
            return result;
        }
        return null;
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
            map.put("action", Constants.DB_GAME_MONEY);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("uid", cptOpenMember.getUsername().toLowerCase());
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("serialNo", orderNo);
            BigDecimal momey = amount.multiply(new BigDecimal(-1));
            map.put("amount", momey + "");
            map.put("allCashOutFlag", "0");
            map.put("remark", MessageUtils.get("transferout"));
            logger.info("dblog {} gameTransferOut  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameTransferOut");
            logger.info("dblog {} gameTransferOut  prams {} result {}", loginUser.getId(), map, result);
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
    public JSONObject gameQeryOrder(String orderNo, LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_QUERY_MONEY);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("serialNo", orderNo);
            logger.info("dblog {} gameQeryOrder  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gameQeryOrder");
            logger.info("dblog {} gameQeryOrder  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameQeryOrder  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }

    /**
     * 踢出玩家
     */
    public JSONObject gamExitUser(LoginInfo loginUser, CptOpenMember cptOpenMember) {
        JSONObject result = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_EXIT_USER);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("uid", cptOpenMember.getUsername());
            logger.info("dblog {} gamExitUser  prams {}", loginUser.getId(), map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, loginUser, "gamExitUser");
            logger.info("dblog {} gamExitUser  prams {} result {}", loginUser.getId(), map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameQeryOrder  ", loginUser.getId(), e);
            return result;
        }
        return result;
    }


    /**
     * 查询注单接口
     *
     * @return 结果
     */
    public JSONObject gameQeryOrder() {
        JSONObject result = null;
        try {
            SimpleDateFormat dataFrom = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.HOUR_OF_DAY, -12);
            calendar.add(Calendar.MINUTE, -13);
            String startTime = dataFrom.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, 1);
            String endTime = dataFrom.format(calendar.getTime());
            Map<String, String> map = new HashMap<>();
            map.put("action", Constants.DB_GAME_QUERY_ORDER);
            map.put("ts", System.currentTimeMillis() + "");
            map.put("parent", openAPIProperties.DB_API_PARENT);
            map.put("starttime", startTime);
            map.put("endtime", endTime);
            logger.info("dblog {} gameQeryOrder  prams {}", map);
            result = commonRequest(openAPIProperties.DB_API_URL, map, new LoginInfo(), "gameQeryOrder");
            logger.info("dblog {} gameQeryOrder  prams {} result {}", map, result);
        } catch (Exception e) {
            logger.error("dblog {} gameQeryOrder  ", e);
            return result;
        }
        return result;
    }


    /**
     * 公共请求接口
     */
    private JSONObject commonRequest(String pathUrl, Map<String, String> map, LoginInfo loginUser, String type) {
        JSONObject jsonObject = null;
        try {
            JSONObject sortParams = DbUtil.sortMap(map);
            String params = DbUtil.encrypt(sortParams.toString(), openAPIProperties.DB_KEY, openAPIProperties.DB_IV);
            Map<String, String> pramsMap = new HashMap<>();
            pramsMap.put("dc", openAPIProperties.DB_API_DC);
            pramsMap.put("x", params);
            String mgApiResponse = DbUtil.doProxyPostJson(openAPIProperties.PROXY_HOST_NAME, openAPIProperties.PROXY_PORT, openAPIProperties.DB_PROXY_HOST, "http", pathUrl, pramsMap, type, map);
            if (StringUtils.isNotEmpty(mgApiResponse)) {
                return jsonObject.parseObject(mgApiResponse);
            }
        } catch (Exception e) {
            logger.error("dblog {}:{} doProxyPostJson occur error:{},  proxyHost:{}, proxyPort:{}, ",
                    loginUser.getId(), type, e.getMessage(), openAPIProperties.PROXY_HOST_NAME, openAPIProperties.PROXY_PORT, e);
            return jsonObject;
        }
        return jsonObject;
    }


    private Map<Integer, String> dbGameOrderList() {
        Map<Integer, String> map = new HashMap<>();
        List<DbGame> list = RedisBusinessUtil.getAllDbGameOrder();
        if (null == list) {
            DbGameExample example = new DbGameExample();
            list = dbGameMapper.selectByExample(example);
            //redisTemplate.opsForValue().set(RedisKeys.DB_GANE_NAME_KEY, list);
            RedisBaseUtil.set(RedisKeys.DB_GANE_NAME_KEY, list);
        }

        if (list.size() > 0) {
            for (DbGame dbGame : list) {
                map.put(dbGame.getmType(), dbGame.getGameName());
            }
        }
        return map;
    }

}
