package com.indo.admin.modules.chongzhika.service.impl;

import com.indo.common.result.Result;
import com.indo.core.mapper.chongzhika.CardInfoMapper;
import com.indo.admin.modules.chongzhika.service.ICardInfoService;
import com.indo.core.pojo.entity.chongzhika.CardInfo;
import com.indo.admin.pojo.req.chongzhika.CardInfoReq;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CardInfoServiceImpl implements ICardInfoService {
    @Autowired
    private CardInfoMapper cardInfoMapper;

    /**
     * 批量新增卡
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result insertCardInfoBatch(CardInfoReq cardInfoReq, String countryCode){
        Result result = new Result();

            Long serialNumber = 0l;
            if(cardInfoReq.getCardNoSerial()!=null&&!"".equals(cardInfoReq.getCardNoSerial())){
                serialNumber = Long.valueOf(cardInfoReq.getCardNoSerial());
            }
            int total = cardInfoReq.getTotal();
            String msg = this.verifyTotal(cardInfoReq, countryCode);
            if(null!=msg&&!"".equals(msg)){
                result.failed(msg);
                return result;
            }
            CardInfo maxCardInfo = cardInfoMapper.selectMaxId(cardInfoReq.getCardNoPrefix());
            if(null!=maxCardInfo){
                if(serialNumber>0){
                    if(maxCardInfo.getCardNoPrefix()!=null&&!"".equals(maxCardInfo.getCardNoPrefix())){
                        if(maxCardInfo.getCardNoSerial()!=null&&!"".equals(maxCardInfo.getCardNoSerial())){
                            if(Long.valueOf(maxCardInfo.getCardNoSerial())>=Long.valueOf(serialNumber)){
                                result.failed(MessageUtils.get("a100004",countryCode)+"（"+serialNumber+"）"+MessageUtils.get("a100005",countryCode)+"（"+maxCardInfo.getCardNoSerial()+"）");
                                return result;
                            }
                        }
                    }
                }else {
                    result.failed(MessageUtils.get("a100006",countryCode));
                    return result;
                }

            }
            List<CardInfo> cardInfoAllLists = new ArrayList<CardInfo>();

            int cycles = total/10000;//循环次数,最大单批新增1W笔
            int remainder = total%10000;
            if(remainder>0){
                cycles = cycles + 1;
            }
            Set<String> cardNumberSet;
            Object[] objects = new Object[0];
            if (null!=cardInfoReq.getLetterNumber()&&cardInfoReq.getLetterNumber().length>0){
                cardNumberSet = this.getCardNumber(cardInfoReq.getLetterNumber(),Integer.valueOf(total),cardInfoReq.getLetterNumberLength());
                objects = (Object[])cardNumberSet.toArray();
            }

            int l = 0;
            for (int c=0;c<cycles;c++){
                List<CardInfo> cardInfoLists = new ArrayList<CardInfo>();
                if(c==cycles-1&&remainder>0){
                    total = remainder;
                }else {
                    total = 10000;
                }
                for (int i=0;i<total;i++) {
                    CardInfo cardInfo = new CardInfo();
                    cardInfo.setUserId(1L);
                    String cardNoSerial = "";
                    if(serialNumber>0){
                        serialNumber = serialNumber + i;
                        cardNoSerial = this.getCardNoSerial(cardInfoReq.getSerialLength(), serialNumber);
                        cardInfo.setCardNoSerial(cardNoSerial);//序号
                    }
                    cardInfo.setCardNoPrefix(cardInfoReq.getCardNoPrefix());//前缀
                    String letterNumber = "";
                    if(null!=objects&&objects.length>0){
                        letterNumber = (String) objects[l];
                    }
                    cardInfo.setCardNo(cardInfoReq.getCardNoPrefix()+letterNumber+cardNoSerial);
                    l = l + 1;
                    String cardPwd = this.getCardPassword(cardInfoReq.getPwdLetterNumber(),cardInfoReq.getPwdLength());
                    cardInfo.setCardPwd(cardPwd);
                    cardInfo.setCardAmount(cardInfoReq.getCardAmount());//卡面金额
                    cardInfo.setAdditionalAmount(cardInfoReq.getAdditionalAmount());//增送金额
                    cardInfo.setIsExp("0");
                    cardInfo.setIs_delete("0");
                    cardInfo.setIsActivation("0");//0否   1激活
                    cardInfo.setIsHandle("0");//0否   1处理
                    cardInfo.setCreate_time(DateUtils.getDateTime(DateUtils.newFormat));
                    cardInfoLists.add(cardInfo);
                }
                cardInfoAllLists.addAll(cardInfoLists);
                cardInfoMapper.insertCollectList(cardInfoLists);
            }

//            result.failed(MessageUtils.get("a100007",countryCode));
        result.success(cardInfoAllLists);

        return result;
    }
    public Result selectCardInfoByisActivation(String countryCode){
        Result result = new Result();

        try {
            List<CardInfo> cardInfoLists = cardInfoMapper.selectCardInfoByisActivationAndIsHandle("0","1","1");
//            result.failed(MessageUtils.get("a100008",countryCode));
            result.success(cardInfoLists);
        } catch (Exception e) {
            result.failed(MessageUtils.get("a100009",countryCode));
            e.printStackTrace();
        }
        return result;
    }

    public Result selectCardNoPrefix(String countryCode){
        Result result = new Result();

        try {
            List<String> prefixLists = cardInfoMapper.selectCardNoPrefix();
//            result.failed(MessageUtils.get("a100010",countryCode));
            result.success(prefixLists);
        } catch (Exception e) {
            result.failed(MessageUtils.get("a100011",countryCode));
            e.printStackTrace();
        }
        return result;
    }

    private String getCardNoSerial(int strLength,Long number){
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(strLength);
        format.setGroupingUsed(false);
        String numberStr = format.format(number);
//        System.out.println(numberStr);
        return numberStr;
    }
    private String getCardPassword(String pwdLetterNumber[],int digits){
        String str = "";
        Random random = new Random();
        if(pwdLetterNumber.length==2){//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
            char arr[] = new char[digits];
            int i = 0;
            while (i < digits) {
                char ch = (char) (int) (Math.random() * 124);
                if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
                    arr[i++] = ch;
                }
            }
            str = String.valueOf(arr);
        }else {
            if ("0".equals(pwdLetterNumber[0])){
                char arr[] = new char[digits];
                int i = 0;
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
            }else {
                char arr[] = new char[digits];
                int i = 0;
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= '0' && ch <= '9') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
            }
        }
        return  str;
    }
    private Set<String> getCardNumber(String letterNumberLength[],int total,int digits){

        Set r = new LinkedHashSet(total);
        Random random = new Random();

        if(letterNumberLength.length==2){//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
            while (r.size()<total){
                char arr[] = new char[digits];
                int i = 0;
                String str = "";
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
                r.add(str);
            }
        }else {
            if ("0".equals(letterNumberLength[0])){
                while (r.size()<total){
                    char arr[] = new char[digits];
                    int i = 0;
                    String str = "";
                    while (i < digits) {
                        char ch = (char) (int) (Math.random() * 124);
                        if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                            arr[i++] = ch;
                        }
                    }
                    str = String.valueOf(arr);
                    r.add(str);
                }
            }else {
                while (r.size()<total){
                    char arr[] = new char[digits];
                    int i = 0;
                    String str = "";
                    while (i < digits) {
                        char ch = (char) (int) (Math.random() * 124);
                        if (ch >= '0' && ch <= '9') {
                            arr[i++] = ch;
                        }
                    }
                    str = String.valueOf(arr);
                    System.out.println(str);
                    r.add(str);
                }
            }
        }
        return r;
    }
    public String verifyTotal(CardInfoReq cardInfoReq,String countryCode){
        int total = cardInfoReq.getTotal();
        if(total>100000){
            return MessageUtils.get("a100012",countryCode);
        }
        int letterNumberLength = cardInfoReq.getLetterNumberLength();
        if (null!=cardInfoReq.getLetterNumber()&&cardInfoReq.getLetterNumber().length>0) {
            if (1 == letterNumberLength) {
                if (total > 10) {
                    return MessageUtils.get("a100013",countryCode);
                }
            }
            if (2 == letterNumberLength) {
                if (total > 100) {
                    return MessageUtils.get("a100014",countryCode);
                }
            }
            if (3 == letterNumberLength) {
                if (total > 1000) {
                    return MessageUtils.get("a100015",countryCode);
                }
            }
            if (4 == letterNumberLength) {
                if (total > 10000) {
                    return MessageUtils.get("a100016",countryCode);
                }
            }
            if (5 == letterNumberLength) {
                if (total > 100000) {
                    return MessageUtils.get("a100017",countryCode);
                }
            }
            if (6 == letterNumberLength) {
                if (total > 1000000) {
                    return MessageUtils.get("a100018",countryCode);
                }
            }
        }
        if(null!=cardInfoReq.getCardNoSerial()&&!"".equals(cardInfoReq.getCardNoSerial())){
            if (1 == cardInfoReq.getSerialLength()) {
                if (total > 10) {
                    return MessageUtils.get("a100019",countryCode);
                }
            }
            if (2 == cardInfoReq.getSerialLength()) {
                if (total > 100) {
                    return MessageUtils.get("a100020",countryCode);
                }
            }
            if (3 == cardInfoReq.getSerialLength()) {
                if (total > 1000) {
                    return MessageUtils.get("a100021",countryCode);
                }
            }
            if (4 == cardInfoReq.getSerialLength()) {
                if (total > 10000) {
                    return MessageUtils.get("a100022",countryCode);
                }
            }
            if (5 == cardInfoReq.getSerialLength()) {
                if (total > 100000) {
                    return MessageUtils.get("a100023",countryCode);
                }
            }
            if (6 == cardInfoReq.getSerialLength()) {
                if (total > 1000000) {
                    return MessageUtils.get("a100024",countryCode);
                }
            }
        }
        return "";
    }
}
