package com.indo.admin.modules.pay.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.pay.mapper.PayTakeCashMapper;
import com.indo.admin.modules.pay.service.IPayTakeCashService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.api.WithdrawFeignClient;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import com.indo.pay.pojo.req.PayTakeCashReq;
import com.indo.user.pojo.bo.MemTradingBO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayTakeCashServiceImpl extends ServiceImpl<PayTakeCashMapper, PayTakeCash> implements IPayTakeCashService {

    @Resource
    private DozerUtil dozerUtil;
    @Resource
    private WithdrawFeignClient withdrawFeignClient;
    @Resource
    private MemBaseinfoMapper memBaseinfoMapper;

    @Override
    public Page<PayTakeCashApplyVO> cashApplyList(PayTakeCashReq req) {
        Page<PayTakeCashApplyVO> applyPage = new Page<>(req.getPage(), req.getLimit());
        List<PayTakeCashApplyVO> list = this.baseMapper.cashApplyList(applyPage, req);
        applyPage.setRecords(list);
        return applyPage;
    }

    @Override
    public Result<List<PayTakeCashRecordVO>> cashRecordList(PayTakeCashReq cashOrderDTO) {
        Page<PayTakeCash> page = new Page<>(cashOrderDTO.getPage(), cashOrderDTO.getLimit());
        LambdaQueryWrapper<PayTakeCash> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cashOrderDTO.getOrderNo())) {
            wrapper.eq(PayTakeCash::getOrderNo, cashOrderDTO.getOrderNo());
        }
        if (cashOrderDTO.getOrderStatus() != null) {
            wrapper.eq(PayTakeCash::getCashStatus, cashOrderDTO.getOrderStatus());
        }
        if (cashOrderDTO.getAccount() != null) {
            LambdaQueryWrapper<MemBaseinfo> baseinfoWrapper = new LambdaQueryWrapper<>();
            baseinfoWrapper.eq(MemBaseinfo::getAccount,cashOrderDTO.getAccount());
            MemBaseinfo memBaseinfo =memBaseinfoMapper.selectOne(baseinfoWrapper);
            if(memBaseinfo!=null){
                wrapper.eq(PayTakeCash::getMemId, memBaseinfo.getId());
            }
        }
        if (cashOrderDTO.getBeginAmount() != null) {
            wrapper.ge(PayTakeCash::getActualAmount, cashOrderDTO.getBeginAmount());
        }
        if (cashOrderDTO.getEndAmount() != null) {
            wrapper.le(PayTakeCash::getActualAmount, cashOrderDTO.getEndAmount());
        }
        if (cashOrderDTO.getBeginTime() != null) {
            wrapper.ge(PayTakeCash::getCreateTime, cashOrderDTO.getBeginTime());
        }
        if (cashOrderDTO.getEndTime() != null) {
            wrapper.le(PayTakeCash::getCreateTime, cashOrderDTO.getEndTime());
        }
        Page<PayTakeCash> pageList = baseMapper.selectPage(page, wrapper);
        List<PayTakeCashRecordVO> result = dozerUtil.convert(pageList.getRecords(), PayTakeCashRecordVO.class);
        return Result.success(result, page.getTotal());
    }

    @Override
    public boolean takeCashOpera(AudiTypeEnum audiTypeEnum, Long takeCashId) {
        PayTakeCash payTakeCash = this.baseMapper.selectById(takeCashId);
        if (ObjectUtil.isEmpty(payTakeCash)) {
            throw new BizException("提现订单不存在");
        }
        if (!payTakeCash.getCashStatus().equals(GlobalConstants.PAY_CASH_STATUS_PENDING)) {
            throw new BizException("提现订单状态错误");
        }
        if (audiTypeEnum.name().equals(AudiTypeEnum.reject)) {
            payTakeCash.setCashStatus(GlobalConstants.PAY_CASH_STATUS_REJECT);
        } else {
            payTakeCash.setCashStatus(GlobalConstants.PAY_CASH_STATUS_OK);
            PayTakeCashBO payTakeCashBO = new PayTakeCashBO();
            DozerUtil.map(payTakeCash, payTakeCashBO);
            Result<Boolean> result = withdrawFeignClient.withdrawRequest(payTakeCashBO);
            if (Result.success().getCode().equals(result.getCode())) {
                Boolean flag = result.getData();
                if (!flag.equals(true)) {
                    throw new BizException("提现处理失败!");
                } else {
                    return true;
                }
            } else {
                throw new BizException("takeCashOpera No client with requested ");
            }

        }
        return this.baseMapper.updateById(payTakeCash) > 0;
    }
}
