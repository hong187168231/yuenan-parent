package com.indo.game.services.impl;

//import com.caipiao.live.common.enums.GoldchangeEnum;
//import com.caipiao.live.common.model.dto.member.MemGoldchangeDTO;
//import com.caipiao.live.common.model.dto.report.DepositStatisticsDO;
//import com.caipiao.live.common.model.request.FamilyIncarnateRequest;
//import com.caipiao.live.common.model.request.FamilyStatisticsRequest;
//import com.caipiao.live.common.model.request.UserReq;
//import com.caipiao.live.common.model.request.UsersRequest;
//import com.caipiao.live.common.model.response.FamilyIncomeAndExpensesResponse;
//import com.caipiao.live.common.model.response.FamilyMemGoldchangeResponse;
//import com.caipiao.live.common.model.response.FamilyMemIncarnateResponse;
//import com.caipiao.live.common.model.response.UserResp;
//import com.caipiao.live.common.mybatis.entity.MemGoldchange;
//import com.caipiao.live.common.mybatis.entity.MemGoldchangeExample;
//import com.caipiao.live.common.mybatis.mapper.MemGoldchangeMapper;
//import com.caipiao.live.common.mybatis.mapperext.member.MemGoldchangeMapperExt;
//import com.caipiao.live.common.service.money.MemGoldchangeService;
import com.github.pagehelper.Page;
import com.indo.game.mapper.MemGoldchangeMapper;
import com.indo.game.pojo.entity.MemGoldchange;
import com.indo.game.services.MemGoldchangeService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lucien
 * @create 2020/6/20 14:53
 */
@Service
public class MemGoldchangeServiceImpl implements MemGoldchangeService {

    @Autowired
    private MemGoldchangeMapper memGoldchangeMapper;
//    @Autowired
//    private MemGoldchangeMapperExt memGoldchangeMapperExt;
//
//    @Override
//    public Integer getcountbyrmtype(Map<String, Object> param) {
//        return memGoldchangeMapperExt.getcountbyrmtype(param);
//    }
//
//    @Override
//    public int insertSelectiveMemGoldchange(MemGoldchange memGoldchange) {
//        return memGoldchangeMapperExt.insertSelectiveMemGoldchange(memGoldchange);
//    }
//
//    @Override
//    public int updateZhuboTixian(MemGoldchange paramMemGoldchange) {
//        return memGoldchangeMapperExt.updateZhuboTixian(paramMemGoldchange);
//    }

