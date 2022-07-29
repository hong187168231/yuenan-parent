package com.indo.pay.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.IPAddressUtil;
import com.indo.pay.pojo.resp.SafeboxMoneyResp;
import com.indo.pay.pojo.resp.SafeboxRecord;
import com.indo.pay.pojo.vo.PayBankVO;
import com.indo.pay.service.IPayBankService;
import com.indo.pay.service.ISafeBoxService;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 保险箱
 *
 *
 * */
@Api(tags = "保险箱")
@RestController
@RequestMapping("/safebox")
@Slf4j
public class SafeBoxController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ISafeBoxService iSafeBoxService;

    @ApiOperation(value = "查询余额")
    @GetMapping("/lookingMoney")
    public Result lookingMoney(@LoginUser LoginInfo loginUser) {
        logger.info ("保险箱查询余额/safebox/lookingMoney 请求userid：" +  loginUser.getId().toString());
        SafeboxMoneyResp safeboxMoneyResp = iSafeBoxService.checkSafeboxBalance(loginUser.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("safeboxMoney", safeboxMoneyResp.getUserSafemoney());
        jsonObject.put("userBalance", loginUser.getBalance());
        logger.info ("保险箱查询余额/safebox/lookingMoney 返回data：" +  safeboxMoneyResp);
        return Result.success(jsonObject);
    }

    @ApiOperation(value = "转入转出金额")
    @PostMapping("/transferMoney")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changeAmount", value = "转入转出金额", required = true, paramType = "query", dataType = "int"),
    })
    public Result transferMoney(@RequestParam("changeAmount") Integer changeAmount, @LoginUser LoginInfo loginUser) {
        //判断传入值是否为0
        BigDecimal amount = new BigDecimal(changeAmount);
        if(amount.compareTo(new BigDecimal("0.00"))==0 ) {
            return Result.failed("Money Number is 0");
        }

        //判断是转入还是转出,-1小于，0等于，1大于
        boolean isInMoney = true;

        if (amount.compareTo(new BigDecimal("0.00"))==-1){
            isInMoney = false;
        }
        //获取用户余额
        BigDecimal balance = loginUser.getBalance();
        //查询获取用户保险金余额，为null则 返回0
        Long uid = loginUser.getId();
        SafeboxMoneyResp safeboxMoneyResp = iSafeBoxService.checkSafeboxBalance(uid);
        BigDecimal boxBalance = safeboxMoneyResp.getUserSafemoney();

        //比较转入转出数量看是否可以正常扣除
        if (isInMoney){
            //用户余额比较转入余额
            int i = balance.compareTo(amount);
            // 不可以,返回余额不足
            if (i == -1){
                log.error("transInMoney 调用失败");
                return Result.failed("转入余额不足");

            }
        }else{
            //保险箱余额比较转出绝对值
            BigDecimal abs = amount.abs();
            int i = boxBalance.compareTo(amount.abs());

            System.out.println("转出金额："+ abs + "，保险箱金额："+ boxBalance);
            // 不可以,返回余额不足
            if (i == -1){
                log.error("transOutMoney 调用失败");
                return Result.failed("转出余额不足");
            }
        }

        BigDecimal laterAmount = boxBalance.add(amount);

        // 构建对象进行插入
        SafeboxRecord record = new SafeboxRecord();
        record.setUserId(Integer.parseInt(uid.toString()));
        int orderType = isInMoney ? 1 : 0;
        record.setSafeOrdertype(orderType);
        record.setBeforeAmount(boxBalance);
        record.setChangeAmount(amount);
        record.setAfterAmount(laterAmount);
        //当前timestamp
        long currentSec = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentSec);
        record.setCreateTime(timestamp);
        //merge当前时间
        String orderNo = "BOX" + uid.toString() + String.valueOf(orderType) +  String.valueOf(currentSec);
        record.setOrderNumber(orderNo);

        iSafeBoxService.insertSafeboxRecord(record);

        return Result.success();
    }

    @ApiOperation(value = " 转入转出记录")
    @PostMapping("/safeboxRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true, paramType = "query", dataType = "int"),
    })
    public Result querySafeboxRecordById(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit, @LoginUser LoginInfo loginUser){

        PageHelper.startPage(page,limit);

        PageInfo<SafeboxRecord> list = new PageInfo<>(iSafeBoxService.selectSafeboxRecordById(loginUser.getId()));
        return Result.success(list);
    }
}
