package com.indo.game.services.impl;

import com.indo.common.constant.RedisKeys;
import com.indo.game.game.RedisBaseUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.mapper.GameBetamountRecordMapper;
import com.indo.game.mapper.LotteryMapperExt;
import com.indo.game.mapper.OrderMapper;
import com.indo.game.pojo.dto.MemGoldchangeDO;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.GameBetamountRecord;
import com.indo.game.pojo.entity.Lottery;
import com.indo.game.pojo.entity.MemBaseinfo;
import com.indo.game.services.GameCommonService;
import com.indo.game.services.MemBaseinfoService;
import com.indo.game.services.MemBaseinfoWriteService;
import com.indo.game.services.OrderCommonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.indo.game.utils.ViewUtil.getTradeOffAmount;

@Service
public class GameCommonServiceImpl implements GameCommonService {

    private static final Logger logger = LoggerFactory.getLogger(GameCommonServiceImpl.class);

    //@Autowired
    //private RedisTemplate redisTemplate;

    @Autowired
    private LotteryMapperExt lotteryMapperExt;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderCommonService orderCommonService;
    @Autowired
    private MemBaseinfoWriteService memBaseinfoWriteService;
    @Autowired
    private GameBetamountRecordMapper gameBetamountRecordMapper;
    @Autowired
    private MemBaseinfoService memBaseinfoService;

    @Override
    public boolean isGameEnabled(Integer lotteryId) {
        if (null == lotteryId || lotteryId <= 0) {
            return false;
        }
        //Lottery lottery = (Lottery) redisTemplate.opsForValue().get(RedisKeys.LOTTERY_KEY + lotteryId);
        Lottery lottery = RedisBaseUtil.get(RedisKeys.LOTTERY_KEY + lotteryId);
        Integer isWork;
        if (null == lottery) {
            isWork = lotteryMapperExt.selectLotteryIsWork(lotteryId);
        } else {
            isWork = lottery.getIsWork();
        }
        return null != isWork && isWork.equals(1);
    }


    /**
     * 第三方公共转入转出的方法
     *
     * @param type          转入转出类型   57：棋牌转出到live   1:棋牌游戏转出到live
     * @param changeAmount  转入转出金额
     * @param xiazhuren     用户基础信息表
     * @param remark        操作说明/标记
     * @param cptOpenMember 第三方用户信息
     * @param iotType       第三方账号类型
     */

    @Override
    public Boolean inOrOutBalanceCommon(int type, BigDecimal changeAmount, MemBaseinfo xiazhuren, String remark, CptOpenMember cptOpenMember, String iotType) {
        MemGoldchangeDO dto = new MemGoldchangeDO();
        Integer userId = xiazhuren.getMemid().intValue();
        //用户账号
        String accountCache = this.getAccountCache(userId, cptOpenMember);
        dto.setAccno(xiazhuren.getAccno());
        dto.setOpnote(remark);
        dto.setUserId(userId);
        //设置创建人
        dto.setCreateUser(accountCache);
        dto.setCreatTime(new Date());
        // 第三方平台下分，同步打码量
        int factor = -1;
        if (GoldchangeEnum.AE_IN.getValue().equals(type) || GoldchangeEnum.MG_IN.getValue().equals(type) || GoldchangeEnum.DB_IN.getValue().equals(type) || type == 1) {
            factor = 1;
            BigDecimal changeNoWithdrawalAmount = null;

            Map<String, Date> dateTime = get2dateTime();
            Date beginTime = dateTime.get("beginTime");
            Date endTime = dateTime.get("endTime");
            //有效打码量
            changeNoWithdrawalAmount = syncDamaliang(cptOpenMember, iotType, userId, beginTime, endTime);
            if (changeNoWithdrawalAmount != null) {
                //记录用户打码量信息
                BigDecimal tradeOffAmount = getTradeOffAmount(changeNoWithdrawalAmount);
                GameBetamountRecord betamountRecord = new GameBetamountRecord();
                betamountRecord.setBetamount(tradeOffAmount);
                betamountRecord.setUserId(userId.longValue());
                betamountRecord.setType(iotType);
                betamountRecord.setBegintime(beginTime);
                betamountRecord.setEndtime(endTime);
                try {
                    gameBetamountRecordMapper.insertSelective(betamountRecord);
                } catch (Exception e) {
                    logger.error("GameCommonServiceImpl {} inOrOutBalanceCommon insert gameBetamountRecordMapper error. Exception:{}", userId, e);
                }
                dto.setOpnote(remark + ":" + MessageUtils.get("andsyncsourcecode") + "：" + tradeOffAmount);
                //添加累计投注额
                dto.setBetAmount(tradeOffAmount);
                // 乘以系统倍数 不可提 -1
                if (!Constants.DB_ACCOUNT_TYPE.equals(type)) {
                    changeNoWithdrawalAmount = orderCommonService.calcNoWithdrawalAmount(changeNoWithdrawalAmount).multiply(new BigDecimal(-1));
                    dto.setNoWithdrawalAmount(getTradeOffAmount(changeNoWithdrawalAmount));
                } else {
                    dto.setNoWithdrawalAmount(getTradeOffAmount(changeNoWithdrawalAmount));
                }
                // 修改用户余额信息
                //添加不可提余额
                dto.setNoWithdrawalAmount(getTradeOffAmount(changeNoWithdrawalAmount));
            }
        }
        // 设置类型
        //电竞
        if (type == -1 && iotType.equals(Constants.ES_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.ES_OUT.getValue());
        }
        if (type == 1 && iotType.equals(Constants.ES_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.ES_IN.getValue());
        }
        //AE
        if (type == GoldchangeEnum.AE_OUT.getValue() && iotType.equals(Constants.AE_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.AE_OUT.getValue());
        }
        if (type == GoldchangeEnum.AE_IN.getValue()) {
            dto.setChangetype(GoldchangeEnum.AE_IN.getValue());
        }
        //mg
        if (type == GoldchangeEnum.MG_OUT.getValue() && iotType.equals(Constants.MG_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.MG_OUT.getValue());
        }
        if (type == GoldchangeEnum.MG_IN.getValue()) {
            dto.setChangetype(GoldchangeEnum.MG_IN.getValue());
        }
        if (type == GoldchangeEnum.DB_OUT.getValue() && iotType.equals(Constants.DB_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.DB_OUT.getValue());
        }
        if (type == GoldchangeEnum.DB_IN.getValue()) {
            dto.setChangetype(GoldchangeEnum.DB_IN.getValue());
        }
        //AG
        if (type == 1 && iotType.equals(Constants.AG_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.AG_IN.getValue());
        }
        if (type == -1 && iotType.equals(Constants.AG_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.AG_OUT.getValue());
        }
        //开元
        if (type == -1 && iotType.equals(Constants.KY_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.KY_OUT.getValue());
        }
        if (type == 1 && iotType.equals(Constants.KY_ACCOUNT_TYPE)) {
            dto.setChangetype(GoldchangeEnum.KY_IN.getValue());
        }
        dto.setQuantity(getTradeOffAmount(changeAmount.multiply(new BigDecimal(factor))));
        return memBaseinfoWriteService.updateUserBalance(dto);

    }

