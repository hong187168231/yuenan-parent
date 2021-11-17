package com.indo.game.mapper;

import com.indo.game.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemBaseinfoMapperExt {

//    BigDecimal getOrderBetRecordAmount(List<Integer> list);
//
//    BigDecimal getOrderRobotRecordAmount(List<Integer> list);
//
//    MemBaseinfo getUserByAccnoForSources(String authorAccno);
//
//    MemBaseinfo getUserByInvitecode(String recomcode);

    MemBaseinfo getUserByAccno(String accno);

//    /**
//     * 根据用户id查询用户信息
//     */
//    @Select("SELECT * FROM mem_baseinfo where memid = #{memberId} LIMIT 1")
//    MemBaseinfo getMemById(@Param("memberId") Integer memberId);

    @Select("SELECT goldnum FROM mem_baseinfo where memid = #{memberId} LIMIT 1")
    BigDecimal getBalanceById(@Param("memberId") Integer memberId);
//
//    /**
//     * 所有人
//     */
//    MemBaseinfo getUserByInvitecodeAll(String recomcode);
//
//    /**
//     * 更新粉丝数
//     */
//    Integer updateFansNum(UsersRequest ur);
//
////    /**
////     * 关注数
////     */
////    Integer getAttentionnum(String accno);
//
////    /**
////     * 收藏数
////     *
////     * @param accno
////     * @return
////     */
////    Integer getCollectionnum(String accno);
//
//    /**
//     * 获取资源数
//     */
//    Integer getResourcesnum(String accno);
//
//    /**
//     * 更新金币 加
//     */
//    Integer updateAddGold(UsersRequest usersRequest);
//
//    /**
//     * 抢红包更新
//     *
//     * @param memid
//     * @param goldnum
//     * @return
//     */
//    int updateByRedPacket(@Param("memid") Long memid, @Param("goldnum") BigDecimal goldnum, @Param("auditfree") Integer auditfree);
//
//    Integer updateMemBalance(@Param("amount") BigDecimal amount, @Param("namount") BigDecimal namount, @Param("payamount") BigDecimal payamount, @Param("accno") String accno);
//
//    /**
//     * 查询改用户是否填写的邀请码 ，如果填写邀请码，检查上级是否意见返了金币 ，若没有，加金币
//     */
//    UserGoldDO getUserRecomcodeGold(String accno);
//
//    /**
//     * 更新金币 减
//     */
//    Integer updateSubtractGold(UsersRequest usersRequest);
//
//    Integer updateAddress(MemBaseinfo memBaseinfo);
//
//    MemBaseinfo getRepeateNickname(String nickname);
//
//    Integer updateAddGold(UserRequest r);
//
//    List<UserDO> findUsers(UserDO userDO);
//
//    List<UserDO> findAllUsers();
//
//    Page<MemBaseinfoDO> applyAnchorList(UserRequest req, RowBounds rowBounds);
//
//    int updatePayAgent(TraAgentclearing req);
//
//    MemBaseinfo selectByAccno(String accno);
//
//    List<String> getNickNameList(String[] arrstr);
//
//    Page<GreatPersonList> getGpList(RowBounds toRowBounds);
//
//    /**
//     * 根据ID获取会员信息
//     */
//    MemBaseinfo selectById(Long memId);
//
//    /**
//     * 修改用户余额,等等金额
//     *
//     * @param amount 余额变动值，等等金额（正为增加，负为减少）
//     * @param accno  用户账号
//     */
//    int updateMemberAmount(@Param("amount") BigDecimal amount, @Param("pamount") BigDecimal pamount, @Param("bamount") BigDecimal bamount, @Param("namount") BigDecimal namount, @Param("wamount") BigDecimal wamount, @Param("waitamount") BigDecimal waitamount, @Param("accno") String accno);

    /**
     * 修改用户余额,等等金额
     *
     * @param amount 余额变动值，等等金额（正为增加，负为减少）
     * @param accno  用户账号
     */
    int updatePersonalFinancialInfo(@Param("amount") BigDecimal amount, @Param("pamount") BigDecimal pamount, @Param("bamount") BigDecimal bamount, @Param("namount") BigDecimal namount, @Param("consumeAcmount") BigDecimal consumeAcmount, @Param("wamount") BigDecimal wamount, @Param("waitamount") BigDecimal waitamount, @Param("accno") String accno);

//    /**
//     * 统计所有用户总余额
//     */
//    BigDecimal countAllBalanceAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);
//
//    @Select("SELECT accno from mem_baseinfo where memid = #{userId}")
//    String selectAccountbyId(@Param("userId") Integer userId);
//
//    /**
//     * 更新用户禁止状态
//     */
//    int updateMemberForbit(MemBaseinfo memBaseinfo);
//
//    BigDecimal countFirstDepositAmount(ProfitAndLossCountVO vo);
//
//    //int updateWithdrawalAmount(@Param("withdrawalAmount") BigDecimal sumamt, @Param("accno") String accno, @Param("updateUser") String updateUser);
//    int updateWithdrawalAmount(MemBaseinfo memBaseinfo);
//
//    @Update("update mem_baseinfo set headimg=null where memid = #{memid}")
//    int delUserHeadimgByPrimaryKey(@Param("memid") Long memid);
//
//    @Update("update mem_baseinfo set memorgin=#{origin} where accno = #{accno}")
//    int updateMemorigin(@Param("accno") String accno, @Param("origin") String origin);
//
//    Page<BasMemInfoStatusResp> getMemInfoStatusList(BasMemInfoStatusReq basMemInfoStatusReq, RowBounds rowBounds);
//
//    @Select("select memid, unique_id uniqueId from mem_baseinfo where unique_id ='0' order by memid limit #{offset},#{size}")
//    List<MemBaseinfo> selectNonUniqueIdMembers(@Param("offset") int offset, @Param("size") int size);
//
//    Integer updateMemUniqueId(@Param("data") Map<Long, String> data);
//
//    /**
//     * 根据账号列表 获取昵称、性别、头像
//     */
//    List<Map<String, Object>> getNicknameSexHeadimg(@Param("accnoList") List<String> accnoList);
//
//    @Update("update mem_baseinfo set last_login_dev=#{source} where accno = #{accno}")
//    int updateMemLastLoginDev(@Param("accno") String accno, @Param("source") String source);
//
//    MemBaseinfo selectByUniqueId(@Param("memaccount") String memaccount);
//
//    String selectUniqueIdByAccno(@Param("accno") String accno);
//
//    BigDecimal countAllUserAmount();
//
//    List<String> getAccnoByUniqueId(@Param("uniqueId") String uniqueId);
//
//    /**
//     * 根据会员昵称或会员唯一ID 获取会员信息
//     */
//    MemBaseinfo getUserByNicknameAndUniqueId(@Param("nickname") String nickname, @Param("uniqueId") String uniqueId);
//
//    /**
//     * 获取用户当日可提现次数
//     *
//     * @param accno
//     * @return
//     */
//    Integer getAllowChargeNums(@Param("accno") String accno);
//
//    /**
//     * 超级房管 根据账号列表 获取会员昵称和会员id
//     */
//    List<Map<String, Object>> getMemNicknameAndIdByAccnos(@Param("accnoList") List<String> accnoList);

}
