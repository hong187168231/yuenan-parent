package com.indo.admin.modules.mem.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemCapitalMapper;
import com.indo.admin.modules.mem.mapper.MemGiftReceiveMapper;
import com.indo.admin.modules.mem.service.IMemGiftReceiveService;
import com.indo.admin.modules.mem.service.IMemTradingService;
import com.indo.admin.pojo.dto.CapitalDTO;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.admin.pojo.vo.mem.MemTradingVO;
import com.indo.common.enums.ChangeCategoryEnum;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.core.pojo.entity.MemGiftReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 活动类型表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Service
public class MemTradingServiceImpl implements IMemTradingService {

    @Autowired
    private MemCapitalMapper memCapitalMapper;


    @Override
    public Page<MemTradingVO> capitalList(CapitalDTO queryDto) {
        if (ObjectUtil.isNotNull(queryDto.getChangeCategory())) {
            if(queryDto.getChangeTypes()==null){
                queryDto.setChangeTypes(new HashSet<>());
            }
            queryDto.getChangeTypes().add(queryDto.getChangeCategory().getCode());
        }
        Page<MemTradingVO> page = new Page<>(queryDto.getPage(), queryDto.getLimit());
        List<MemTradingVO> list = memCapitalMapper.capitalList(page, queryDto);
//        list.forEach(item -> {
//            if (ObjectUtil.isNotNull(queryDto.getChangeCategory())) {
//                item.setTradingType();
//            }
//
//        });
        page.setRecords(list);
        return page;
    }


//    public Set<Integer> CategoryToType(ChangeCategoryEnum changeCategoryEnum) {
//        Set<Integer> changeTypes = new HashSet<>();
//        if (changeCategoryEnum.equals(ChangeCategoryEnum.recharge)) {
//            changeTypes.add(GoldchangeEnum.CZ.getCode());
//        }
//        if (changeCategoryEnum.equals(ChangeCategoryEnum.withdraw)) {
//            changeTypes.add(GoldchangeEnum.TXKK.getCode());
//        }
//        if (changeCategoryEnum.equals(ChangeCategoryEnum.activity)) {
//            changeTypes.add(GoldchangeEnum.SUPERLOTTO.getCode());
//        }
//        if (changeCategoryEnum.equals(ChangeCategoryEnum.vip)) {
//            changeTypes.add(GoldchangeEnum.everyday.getCode());
//            changeTypes.add(GoldchangeEnum.week.getCode());
//            changeTypes.add(GoldchangeEnum.month.getCode());
//            changeTypes.add(GoldchangeEnum.birthday.getCode());
//            changeTypes.add(GoldchangeEnum.year.getCode());
//        }
//        if (changeCategoryEnum.equals(ChangeCategoryEnum.activity)) {
//            changeTypes.add(GoldchangeEnum.SUPERLOTTO.getCode());
//        }
//        return changeTypes;
//    }
}
