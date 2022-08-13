package com.indo.core.service.game.s128;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.service.game.common.GameCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import sun.reflect.generics.tree.VoidDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.applet.AppletContext;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * PG
 *
 * @author
 */
@Slf4j
@Service
public class S128TaskServiceImpl implements IS128TaskService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    public static void main(String[] args) {
        //系统当前时间
        long totalMilliSeconds = System.currentTimeMillis();
        long fiveMilli = 60*1000*5;
        long thirtyMilli = 60*1000*25;
        System.out.println("当前时间："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds),DateUtils.newFormat2));
        System.out.println("开始："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds-fiveMilli),DateUtils.newFormat2));
        System.out.println("结束："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds+thirtyMilli),DateUtils.newFormat2));
        new S128TaskServiceImpl().is128SettleTask();
    }
    /**
     * 获取结算
     */
    public void is128SettleTask() {
        try {
            StringBuilder apiUrl = new StringBuilder();
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", OpenAPIProperties.DJ_API_KEY);
            params.put("agent_code", OpenAPIProperties.DJ_AGENT_CODE);
//            params.put("api_key", "7F3C78F8ECC04798893262105631A0F2");
//            params.put("agent_code", "VRJ00101");
            //系统当前时间
            long totalMilliSeconds = System.currentTimeMillis();
            long fiveMilli = 60*1000*5;
            long thirtyMilli = 60*1000*25;
            System.out.println("当前时间："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds),DateUtils.newFormat2));
            System.out.println("开始："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds-fiveMilli),DateUtils.newFormat2));
            System.out.println("结束："+ DateUtils.format(DateUtils.getDate(totalMilliSeconds+thirtyMilli),DateUtils.newFormat2));
            params.put("start_datetime", "2022-08-11 18:30");
            params.put("end_datetime", "2022-08-11 19:00");
            apiUrl.append(OpenAPIProperties.DJ_API_URL).append("/get_cockfight_processed_ticket_by_bet_time.aspx");
