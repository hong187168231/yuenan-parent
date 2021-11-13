package com.indo.game.services.es.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBaseUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.MD5Util;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.CaipiaoTypeEnum;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.es.EsBetOrder;
import com.indo.game.services.CptOpenMemberService;
import com.indo.game.services.ExternalService;
import com.indo.game.services.GameCommonService;
import com.indo.game.services.MemBaseinfoService;
import com.indo.game.services.es.ESGameService;
import com.indo.game.services.es.EsbetOrderService;
import com.indo.game.utils.AGUtil;
import com.indo.game.utils.SnowflakeIdWorker;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author eric
 * @Description: 电竞游戏业务服务类
 */
@Service
public class ESGameServiceImpl implements ESGameService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${http.proxy.hostName}")
    private String hostName;
    @Value("${http.proxy.port}")
    private int port;

    @Autowired
    private OpenAPIProperties openAPIProperties;
    @Autowired
    private RedissonClient redissonClient;
    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private EsbetOrderService esbetOrderService;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService cptOpenMemberService;
    /**
     * 页数
     */
    private static final int pageSize = 100;
    /**
     * 页码 默认字符 0
     */
    private static final String pageNo = "0";

    @Autowired
    private MemBaseinfoService memBaseinfoService;

    @Override
    public Result<String> go(LoginInfo loginUser, String ip) {
        logger.info("eslog {} go：Come here numbers  account:{}", loginUser.getId(), loginUser.getNickName());
        // 是否开售校验
        Integer lotteryId = CaipiaoTypeEnum.ES_GAME.getIntegerTagType();
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        String key = Constants.ES_ACCOUNT_TYPE + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.error("eslog {} go increment error ", loginUser.getId());
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.ES_ACCOUNT_TYPE + "_" + loginUser.getId() + loginUser.getNickName();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
            Result<String> result = null;
            if (bool) {
                logger.info("eslog {} ESGameServiceImpl go run start ...  account {}", loginUser.getId(), loginUser.getNickName());
                MemBaseinfo xiazhuren = memBaseinfoService.getUserByAccno(loginUser.getAccount());
                if (xiazhuren == null) {
                    logger.error("eslog {} member is null member:{}", loginUser.getId(), xiazhuren);
                    return null;
                }
                String esAccount = null;
                String esPassword = null;
                Long uid = xiazhuren.getId();
                // 验证且绑定（电竞-CPT第三方会员关系）
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.ES_ACCOUNT_TYPE);
                String goGameUrl = null;
                if (cptOpenMember == null) {
                    logger.info("eslog {}  cptOpenMember is null  cptOpenMember:{}", loginUser.getId(), cptOpenMember);
                    cptOpenMember = new CptOpenMember();
                    esAccount = openAPIProperties.platformUserPrefix + uid;
                    esPassword = AGUtil.MD5Encode(esAccount + Constants.AG_USER_MD5_KEY, Charsets.UTF_8.name()).substring(0, 16);
                    cptOpenMember.setUsername(esAccount);
                    cptOpenMember.setPassword(esPassword);
                    JSONObject resultRegister = register(cptOpenMember, ip);
                    if (resultRegister != null && resultRegister.get("Code").equals(200)) {
                        cptOpenMember.setUserId(uid.intValue());
                        cptOpenMember.setCreateTime(new Date());
                        cptOpenMember.setLoginTime(new Date());
                        cptOpenMember.setType(Constants.ES_ACCOUNT_TYPE);
                        externalService.saveCptOpenMember(cptOpenMember);
                    }
                } else {
                    logger.info("eslog {} select cptOpenMember go run start cptOpenMember:{}", cptOpenMember);
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                }
                // 登入
                JSONObject resultLogin = login(cptOpenMember, ip);
                if (resultLogin != null && resultLogin.get("Code").equals(200)) {
                    JSONObject dataJson = JSONObject.parseObject(resultLogin.get("Data").toString());
                    goGameUrl = dataJson.get("RedirectUrl").toString();
                }
                if (StringUtils.isNotBlank(goGameUrl)) {
                    result = new Result<String>();
                    result.setData(goGameUrl);
                }
                logger.info("eslog {} ESGameServiceImpl go run end ...  account:{} appMember:{}", loginUser.getId(), loginUser.getNickName(), xiazhuren);
            }
            return result;
        } catch (Exception e) {
            logger.error("eslog {} ESGameServiceImpl go occur error.  account:{}", loginUser.getId(), loginUser.getNickName(), e);
            return Result.failed(MessageUtils.get("thesystemisbusy"));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Async
    @Override
    public void initAccountInfo(LoginInfo loginUser, String ip) {
        // 是否开售校验
        Integer lotteryId = CaipiaoTypeEnum.ES_GAME.getIntegerTagType();
        if (!gameCommonService.isGameEnabled(lotteryId)) {
            return;
        }
        String key = CaipiaoTypeEnum.ES_GAME.getTagType() + "_" + loginUser.getId();
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 30, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("eslog {} initAccountInfo  isLocked {} return thread", loginUser.getId(), key);
            return;
        }

        String initKey = Constants.ES_INIT_KEY + loginUser.getId();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
            if (bool) {
                // 自动转入余额
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.ES_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.info("eslog {}  initAccountInfo cptOpenMember is null... cptOpenMember:{}", loginUser.getId(), cptOpenMember);
                    return;
                }
                // 自动转入  修改
                MemBaseinfo xiazhuren = memBaseinfoService.selectById(loginUser.getId());
                if (xiazhuren == null) {
                    logger.error("eslog {} initAccountES appMember is null:" + xiazhuren);
                    return;
                }
                if (xiazhuren.getBalance().intValue() <= 0) {
                    logger.error("eslog {} initAccountES appMember balance {} is lt=; zero", loginUser.getId(), xiazhuren.getBalance());
                    return;
                }
                String orderNo = SnowflakeIdWorker.createOrderSn();
                //先扣款
                gameCommonService.inOrOutBalanceCommon(-1, BigDecimal.valueOf(xiazhuren.getBalance()), xiazhuren, MessageUtils.get("ttesg") + orderNo,
                        cptOpenMember, Constants.ES_ACCOUNT_TYPE);

                JSONObject transferResult = initGame(orderNo, BigDecimal.valueOf(xiazhuren.getBalance()), cptOpenMember, ip);
                //失败则把钱加上
                if (transferResult != null && !transferResult.get("Code").equals(200)) {
                    logger.info("eslog {} esGameInit orderNo {}  result {}",
                            xiazhuren.getId(), orderNo, transferResult);

                    gameCommonService.inOrOutBalanceCommon(1, BigDecimal.valueOf(xiazhuren.getBalance()), xiazhuren, MessageUtils.get("esato") + orderNo,
                            cptOpenMember, Constants.ES_ACCOUNT_TYPE);
                } else {
                    int checkOrderNum = 0;
                    while (checkOrderNum <= 3) {
                        logger.info("eslog {} esGameInit orderNo {} ", xiazhuren.getId(), orderNo);
                        JSONObject status = getOrderNo(orderNo, cptOpenMember, ip);
                        logger.info("{}eslog {} aeGameInit orderNo {} result {}",
                                xiazhuren.getId(), orderNo, JSONObject.toJSONString(status));
                        if (status != null && status.get("Code").equals(200)) {
                            JSONObject dataJson = JSONObject.parseObject(status.get("Data").toString());
                            if (dataJson.getBigDecimal("OrderAmount").compareTo(BigDecimal.ZERO) == 1) {
                                break;
                            } else {
                                //失败则加回去
                                logger.info("eslog {} esGameInit error orderNo {}  result {}",
                                        xiazhuren.getId(), orderNo, dataJson);
                                gameCommonService.inOrOutBalanceCommon(1, BigDecimal.valueOf(xiazhuren.getBalance()), xiazhuren, MessageUtils.get("esato"),
                                        cptOpenMember, Constants.ES_ACCOUNT_TYPE);
                                break;
                            }
                            //异常返回null  继续请求  直到返回成功失败
                        } else if (null == status) {
                            checkOrderNum++;
                            TimeUnit.SECONDS.sleep(1);
                        }
                    }
                }
                //标识有进入游戏
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_IN);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            }
        } catch (Exception e) {
            logger.error("eslog {} initAccountInfo occur error:  ip:{},e:{}", loginUser.getId(), ip, e);
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * 转出电竞余额 返回string:0，业务校验不通过，余额无变动 1，余额有变动
     */
    @Override
    public Result<String> exit(LoginInfo loginUser, String ip) {
        Long memid = loginUser.getId();
        logger.info("eslog {}  exit start account:{} ip:{}", loginUser.getId(), loginUser.getNickName(), ip);
        String key = Constants.ES_EXIT_KEY + memid;
        //long total = redisTemplate.opsForValue().increment(key, 1);
        long total = RedisBaseUtil.increment(key, 1);
        RedisBaseUtil.setExpire(key, 3, TimeUnit.SECONDS);
        if (total > 1) {
            logger.info("eslog {} exit isLocked {} return thread", memid, key);
            return Result.failed(MessageUtils.get("sdctf"));
        }

        String initKey = Constants.ES_ACCOUNT_TYPE + "_" + loginUser.getId() + loginUser.getNickName();
        RReadWriteLock lock = redissonClient.getReadWriteLock(initKey);
        try {
            boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
            if (bool) {
                CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), Constants.ES_ACCOUNT_TYPE);
                if (cptOpenMember == null) {
                    logger.info("eslog {}  exit cptOpenMember is null", memid);
                    return Result.success();
                }
                MemBaseinfo memBaseinfo = memBaseinfoService.selectById(loginUser.getId());
                if (memBaseinfo == null) {
                    logger.error("eslog {} exit memBaseinfo is null:" + memBaseinfo);
                    Result.failed(MessageUtils.get("tnibptal"));
                }


                BigDecimal balance = BigDecimal.ZERO;
                //查询余额
                JSONObject getBalance = getBalance(cptOpenMember, ip);
                if (getBalance != null && getBalance.get("Code").equals(200)) {
                    JSONObject dataJson = JSONObject.parseObject(getBalance.get("Data").toString());
                    balance = dataJson.getBigDecimal("Balance");
                }

                if (balance.compareTo(BigDecimal.ZERO) == 1) {
                    String orderNo = SnowflakeIdWorker.createOrderSn();
                    JSONObject transferResult = exitGame(orderNo, balance, cptOpenMember, ip);
                    if (transferResult != null && transferResult.get("Code").equals(200)) {
                        //成功加上余额
                        gameCommonService.inOrOutBalanceCommon(1, balance, memBaseinfo, MessageUtils.get("esato") + orderNo,
                                cptOpenMember, Constants.ES_ACCOUNT_TYPE);
                    } else {
                        int checkOrderNum = 0;
                        while (checkOrderNum <= 3) {
                            logger.info("eslog {} autoXf orderNo {} ", memBaseinfo.getId(), orderNo);
                            JSONObject status = getXfOrderNo(orderNo, cptOpenMember, ip);
                            logger.info("eslog {} autoXf orderNo {} result {}", memBaseinfo.getId(), orderNo, JSONObject.toJSONString(status));
                            if (status != null && status.get("Code").equals(200)) {
                                JSONObject dataJson = JSONObject.parseObject(status.get("Data").toString());
                                if (dataJson.getBigDecimal("OrderAmount").compareTo(BigDecimal.ZERO) == 1) {
                                    //成功加上余额
                                    gameCommonService.inOrOutBalanceCommon(1, balance, memBaseinfo, MessageUtils.get("esato") + orderNo,
                                            cptOpenMember, Constants.ES_ACCOUNT_TYPE);
                                    break;
                                }
                                //异常返回null  继续请求  直到返回成功失败
                            } else if (null == status) {
                                checkOrderNum++;
                                TimeUnit.SECONDS.sleep(1);
                            }
                        }
                    }
                }
                //标识游戏下分
                CptOpenMember gameOpenMember = new CptOpenMember();
                gameOpenMember.setLayerNo(Constants.THIRD_GAME_OUT);
                gameOpenMember.setId(cptOpenMember.getId());
                cptOpenMemberService.updateByPrimaryKeySelective(gameOpenMember);
            }
            return Result.success();
        } catch (Exception e) {
            logger.error("eslog {} exit occur error.  account:{} ip:{}", loginUser.getId(), loginUser.getNickName(), ip, e);
            return Result.success();
        } finally {
            lock.writeLock().unlock();
        }
    }


    /***
     * 注册
     * @param gameOpenMember
     * @return
     */
    public JSONObject register(CptOpenMember gameOpenMember, String ip) {
        JSONObject resultRegister = null;
        try {
            // 注册电竞账号
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("UserName", gameOpenMember.getUsername());
            params.add("RoleId", "0");
            params.add("Password", gameOpenMember.getPassword());
            params.add("NickName", gameOpenMember.getUsername());
            params.add("Timestamp", timestamp);
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            resultRegister = commonInvoking(OpenAPIProperties.ES_API_URL_REGISTER, params, ip, gameOpenMember.getUserId(), HttpMethod.POST, "register");
        } catch (Exception e) {
            logger.error("eslog {} register  error：{} ", gameOpenMember.getUserId(), e);
            return resultRegister;
        }
        return resultRegister;
    }

    /**
     * 登录
     */
    public JSONObject login(CptOpenMember gameOpenMember, String ip) {
        JSONObject resultLogin = null;
        try {
            // 注册电竞账号
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("UserName", gameOpenMember.getUsername());
            params.add("AppId", "1");
            params.add("DeviceType", "H5");
            params.add("ClientIp", ip);
            params.add("Timestamp", timestamp);
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            resultLogin = commonInvoking(OpenAPIProperties.ES_API_URL_LOGIN, params, ip, gameOpenMember.getUserId(), HttpMethod.POST, "esGame");
        } catch (Exception e) {
            logger.error("eslog{} login   error{} ", gameOpenMember.getUserId(), e);
            return resultLogin;
        }
        return resultLogin;
    }

    /**
     * 上分
     */
    public JSONObject initGame(String orderNo, BigDecimal money, CptOpenMember gameOpenMember, String ip) {
        JSONObject transferResult = null;
        try {
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("OutTradeNo", gameOpenMember.getUsername());
            params.add("Timestamp", timestamp);
            params.add("Amount", money + "");
            params.add("OutTradeNo", orderNo);
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            transferResult = commonInvoking(OpenAPIProperties.ES_API_URL_DEPOSIT, params, ip, gameOpenMember.getUserId(), HttpMethod.POST, "initAccountInfo");
        } catch (Exception e) {
            logger.error("eslog {}  inItGame orderNo {} error {}", gameOpenMember.getUserId(), orderNo, e);
            return transferResult;
        }
        return transferResult;
    }

    /**
     * 查询上分订单信息
     */
    public JSONObject getOrderNo(String orderNo, CptOpenMember gameOpenMember, String ip) {
        JSONObject orderResult = null;
        try {
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("OutTradeNo", orderNo + "");
            params.add("Timestamp", timestamp);
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            orderResult = commonInvoking(OpenAPIProperties.ES_API_URL_QUERYDEPOSIT,
                    params, ip, gameOpenMember.getUserId(), HttpMethod.POST, "getOrderNo");
        } catch (Exception e) {
            logger.error("eslog {}  getOrderNo orderNo {} error {}", gameOpenMember.getUserId(), orderNo, e);
            return orderResult;
        }
        return orderResult;
    }

    /**
     * 查询余额
     */
    private JSONObject getBalance(CptOpenMember gameOpenMember, String ip) {
        JSONObject queryBalanceResult = null;
        try {
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            // 先查询余额
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("UserName", gameOpenMember.getUsername());
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Timestamp", timestamp);
            Map<String, String> md5ResultMap = MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            String method = OpenAPIProperties.ES_API_URL_QUERYMEMBERBALANCE + md5ResultMap.get("params") + "&Sign=" + md5ResultMap.get("sign");
            queryBalanceResult = commonInvoking(method, params, ip, gameOpenMember.getUserId(), HttpMethod.GET, "queryBalance");
        } catch (Exception e) {
            logger.error("eslog {}  getBalance error {}", gameOpenMember.getUserId(), e);
            return queryBalanceResult;
        }
        return queryBalanceResult;
    }

    /**
     * 下分
     */
    public JSONObject exitGame(String orderNo, BigDecimal money, CptOpenMember gameOpenMember, String ip) {
        JSONObject xfResult = null;
        try {
            // 转出余额
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> transferOutParams = new LinkedMultiValueMap<String, String>();
            transferOutParams.add("UserName", gameOpenMember.getUsername());
            transferOutParams.add("Amount", money + "");
            transferOutParams.add("Timestamp", timestamp);
            transferOutParams.add("OutTradeNo", orderNo + "");
            transferOutParams.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            transferOutParams.add("Sign", MD5Util.sign(transferOutParams, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            xfResult = commonInvoking(OpenAPIProperties.ES_API_URL_WITHDRAWAL,
                    transferOutParams, ip, gameOpenMember.getUserId(), HttpMethod.POST, "exit");
        } catch (Exception e) {
            logger.error("eslog {} exitGame orderNo {} error {}", gameOpenMember.getUserId(), orderNo, e);
            return xfResult;
        }
        return xfResult;
    }

    /**
     * 下分订单查询
     */
    public JSONObject getXfOrderNo(String orderNo, CptOpenMember gameOpenMember, String ip) {
        JSONObject orderResult = null;
        try {
            String timestamp = DateUtils.format(new Date(),DateUtils.newFormat);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("OutTradeNo", orderNo + "");
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            params.add("Timestamp", timestamp);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            orderResult = commonInvoking(OpenAPIProperties.ES_API_URL_QUERYWITHDRAWAL, params,
                    ip, gameOpenMember.getUserId(), HttpMethod.POST, "getXfOrderNo");
        } catch (Exception e) {
            logger.error("eslog {}  getXfOrderNo orderNo {} error {}", gameOpenMember.getUserId(), orderNo, e);
            return orderResult;
        }
        return orderResult;
    }


    /**
     * 统一请求
     *
     * @return String
     */
    public JSONObject commonInvoking(String method, MultiValueMap<String, String> params, String ip, Integer uid, HttpMethod httpMethod, String type) {
        JSONObject resultBody = null;
        try {
            String apiUlr = OpenAPIProperties.ES_API_URL + method;
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setReadTimeout(60000);
            requestFactory.setConnectTimeout(60000);
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port)));
            RestTemplate client = new RestTemplate(requestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            ResponseEntity<String> response = client.exchange(apiUlr, httpMethod, requestEntity, String.class);
            // 订单数据长度过长，不记录返回信息
            resultBody = JSONObject.parseObject(response.getBody());
            logger.info("eslog commonInvoking type:{} method:{} params:{} uid:{} ip:{} resultBody:{}", type, method, JSONObject.toJSONString(params), uid, ip, resultBody);
        } catch (Exception e) {
            logger.error("esgame invoking api is error. type:{} method:{} params:{} uid:{} ip:{} ,exception:{}", type, method, JSONObject.toJSONString(params), uid, ip, e);
            return resultBody;
        }
        return resultBody;
    }


    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public void pullOrder() {
        try {
            // 查询注单
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            // params.add("UserName", cptOpenMember.getUsername());
            params.add("Timestamp", DateUtils.format(new Date(),DateUtils.newFormat));
            params.add("ApiKey", OpenAPIProperties.ES_API_APIKEY);
            Map<String, String> md5ResultMap = MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY);
            params.add("Sign", MD5Util.sign(params, OpenAPIProperties.ES_API_SECRETKEY).get("sign"));
            // 拼接请求
            StringBuilder buliber = new StringBuilder();
            buliber.append("/external/querysettledbetrecords?");
            buliber.append(md5ResultMap.get("params"));
            buliber.append("&Sign=");
            buliber.append(md5ResultMap.get("sign"));

            JSONObject betOrders = commonInvoking(buliber.toString(), params, "127.0.0.1", 0, HttpMethod.GET, "pullOrder");
            if (betOrders != null && betOrders.get("Code").equals(200)) {
                HashMap<String, BigDecimal> queueHashMap = new HashMap<String, BigDecimal>();
                List<EsBetOrder> betOrderList = new ArrayList<EsBetOrder>();
                List<Map<String, Object>> listJsonMaps = (List<Map<String, Object>>) betOrders.get("Data");
                for (int i = 0; i < listJsonMaps.size(); i++) {
                    EsBetOrder betOrder = new EsBetOrder();
                    Map<String, Object> itemMap = listJsonMaps.get(i);
                    itemMap.forEach((key, val) -> {
                        if ("Id".equals(key)) {
                            betOrder.setEsOrderId(String.valueOf(val));
                        }
                        if ("MemberUserId".equals(key)) {
                            betOrder.setEsUserId((Integer) val);
                        }
                        if ("MemberUserName".equals(key)) {
                            betOrder.setUsername(String.valueOf(val));
                            betOrder.setUserId(Integer.parseInt(betOrder.getUsername().substring(betOrder.getUsername().lastIndexOf("V") + 1)));
                        }
                        if ("TotalAmount".equals(key)) {
                            betOrder.setTotalAmount((BigDecimal) val);
                        }
                        if ("OddsValue".equals(key)) {
                            betOrder.setOddsValue((BigDecimal) val);
                        }
                        if ("CanWinAmount".equals(key)) {
                            betOrder.setCanWinAmount((BigDecimal) val);
                        }
                        if ("ConfirmStatus".equals(key)) {
                            betOrder.setConfirmStatus((Integer) val);
                        }
                        if ("SettlementStatus".equals(key)) {
                            betOrder.setSettlementStatus((Integer) val);
                        }
                        if ("SettlementAmount".equals(key)) {
                            betOrder.setSettlementAmount(String.valueOf(val));
                        }
                        if ("SettlementRatio".equals(key)) {
                            betOrder.setSettlementRatio(String.valueOf(val));
                        }
                        if ("SettlementTime".equals(key)) {
                            try {
                                betOrder.setSettlementTime(DateUtils.parseDateNoTime(String.valueOf(val),DateUtils.newFormat));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if ("WinLoseState".equals(key)) {
                            betOrder.setWinLoseState(Integer.parseInt(String.valueOf(val)));
                        }
                        if ("CreateTime".equals(key)) {
                            try {
                                betOrder.setCreateTime(DateUtils.parseDateNoTime(String.valueOf(val),DateUtils.newFormat));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    betOrderList.add(betOrder);
                    // 累计玩家投注额度
                    if (queueHashMap.get(betOrder.getUsername()) == null) {
                        queueHashMap.put(betOrder.getUsername(),
                                new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    queueHashMap.replace(betOrder.getUsername(),
                            queueHashMap.get(betOrder.getUsername()).add(betOrder.getTotalAmount()));
                }
                if (betOrderList.size() > 0) {
                    esbetOrderService.insertBatch(betOrderList);
                    // 电竞有效投注额推送
                }
            }
            logger.info("esPullOrder pullOrder end ...");
        } catch (Exception e) {
            logger.error("esPullOrder occur error.", e);
            throw new RuntimeException(e.getMessage());
        }

    }

}
