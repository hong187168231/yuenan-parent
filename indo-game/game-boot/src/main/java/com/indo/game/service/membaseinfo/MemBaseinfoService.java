//package com.indo.game.service.membaseinfo;
//
//import com.indo.user.pojo.entity.MemBaseinfo;
//
//import java.math.BigDecimal;
//
//public interface MemBaseinfoService {
//
////    void updateMemNameById(String memName, Long memId);
////
////    void updateMemBalance(BigDecimal amount, BigDecimal namount, BigDecimal payamount, String accno);
////
////    BigDecimal getOrderBetRecordAmount(List<Integer> list);
////
////    BigDecimal getOrderRobotRecordAmount(List<Integer> list);
////
////    public Integer countAvailableAppMember();
////
////    MemBaseinfo getUserByInvitecode(String recomcode);
//
//    MemBaseinfo getUserByAccno(String accno);
//
////    MemBaseinfo getUserByUniqueId(String uniqueId);
////
////    List<String> getAccnoByUniqueId(String uniqueId);
////
////    MemBaseinfo getUserByAccnoNoCache(String accno);
//
//    MemBaseinfo getMemById(Long memberId);
//
//    BigDecimal getBalanceById(Integer memberId);
////
////    MemBaseinfo getUserByInvitecodeAll(String recomcode);
////
////    void updateFansNum(UsersRequest ur);
////
//////    Integer getAttentionnum(String accno);
////
//////    Integer getCollectionnum(String accno);
////
////    Integer getResourcesnum(String accno);
////
////    void updateAddGold(UsersRequest usersRequest);
////
////    Integer updateByRedPacket(Long memid, BigDecimal goldnum, Integer auditfree);
////
////    UserGoldDO getUserRecomcodeGold(String accno);
////
////    void updateSubtractGold(UsersRequest usersRequest);
////
////    void updateAddress(Long uid, MemBaseinfo memBaseinfo);
////
////    MemBaseinfo getRepeateNickname(String nickname);
////
////    void updateAddGold(UserRequest r);
////
////    List<UserDO> findUsers(UserDO userDO);
////
////    List<UserDO> findAllUsers();
////
////    Page<MemBaseinfoDO> applyAnchorList(UserRequest req, RowBounds rowBounds);
////
////    int updatePayAgent(TraAgentclearing req);
////
////    MemBaseinfo selectByAccno(String accno);
////
////    List<String> getNickNameList(String[] arrstr);
////
////    Page<GreatPersonList> getGpList(RowBounds toRowBounds);
//
//    MemBaseinfo selectById(Long memId);
//
////    int updateMemberAmount(BigDecimal amount, BigDecimal pamount, BigDecimal bamount, BigDecimal namount, BigDecimal wamount, BigDecimal waitamount, String accno, Long userId);
//
//    int updateMemberAmount(BigDecimal amount, BigDecimal pamount, BigDecimal bamount, BigDecimal namount, BigDecimal consumeAcmount, BigDecimal wamount, BigDecimal waitamount, String accno, Long userId);
//
////    BigDecimal countAllBalanceAmount(String startTime, String endTime);
////
////    String selectAccountbyId(Long userId);
////
////    int updateMemberForbit(MemBaseinfo memBaseinfo);
////
////    BigDecimal countFirstDepositAmount(ProfitAndLossCountVO vo);
////
////    int updateWithdrawalAmount(MemBaseinfo memBaseinfo);
////
////    //mapper
////    MemBaseinfo selectOneByExample(Long uid, MemBaseinfoExample example);
//
////    MemBaseinfo selectOneByExample(MemBaseinfoExample example);
//
//    MemBaseinfo selectByPrimaryKey(Long memid);
//
////    MemBaseinfo selectByPrimaryKeyNoCache(Long memid);
//
//    int updateByPrimaryKeySelective(MemBaseinfo record);
//
////    List<MemBaseinfo> selectByExample(MemBaseinfoExample example);
////
////    int insertSelective(MemBaseinfo record);
////
////    void delUserHeadimgByPrimaryKey(Long memid);
////
////    /**
////     * 更新用户注册来源
////     */
////    int updateMemorigin(String accno, String origin);
////
////    Page<BasMemInfoStatusResp> getMemInfoStatusList(BasMemInfoStatusReq basMemInfoStatusReq, RowBounds rowBounds);
////
////    /**
////     * 历史数据处理：mem.uniqueId
////     */
////    void handMemUniqueId();
////
////    /**
////     * 根据账号列表 获取昵称、性别、头像
////     */
////    List<Map<String, Object>> getNicknameSexHeadimg(List<String> accnoList);
////
////    void updateLastLoginDev(String accno, String source);
////
////    void updateRegisterIp(String accno, String registerIp);
////
////    /**
////     * 补历史数据用  一次取出
////     * 提现总次数,首次提现金额,最大提现金额,充值总次数,首次充值金额,最大充值金额
////     */
////    void handMemWithdrawalAndPayInfo();
////
////    /**
////     * 补历史数据用  支付次数和提款次数都为0 每次处理500个
////     * 提现总次数,首次提现金额,最大提现金额,充值总次数,首次充值金额,最大充值金额
////     */
////    void handMemWithdrawalAndPayInfoZeroNum();
////
////    /**
////     * 根据会员昵称或会员唯一ID 获取会员信息
////     */
////    MemBaseinfo getUserByNicknameAndUniqueId(String nickname, String uniqueId);
////
////    /**
////     * 获取用户当前可提现次数
////     *
////     * @param accno
////     * @return
////     */
////    Integer getAllowChargeNums(String accno);
////
////    /**
////     * 超级房管 根据账号列表 获取会员昵称和会员id
////     */
////    List<Map<String, Object>> getMemNicknameAndIdByAccnos(List<String> accnoList);
//
//}
