package com.indo.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.ViewUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemBank;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.mapper.MemBankRelationMapper;
import com.indo.pay.mapper.TakeCashMapper;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.req.TakeCashApplyReq;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
import com.indo.pay.service.ITakeCashService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.bo.MemTradingBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Service
@Slf4j
public class TakeCashServiceImpl extends SuperServiceImpl<TakeCashMapper, PayTakeCash> implements ITakeCashService {

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;
    @Autowired
    private MemBaseInfoFeignClient memBaseInfoFeignClient;
    @Autowired
    private MemBankRelationMapper memBankRelationMapper;

    @Override
    public boolean takeCashApply(TakeCashApplyReq cashApplyReq, LoginInfo loginUser) {
        MemBank bridgeMemBank = new MemBank();
        //  请求参数检查
        requestConditionCheck(loginUser, cashApplyReq);
        // 业务逻辑参数校验
        logicConditionCheck(loginUser, cashApplyReq, bridgeMemBank);
        // 保存提现申请
        Long applyId = saveCashOrder(loginUser, cashApplyReq, bridgeMemBank);
        // 更新账变信息
        updateCashGoldChange(loginUser, cashApplyReq, applyId);
        return true;
    }


    /**
     * 提现申请 请求参数校验`
     *
     * @param loginInfo
     * @param cashApplyReq
     */
    public void requestConditionCheck(LoginInfo loginInfo, TakeCashApplyReq cashApplyReq) {
        // 控制频率
        cashFrequency(loginInfo);
        // 校验安全码
//        memBusiness.verifySafetyPassword(cashApplyReq.getSafetyPassword(), loginUser.getUid());
//        Integer cashNum = CashBusinessRedisUtils.getTodayCashNum(loginUser.getAcclogin());
        BigDecimal cashAmount = cashApplyReq.getTakeCashAmount();
        // 提现金额必须大于0
        if (!(cashAmount.doubleValue() > 0)) {
            //  throw new BizException(StatusCode.CASH_AMOUNT_GREATER_ZERO);
        }
        // 提现金额只能为1的倍数
        if ((cashAmount.doubleValue() % 1) > 0) {
            //  throw new BizException(StatusCode.CASH_AMOUNT_MULTIPLE_ONE);
        }
        // 提现金额不能为负数
        if (cashAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // throw new BizException(StatusCode.CASH_AMOUNT_NOT_NEGATIVE);
        }
        // 提现银行卡不能为空
        if (null == cashApplyReq.getMemBankId()) {
            //  throw new BizException(StatusCode.CASH_BANKCARD_NOT_NULL);
        }
    }


    /**
     * 提现申请业务逻辑参数校验
     *
     * @param loginUser
     * @param cashApplyReq
     */
    public void logicConditionCheck(LoginInfo loginUser, TakeCashApplyReq cashApplyReq, MemBank bridgeMemBank) {
        MemTradingBO memTradingBO = this.getMemTradingInfo(loginUser.getAccount());
        // 账户余额不足
        if (cashApplyReq.getTakeCashAmount().compareTo(memTradingBO.getBalance()) == 1) {
            throw new BizException("账户余额不足");
        }
        // 大于可提现金额
        if (cashApplyReq.getTakeCashAmount().compareTo(memTradingBO.getCanAmount()) == 1) {
            throw new BizException("提前金额大于可提现金额");
        }
        // 存在处理中的提现订单
        boolean processCashFlag = this.selectProcessCashOrder(loginUser.getId());
        if (processCashFlag) {
           throw new BizException("已存在提现中的订单");
        }
        // 获取会员提现银行卡信息
        MemBank memBank = this.selectMemBankById(cashApplyReq.getMemBankId());
        // 银行卡信息无效
        if (memBank == null || NumberUtils.toInt(memBank.getStatus() + "") == 1
                || !memBank.getAccount().equals(loginUser.getAccount())) {
            throw new BizException("银行卡无效");
        }
        MemBaseInfoBO currentMem = getMemCacheBaseInfo(loginUser.getAccount());
        if (currentMem.getProhibitDisbursement().equals(1)) {
            throw new BizException("你暂时提现,请联系管理员");
        }
        BeanUtils.copyProperties(memBank, bridgeMemBank);
    }


