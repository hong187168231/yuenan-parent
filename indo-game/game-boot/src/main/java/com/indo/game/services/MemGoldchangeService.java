package com.indo.game.services;

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
import com.github.pagehelper.Page;
import com.indo.game.pojo.entity.MemGoldchange;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lucien
 * @create 2020/6/20 14:53
 */
public interface MemGoldchangeService {
    /**
//     * 新增代理结算帐变
//     *
//     * @param mg
//     * @return
//     */
//    int insertAuto(MemGoldchange mg);
//
//    /**
//     * 主播获得打赏帐变
//     *
//     * @param userReq
//     * @return
//     */
//    List<UserResp> getTopTenStatistics(UserReq userReq);
//
//    /**
//     * 土豪打赏帐变
//     *
//     * @param userReq
//     * @return
//     */
//    List<UserResp> getRoomBigMoneyTop(UserReq userReq);
//
//    /**
//     * 查询家族当月是否已经提现
//     *
//     * @param param
//     * @return
//     */
//    List<FamilyMemGoldchangeResponse> isFamilyTiXian(FamilyIncarnateRequest param);
//
//    /**
//     * 家族的总收入 总支出
//     *
//     * @param req
//     * @return
//     */
//    List<FamilyIncomeAndExpensesResponse> familyIncomeAndExpensesList(FamilyIncarnateRequest req);
//
//    /**
//     * 当月该家族长
//     *
//     * @param memGoldchangeParam
//     * @return
//     */
//    MemGoldchange findFamilyIsIncarnate(MemGoldchange memGoldchangeParam);
//
//    /**
//     * 新增：计算从每个主播処获取的金币 和 金额 提现
//     *
//     * @param param
//     * @return
//     */
//    int doFamilyIncarnateMemGoldchange(Map<String, Object> param);
//
//    /**
//     * 邀请新用户加金币帐变
//     *
//     * @param memGoldchange
//     * @return
//     */
//    int insertSelectiveMemGoldchange(MemGoldchange memGoldchange);
//
//    /**
//     * 分页获取帐变记录
//     *
//     * @param req
//     * @param toRowBounds
//     * @return
//     */
//    Page<MemGoldchangeDTO> myIncomeAndExpensesList(UsersRequest req, RowBounds toRowBounds);
//
//    /**
//     * 会员播币
//     */
//    Integer getcountbyrmtype(Map<String, Object> param);
//
//    /**
//     * 累计收益(播币)
//     *
//     * @param param
//     * @return
//     */
//    Double getAllQuantityByType(UsersRequest param);
//
//    /**
//     * 主播
//     * 累计收益(播币)
//     *
//     * @param changetype3
//     * @param accno
//     * @return
//     */
//    Double getAllQuantity(Integer changetype3, String accno);
//
//    /**
//     * 插入金币变动数据
//     *
//     * @param memGoldchange
//     * @return
//     */
//    int insertSelectiveSubtractMemGoldchange(MemGoldchange memGoldchange);
//
//    /**
//     * 获取縂充值的乐比 数量 金额
//     *
//     * @param accno
//     * @return
//     */
//    Double getZongchongzhi(String accno);
//
//    /**
//     * 后台统计-帐变列表-总数
//     *
//     * @param accno
//     * @param typeList
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    BigDecimal tatolGoldchange(String refaccno, String accno, List<Integer> typeList, String startTime, String endTime);
//
//    /**
//     * 后台统计-出入账目汇总
//     */
//    List<Map<String, Object>> statisticsAllType(String startTime, String endTime);
//
//    /**
//     * 后台统计-出入账目汇总  充值：1在线支付 2线下支付
//     */
//    List<Map<String, Object>> statisticsIncomeRecharge(String startTime, String endTime);
//
//    /**
//     * 送礼 + 下注
//     *
//     * @param xiaofeiparam
//     * @return
//     */
//    List<MemGoldchange> getAllGoldchangeByType(UsersRequest xiaofeiparam);
//
//    /**
//     * 修改 金币变化记录表 将用户申请提现的记录 改为 状态已提现
//     *
//     * @param paramMemGoldchange
//     * @return
//     */
//    int updateZhuboTixian(MemGoldchange paramMemGoldchange);
//
    /**
     * 插入金币变动数据
     *
     * @param goldchange
     * @return
     */
    int insertSelective(MemGoldchange goldchange);

//    /**
//     * 统计帐变
//     *
//     * @param example
//     * @return
//     */
//    Integer countByExample(MemGoldchangeExample example);
//
//    /**
//     * 获取帐变记录
//     *
//     * @param example
//     * @return
//     */
//    List<MemGoldchange> selectByExample(MemGoldchangeExample example);
//
//    /**
//     * 修改 金币变化记录表 将用户申请提现的记录 改为 状态已提现
//     *
//     * @param memGoldchange
//     * @return
//     */
//    int updateByPrimaryKeySelective(MemGoldchange memGoldchange);
//
//    /**
//     * 根据订单获取帐变记录
//     *
//     * @param orderid
//     * @param accno
//     * @return
//     */
//    MemGoldchange selectOneByExample(Long orderid, String accno);
//
//    /**
//     * 获取家族长帐变
//     *
//     * @param req
//     * @return
//     */
//    List<FamilyMemIncarnateResponse> findFamilyTiXian(FamilyIncarnateRequest req);
//
//    /**
//     * 获取总数量
//     *
//     * @param f
//     * @return
//     */
//    Double getSumQuantity(FamilyStatisticsRequest f);
//
//    /**
//     * 获得Nengti金币
//     *
//     * @param accno
//     * @return
//     */
//    Double getNengtiGoldNum(String accno);
//
//    /**
//     * 插入家族长Goldchange
//     *
//     * @param memGoldchange
//     * @return
//     */
//    int insertSelectiveFamilyGoldchange(MemGoldchange memGoldchange);
//
//    /**
//     * 获得实时收入
//     *
//     * @param dashangReq
//     * @return
//     */
//    double getLiveincome(UserReq dashangReq);
//
////    /**
////     * 根据账号获得帐变总数  列表
////     */
////    List<Map<String, Object>> getTotalGoldsByAccnos(List<String> accnoList);
//
////    /**
////     * 根据账号获得昨日帐变总数  列表
////     */
////    List<Map<String, Object>> getTotalYesterdayGoldsByAccnos(List<String> accnoList, String startTime, String endTime);
//
//    DepositStatisticsDO selectSumPeople(String startTime, String endTime, Integer value);
//
//    /**
//     * 获取条件下充值账变记录
//     *
//     * @param changetypeList
//     * @param accnoList
//     * @param minCreatedate
//     * @param maxCreatedate
//     * @return
//     */
//    List<MemGoldchange> findByChangetypeInAndAccnoIn(List<Integer> changetypeList, List<String> accnoList, Date minCreatedate, Date maxCreatedate);
//
//    /**
//     * 获取用户当前所需打码量
//     *
//     * @param accno
//     * @return
//     */
//    BigDecimal getNowCgdmlByAccno(String accno);
}