    @Override
    public int insertSelective(MemGoldchange goldchange) {
        return memGoldchangeMapper.insertSelective(goldchange);
    }

//    @Override
//    public Integer countByExample(MemGoldchangeExample example) {
//        return memGoldchangeMapper.countByExample(example);
//    }
//
//    @Override
//    public List<MemGoldchange> selectByExample(MemGoldchangeExample example) {
//        return memGoldchangeMapper.selectByExample(example);
//    }
//
//    @Override
//    public int updateByPrimaryKeySelective(MemGoldchange memGoldchange) {
//        return memGoldchangeMapper.updateByPrimaryKeySelective(memGoldchange);
//    }
//
//    @Override
//    public MemGoldchange selectOneByExample(Long orderid, String accno) {
//        MemGoldchangeExample example = new MemGoldchangeExample();
//        MemGoldchangeExample.Criteria criteria = example.createCriteria();
//        criteria.andRefidEqualTo(orderid);
//        criteria.andAccnoEqualTo(accno);
//        MemGoldchange memGoldchange = memGoldchangeMapper.selectOneByExample(example);
//        return null == memGoldchange ? new MemGoldchange() : memGoldchange;
//    }
//
//    @Override
//    public List<FamilyMemGoldchangeResponse> isFamilyTiXian(FamilyIncarnateRequest param) {
//        return memGoldchangeMapperExt.isFamilyTiXian(param);
//    }
//
//    @Override
//    public List<FamilyIncomeAndExpensesResponse> familyIncomeAndExpensesList(FamilyIncarnateRequest req) {
//        return memGoldchangeMapperExt.familyIncomeAndExpensesList(req);
//    }
//
//    @Override
//    public MemGoldchange findFamilyIsIncarnate(MemGoldchange memGoldchangeParam) {
//        return memGoldchangeMapperExt.findFamilyIsIncarnate(memGoldchangeParam);
//    }
//
//    @Override
//    public int doFamilyIncarnateMemGoldchange(Map<String, Object> param) {
//        return memGoldchangeMapperExt.doFamilyIncarnateMemGoldchange(param);
//    }
//
//    @Override
//    public List<FamilyMemIncarnateResponse> findFamilyTiXian(FamilyIncarnateRequest req) {
//        return memGoldchangeMapperExt.findFamilyTiXian(req);
//    }
//
//    @Override
//    public Double getZongchongzhi(String accno) {
//        return memGoldchangeMapperExt.getZongchongzhi(accno);
//    }
//
//    @Override
//    public Double getSumQuantity(FamilyStatisticsRequest f) {
//        return memGoldchangeMapperExt.getSumQuantity(f);
//    }
//
//    @Override
//    public int insertAuto(MemGoldchange record) {
//        return memGoldchangeMapperExt.insertAuto(record);
//    }
//
//    @Override
//    public List<UserResp> getTopTenStatistics(UserReq userReq) {
//        return memGoldchangeMapperExt.getTopTenStatistics(userReq);
//    }
//
//    @Override
//    public List<UserResp> getRoomBigMoneyTop(UserReq userReq) {
//        return memGoldchangeMapperExt.getRoomBigMoneyTop(userReq);
//    }
//
//    @Override
//    public double getLiveincome(UserReq dashangReq) {
//        return memGoldchangeMapperExt.getLiveincome(dashangReq);
//    }
//
//    @Override
//    public int insertSelectiveSubtractMemGoldchange(MemGoldchange memGoldchange) {
//        return memGoldchangeMapperExt.insertSelectiveSubtractMemGoldchange(memGoldchange);
//    }
//
//    @Override
//    public Double getAllQuantity(Integer changetype, String accno) {
//        return memGoldchangeMapperExt.getAllQuantity(changetype, accno);
//    }
//
//    @Override
//    public Double getNengtiGoldNum(String accno) {
//        return memGoldchangeMapperExt.getNengtiGoldNum(accno);
//    }
//
//    @Override
//    public List<MemGoldchange> getAllGoldchangeByType(UsersRequest xiaofeiparam) {
//        return memGoldchangeMapperExt.getAllGoldchangeByType(xiaofeiparam);
//    }
//
//    @Override
//    public Page<MemGoldchangeDTO> myIncomeAndExpensesList(UsersRequest req, RowBounds toRowBounds) {
//        return memGoldchangeMapperExt.myIncomeAndExpensesList(req, toRowBounds);
//    }
//
//    @Override
//    public int insertSelectiveFamilyGoldchange(MemGoldchange memGoldchange) {
//        return memGoldchangeMapperExt.insertSelectiveFamilyGoldchange(memGoldchange);
//    }
//
//    @Override
//    public Double getAllQuantityByType(UsersRequest param) {
//        return memGoldchangeMapperExt.getAllQuantityByType(param);
//    }
//
//    @Override
//    public BigDecimal tatolGoldchange(String refaccno, String accno, List<Integer> typeList, String startTime, String endTime) {
//        return memGoldchangeMapperExt.tatolGoldchange(refaccno, accno, typeList, startTime, endTime);
//    }
//
//    @Override
//    public List<Map<String, Object>> statisticsAllType(String startTime, String endTime) {
//        return memGoldchangeMapperExt.statisticsAllType(startTime, endTime);
//    }
//
//    @Override
//    public List<Map<String, Object>> statisticsIncomeRecharge(String startTime, String endTime) {
//        return memGoldchangeMapperExt.statisticsIncomeRecharge(startTime, endTime);
//    }
//
////    @Override
////    public List<Map<String, Object>> getTotalGoldsByAccnos(List<String> accnoList) {
////        return memGoldchangeMapperExt.getTotalGoldsByAccnos(accnoList);
////    }
//
////    @Override
////    public List<Map<String, Object>> getTotalYesterdayGoldsByAccnos(List<String> accnoList, String startTime, String endTime) {
////        return memGoldchangeMapperExt.getTotalYesterdayGoldsByAccnos(accnoList, startTime, endTime);
////    }
//
//    @Override
//    public DepositStatisticsDO selectSumPeople(String startTime, String endTime, Integer value) {
//        return memGoldchangeMapperExt.selectSumPeople(startTime, endTime, value);
//    }
//
//    @Override
//    public List<MemGoldchange> findByChangetypeInAndAccnoIn(List<Integer> changetypeList, List<String> accnoList, Date minCreatedate, Date maxCreatedate) {
//        return memGoldchangeMapperExt.findByChangetypeInAndAccnoIn(changetypeList, accnoList, minCreatedate, maxCreatedate);
//    }
//
//    @Override
//    public BigDecimal getNowCgdmlByAccno(String accno) {
//        return memGoldchangeMapperExt.getNowCgdmlByAccno(accno);
//    }
}