    @Override
    public Result<List<TakeCashRecordVO>> cashRecordList(Integer page, Integer limit, LoginInfo loginInfo) {
        Page<PayTakeCash> cashPage = new Page<>(page, limit);
        LambdaQueryWrapper<PayTakeCash> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayTakeCash::getMemId, loginInfo.getId());
        Page<PayTakeCash> pageList = baseMapper.selectPage(cashPage, wrapper);
        List<TakeCashRecordVO> result = DozerUtil.convert(pageList.getRecords(), TakeCashRecordVO.class);
        return Result.success(result, cashPage.getTotal());
    }


    /**
     * 提现频率限制
     *
     * @param loginInfo
     */
    public void cashFrequency(LoginInfo loginInfo) {
        // 控制频率
        String keySuffix = RedisKeys.CASH_FREQUENCY + loginInfo.getAccount();
        if (RedisUtils.hasKey(keySuffix)) {
            throw new BizException("提现操作太频繁,请稍后再试!");
        }
        boolean haveAuth = RedisUtils.setIfAbsent(keySuffix, "1", 1L);
        if (!haveAuth) {
            throw new BizException("提现操作太频繁,请稍后再试!");
        }

    }


    public MemBank selectMemBankById(Long memBankId) {
        LambdaQueryWrapper<MemBank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBank::getMemBankId, memBankId);
        return memBankRelationMapper.selectOne(wrapper);
    }


    public boolean selectProcessCashOrder(Long memId) {
        List<Integer> statusList = new LinkedList<>();
        statusList.add(1);
        statusList.add(2);
        LambdaQueryWrapper<PayTakeCash> wrapper = new LambdaQueryWrapper<>();
        wrapper.
                eq(PayTakeCash::getMemId, memId)
                .in(PayTakeCash::getCashStatus, statusList);
        List list = this.baseMapper.selectList(wrapper);
        return list.size() > 0;
    }

    /**
     * 保存提现申请订单
     *
     * @param loginUser
     * @param cashApplyReq
     * @param memBank
     * @return
     */
    public Long saveCashOrder(LoginInfo loginUser, TakeCashApplyReq cashApplyReq, MemBank memBank) {
        PayTakeCash orderCash = new PayTakeCash();
        orderCash.setMemId(loginUser.getId());
        orderCash.setMemBankId(memBank.getMemBankId());
        orderCash.setApplyAmount(cashApplyReq.getTakeCashAmount());
        orderCash.setBankName(memBank.getBankName());
        orderCash.setBankCardNo(memBank.getBankCardNo());
        orderCash.setBankCity(memBank.getCity());
        orderCash.setOrderNo(GeneratorIdUtil.generateId());
        orderCash.setApplyTime(new Date());
        orderCash.setCreateUser(loginUser.getAccount());
        boolean cashFlag = this.baseMapper.insert(orderCash) > 0;
        if (cashFlag) {
            return orderCash.getTakeCashId();
        } else {
            throw new BizException("提现异常");
        }

    }

    /**
     * 更新提现账变信息
     *
     * @param loginUser
     * @param cashApplyReq
     * @param applyId
     */
    public void updateCashGoldChange(LoginInfo loginUser, TakeCashApplyReq cashApplyReq, Long applyId) {
        MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
        goldChangeDO.setChangeAmount(cashApplyReq.getTakeCashAmount());
        goldChangeDO.setTradingEnum(TradingEnum.SPENDING);
        goldChangeDO.setGoldchangeEnum(GoldchangeEnum.TXKK);
        goldChangeDO.setUserId(loginUser.getId());
        goldChangeDO.setUpdateUser(loginUser.getAccount());
        goldChangeDO.setRefId(applyId);
        iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
    }

    public MemTradingBO getMemTradingInfo(String account) {
        Result<MemTradingBO> result = memBaseInfoFeignClient.getMemTradingInfo(account);
        if (Result.success().getCode().equals(result.getCode())) {
            MemTradingBO memBaseinfo = result.getData();
            return memBaseinfo;
        } else {
            throw new BizException("No client with requested id: " + account);
        }
    }


    /**
     * 公共回调订单处理成功
     */
    @Override
    @Transactional
    public boolean withdrawSuccess(PayCallBackDTO callBackDTO) {
        log.info("进入代付回调数据成功处理========================================={}==", callBackDTO.getOrderNo());
        try {
            MemBaseinfo memBaseinfo = new MemBaseinfo();//todo
            if (null != memBaseinfo) {
                Date now = new Date();
                // 根据商户订单号，查询订单信息
                QueryWrapper<PayTakeCash> query = new QueryWrapper<>();
                query.lambda().eq(PayTakeCash::getOrderNo, callBackDTO.getOrderNo());
                PayTakeCash payTakeCash = this.baseMapper.selectOne(query);

                //更新充值订单表信息
                payTakeCash.setActualAmount(ViewUtil.getTradeOffAmount(callBackDTO.getAmount()));
                payTakeCash.setCashStatus(GlobalConstants.PAY_RECHARGE_STATUS_COMPLETE);
                payTakeCash.setRemitTime(now);
                boolean flag = this.baseMapper.updateById(payTakeCash) > 0;
                if (!flag) {
                    throw new RuntimeException("修改订单为成功状态失败");
                }
                // 更新用户余额
                updateMemAmount(payTakeCash);
            }
            log.info("进入代付回调数据成功处理结束========================================={}==", callBackDTO.getOrderNo());
        } catch (Exception e) {
            log.error("withdrawSuccess occur error.", e);
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * 提现更新用户余额信息
     *
     * @param payTakeCash
     */
    public void updateMemAmount(PayTakeCash payTakeCash) {
        MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
        goldChangeDO.setChangeAmount(payTakeCash.getActualAmount());
        goldChangeDO.setTradingEnum(TradingEnum.SPENDING);
        goldChangeDO.setGoldchangeEnum(GoldchangeEnum.TXKK);
        goldChangeDO.setUserId(payTakeCash.getMemId());
        boolean changeFlag = iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
        if (!changeFlag) {
            log.info("提现回调更新用户余额失败 error param{}", JSON.toJSONString(payTakeCash));
            throw new RuntimeException("订单处理失败！");
        }
    }


}
