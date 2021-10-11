package com.indo.admin.modules.mem.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.mapper.ManualDepositWithDrawMapper;
import com.indo.admin.modules.mem.service.IManualDepositWithDrawService;
import com.indo.admin.utils.OrderIdUtils;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.ManualDepositWithDrawDto;
import com.indo.user.pojo.entity.ManualDepositWithDraw;
import com.indo.user.pojo.vo.ManualDepositWithDrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:11
 * @Version: 1.0.0
 * @Desc:
 */
@Service
public class ManualDepositWithDrawServiceImpl extends SuperServiceImpl<ManualDepositWithDrawMapper, ManualDepositWithDraw> implements IManualDepositWithDrawService {

    @Autowired
    private ManualDepositWithDrawMapper manualDepositWithDrawMapper;

    //@Autowired
    //private DepositSender depositSender;


    @Override
    public List<ManualDepositWithDrawVO> queryList(Page<ManualDepositWithDrawVO> page, ManualDepositWithDrawDto dto) {
        return manualDepositWithDrawMapper.queryList(page, dto);
    }

    @Override
    public Result insert(ManualDepositWithDrawDto dto) {

        if(Objects.isNull(dto.getAmount())) dto.setAmount(new BigDecimal("0"));

        // 写入人工存提记录
        Arrays.stream(dto.getMemIds().split("/")).forEach(memId -> {

            ManualDepositWithDraw manualDepositWithDraw = new ManualDepositWithDraw();
            manualDepositWithDraw.setOrderId(OrderIdUtils.getOrderId());
            manualDepositWithDraw.setMemId(Long.parseLong(memId));
            manualDepositWithDraw.setAmount(dto.getAmount());
            manualDepositWithDraw.setOperationType(dto.getOperationType());
            manualDepositWithDraw.setRemark(dto.getRemark());
            manualDepositWithDraw.setServiceFee(new BigDecimal("0"));

            // 误存提取校验金额是否足够, 是否需要扣除服务手续费 TODO
            if("2".equals(dto.getOperationType())){

            }

            JSONObject json = new JSONObject();
            json.putOpt("orderId", manualDepositWithDraw.getOrderId());
            //depositSender.send(json.toString());




            manualDepositWithDrawMapper.insertManualDepositWithDraw(manualDepositWithDraw);

            // todo
            // 根据存提不同类型进行会员剩余金额数据处理

        });


        return Result.success();
    }
}