    /**
     * 同步打码量
     */
    @Transactional
    @Override
    public boolean syncCodeSize(CptOpenMember cptOpenMember, String type, String remark, int changeType) {
        try {
            MemBaseinfo xiazhuren = memBaseinfoService.getMemById(Long.valueOf(cptOpenMember.getUserId()));
            Integer userId = xiazhuren.getMemid().intValue();
            Map<String, Date> dateTime = get2dateTime();
            Date beginTime = dateTime.get("beginTime");
            Date endTime = dateTime.get("endTime");
            //有效打码量
            BigDecimal changeNoWithdrawalAmount = syncDamaliang(cptOpenMember, type, userId, beginTime, endTime);
            if (changeNoWithdrawalAmount == null) {
                return false;
            }
            if (Constants.DB_ACCOUNT_TYPE.equals(type)) {
                changeNoWithdrawalAmount = changeNoWithdrawalAmount.multiply(new BigDecimal(-1));
            }
            if (changeNoWithdrawalAmount.compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal tradeOffAmount = getTradeOffAmount(changeNoWithdrawalAmount);
                GameBetamountRecord betamountRecord = new GameBetamountRecord();
                betamountRecord.setUserId(cptOpenMember.getUserId().longValue());
                betamountRecord.setBetamount(tradeOffAmount);
                betamountRecord.setType(type);
                betamountRecord.setBegintime(beginTime);
                betamountRecord.setEndtime(endTime);
                gameBetamountRecordMapper.insertSelective(betamountRecord);

                MemGoldchangeDO dto = new MemGoldchangeDO();
                //用户账号
                String accountCache = this.getAccountCache(userId, cptOpenMember);
                dto.setAccno(xiazhuren.getAccno());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MessageUtils.get("synchronize")).append(remark).append(MessageUtils.get("codeamount")+"/").append(changeNoWithdrawalAmount);
                dto.setOpnote(stringBuilder.toString());
                dto.setUserId(userId);
                dto.setChangetype(changeType);
                dto.setType(Constants.EXTERNAL_TYPE);
                //设置创建人
                dto.setCreateUser(accountCache);
                dto.setCreatTime(new Date());
                //DB 投注为负数做处理
                changeNoWithdrawalAmount = orderCommonService.calcNoWithdrawalAmount(changeNoWithdrawalAmount).multiply(new BigDecimal(-1));
                dto.setNoWithdrawalAmount(getTradeOffAmount(changeNoWithdrawalAmount));
                return memBaseinfoWriteService.updateUserBalance(dto);
            }
        } catch (Exception e) {
            logger.error("syncCodeSize {} error. Exception:{}", cptOpenMember.getUserId(), e);
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BigDecimal syncDamaliang(CptOpenMember cptOpenMember, String iotType, Integer userId, Date beginTime, Date endTime) {
        BigDecimal changeAmount = null;
        try {
            if (cptOpenMember == null) {
                logger.info("changeNoWithdrawalAmount type {} cptOpenMember {} is null", iotType, cptOpenMember);
                return changeAmount;
            }
            if (iotType.equals(Constants.KY_ACCOUNT_TYPE)) {
                // 根据玩家账号查询 ky_bet_order表中24小时内的总有效打码量
                Map<String, Object> betOrdersMap = orderMapper.kyBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("ky_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime, betOrdersMap);
                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("ky_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount ky_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.kyUpdateBetData(orderIds);
                }
            }
            if (iotType.equals(Constants.AG_ACCOUNT_TYPE)) {
                // 查询
                Map<String, Object> betOrdersMap = orderMapper.agBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("ag_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime,
                        betOrdersMap);

                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("ag_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount ag_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.agUpdateBetData(orderIds);
                }

                Map<String, Object> betFishOrdersMap = orderMapper.agFishBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("ag_account_type beginTime {} endTime {} betFishOrdersMap {}", beginTime, endTime,
                        betFishOrdersMap);

                if (betFishOrdersMap != null) {
                    String ids = String.valueOf(betFishOrdersMap.get("ids"));
                    logger.info("ag_account_type betFishOrdersMap ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betFishOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount betFishOrdersMap ids or betAmount is null ");
                        return changeAmount;
                    }
                    if (changeAmount.compareTo(BigDecimal.ZERO) == 1) {
                        changeAmount = changeAmount.add((BigDecimal) betOrdersMap.get("betAmount"));
                    } else {
                        changeAmount = (BigDecimal) betFishOrdersMap.get("betAmount");
                    }
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.agUpdateBetFishData(orderIds);
                }

            }
            if (iotType.equals(Constants.ES_ACCOUNT_TYPE)) {
                // 查询
                Map<String, Object> betOrdersMap = orderMapper.esBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("es_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime, betOrdersMap);
                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("es_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount es_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.esUpdateBetData(orderIds);
                }
            }
            if (iotType.equals(Constants.AE_ACCOUNT_TYPE)) {
                // 查询
                Map<String, Object> betOrdersMap = orderMapper.aeBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("ae_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime, betOrdersMap);
                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("ae_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount ae_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.aeUpdateBetData(orderIds);
                }
            }

            if (iotType.equals(Constants.MG_ACCOUNT_TYPE)) {
                // 查询
                Map<String, Object> betOrdersMap = orderMapper.mgBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("mg_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime, betOrdersMap);
                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("mg_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount mg_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.mgUpdateBetData(orderIds);
                }
            }

            if (iotType.equals(Constants.DB_ACCOUNT_TYPE)) {
                // 查询
                Map<String, Object> betOrdersMap = orderMapper.dbBetData(beginTime, endTime, cptOpenMember.getUserId());
                logger.info("db_account_type beginTime {} endTime {} betOrdersMap {}", beginTime, endTime, betOrdersMap);
                if (betOrdersMap != null) {
                    String ids = String.valueOf(betOrdersMap.get("ids"));
                    logger.info("db_account_type ids {} ", ids);
                    if (StringUtils.isBlank(ids) || betOrdersMap.get("betAmount") == null) {
                        logger.error("changeNoWithdrawalAmount db_account_type ids or betAmount is null ");
                        return changeAmount;
                    }
                    changeAmount = (BigDecimal) betOrdersMap.get("betAmount");
                    int[] orderIds = Arrays.asList(ids.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                    // 更新标识
                    orderMapper.dbUpdateBetData(orderIds);
                }
            }
            return changeAmount;
        } catch (Exception e) {
            logger.error("changeNoWithdrawalAmount exception params: type {} userId {} ", iotType, userId);
            return null;
        }
    }


    public String getAccountCache(Integer userId, CptOpenMember cptOpenMember) {
        if (userId == null) {
            return "";
        }
        String key = Constants.MEMBER_ACCOUNT + userId;
        //String account = (String) redisTemplate.opsForValue().get(key);
        String account = RedisBaseUtil.get(key);
        if (StringUtils.isEmpty(account)) {
            account = cptOpenMember.getUsername();
            //redisTemplate.opsForValue().set(key, account);
            RedisBaseUtil.set(key, account);
        }
        return account;
    }

    public static Map<String, Date> get2dateTime() {
        HashMap<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();
        map.put("endTime", endTime);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date beginTime = calendar.getTime();
        map.put("beginTime", beginTime);
        return map;
    }

}
