package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemBank;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.mapper.MemBankRelationMapper;
import com.indo.pay.mapper.TakeCashMapper;
import com.indo.pay.pojo.req.TakeCashApplyReq;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
import com.indo.pay.service.ITakeCashService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.bo.MemTradingBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//            throw new BizException(StatusCode.ACCOUNT_BALANCE_DEFICIENCY);
        }
        // 大于可提现金额
        if (cashApplyReq.getTakeCashAmount().compareTo(memTradingBO.getCanAmount()) == 1) {
//            throw new BizException(StatusCode.DOT_MEET_CAN_AMOUNT);
        }
        // 存在处理中的提现订单
        boolean processCashFlag = this.selectProcessCashOrder(loginUser.getId());
        if (processCashFlag) {
//            throw new BizException(StatusCode.EXIST_CASH_ORDER);
        }
        // 获取会员提现银行卡信息
        MemBank memBank = this.selectMemBankById(cashApplyReq.getMemBankId());
        // 银行卡信息无效
        if (memBank == null || memBank.getStatus().equals(0)
                || !memBank.getAccount().equals(loginUser.getAccount())) {
//            throw new BizException(StatusCode.CASH_BANK_INVALID);
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
//        if (RedisLock.hasKey(keySuffix)) {
//            throw new BusinessException(StatusCode.OPERATION_FREQUENT);
//        }
//        boolean haveAuth = RedisLock.setIfAbsent(keySuffix, "1", 1, TimeUnit.MINUTES);
//        if (!haveAuth) {
//            throw new BusinessException(StatusCode.OPERATION_FREQUENT);
//        }

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
        orderCash.setBankCity(memBank.getBankCity());
        orderCash.setOrderNo("TX" + SnowflakeIdWorker.createOrderSn());
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


}