//            apiUrl.append("https://api8745.cfb2.net").append("/get_cockfight_processed_ticket_by_bet_time.aspx");
            String result = commonRequest(apiUrl.toString(), params, 0, "gameLogin");
            logger.info("is128SettleTask斗鸡定时拉取结算任务 result:{}", result);
            if (!StringUtils.isEmpty(result)) {
                Document doc = commonXml(result);
                String errorCode = doc.getElementsByTagName("status_code").item(0).getTextContent();
                String data = doc.getElementsByTagName("data").item(0).getTextContent();
                System.out.println("data:"+data);
                if (StringUtils.isNotEmpty(result) && errorCode.equals("00")) {
                    if(StringUtils.isNotEmpty(data)){
                        String[] arr = data.split("\\|");
                        System.out.println("arr:"+arr);
                        for(int i=0;i< arr.length;i++){
                            System.out.println("arr【】:"+arr[i]);
                            String[] settleData = arr[i].split("\\,");
                            System.out.println("settleData【】:"+settleData);
                            String ticket_id = settleData[0];//注单号码 ticket_id Integer
                            String login_id = settleData[1];//登录帐号 login_id String 30
                            String arena_code = settleData[2];//赛场编号 arena_code String 10
                            String arena_name_cn = settleData[3];//赛场名中文名字 arena_name_cn String 10
                            String match_no = settleData[4];//赛事编号 match_no String 10
                            String match_type = settleData[5];//赛事类型 match_type String 20 OPENFIGH/TOURNAMENT
                            String match_date = settleData[6];//赛事日期 match_date Datetime
                            String fight_no = settleData[7];//日场次 fight_no Integer
                            String fight_datetime = settleData[8];//赛事时间 fight_datetime Datetime
                            String meron_cock = settleData[9];//龍斗鸡 meron_cock String 20
                            String meron_cock_cn = settleData[10];//龍斗鸡中文名字 meron_cock_cn String 10
                            String wala_cock = settleData[11];//鳳斗鸡 wala_cock String 20
                            String wala_cock_cn = settleData[12];//鳳斗鸡中文名字 wala_cock_cn String 10
                            String bet_on = settleData[13];//投注 bet_on String 10 MERON/WALA/BDD/FTD
                            String odds_type = settleData[14];//赔率类型 odds_type String 10 MY/HK/ EU
                            String odds_asked = settleData[15];//要求赔率 odds_asked Decimal 5,3
                            String odds_given = settleData[16];//给出赔率 odds_given Decimal 5,3
                            String stake = settleData[17];//投注金额 stake Integer
                            String stake_money = settleData[18];//奖金 stake_money Decimal 14,4
                            String balance_open = settleData[19];//转账前余额 balance_open Decimal 14,4
                            String balance_close = settleData[20];//转账后余额 balance_close Decimal 14,4
                            String created_datetime = settleData[21];//创建时间 created_datetime Datetime
                            String fight_result = settleData[22];//赛事结果 fight_result String 10 MERON/WALA/BDD/FTD
                            String status = settleData[23];//状态 status String 10 WIN/LOSE/REFUND/CANCEL/VOID
                            String winloss = settleData[24];//输赢 winloss Decimal 14,4
                            String comm_earned = settleData[25];//所得佣金 comm_earned Decimal 14,4
                            String payout = settleData[26];//派彩 payout Decimal 14,4
                            String balance_open1 = settleData[27];//转账前余额 balance_open1 Decimal 14,4
                            String balance_close1 = settleData[28];//转账后余额 balance_close1 Decimal 14,4
                            String processed_datetime = settleData[29];//处理时间 processed_datetime Datetime
                            String tax_money = settleData[30];//税 tax_money Decimal 8,4
                            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                            wrapper.eq(Txns::getMethod, "Place Bet");
                            wrapper.eq(Txns::getStatus, "Running");
                            wrapper.eq(Txns::getPlatformTxId, ticket_id);
                            wrapper.eq(Txns::getPlatform, OpenAPIProperties.DJ_PLATFORM_CODE);
                            Txns oldTxns = txnsMapper.selectOne(wrapper);
                            if(null!=oldTxns){
                                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
                                BigDecimal balance = memBaseinfo.getBalance();
                                BigDecimal betAmount = BigDecimal.valueOf(null!=stake&&!"".equals(stake)?Double.valueOf(stake):0L)
                                        .add(BigDecimal.valueOf(null!=stake_money&&!"".equals(stake_money)?Double.valueOf(stake_money):0L));

                                if (betAmount.compareTo(BigDecimal.ZERO) != 0) {
                                    if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
                                        balance = balance.subtract(betAmount.abs());
                                        gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                                    } else {
                                        balance = balance.add(betAmount);
                                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                                    }
                                }

                                Txns txns = new Txns();
                                BeanUtils.copyProperties(oldTxns, txns);
                                if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
                                    txns.setWinningAmount(betAmount);
                                } else {
                                    txns.setWinningAmount(betAmount.negate());
                                }
                                txns.setWinAmount(betAmount);
                                //真实下注金额,需增加在玩家的金额
                                txns.setRealBetAmount(betAmount);
                                //返还金额 (包含下注金额)
                                //有效投注金额 或 投注面值
                                txns.setTurnover(betAmount);
                                //辨认交易时间依据
                                txns.setTxTime(processed_datetime);
                                //操作名称
                                txns.setMethod("Settle");
                                txns.setStatus("Running");
                                //余额
                                txns.setBalance(balance);
                                //创建时间
                                txns.setCreateTime(created_datetime);
                                oldTxns.setStatus("Settle");
                                txnsMapper.updateById(oldTxns);
                                int num = txnsMapper.insert(txns);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error("djlog  djlog out:{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 公共请求
     */
    public String commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, apiUrl, params, type, userId);
        logger.info("djLog commonRequest resultString:{}",resultString);
        return resultString;
    }

    public static Document commonXml(String repXML) {
        Document doc = null;
        try {
            StringReader sr = new StringReader(repXML);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            return doc;
        } catch (Exception e) {
            log.error("djlog commonXml error {}", e);
            return doc;
        }
    }

    public static String replaceAllBlank(String str) {
        String s = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\*|");
            /*\n 回车(\u000a)
            \t 水平制表符(\u0009)
            \s 空格(\u0008)
            \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }
}
