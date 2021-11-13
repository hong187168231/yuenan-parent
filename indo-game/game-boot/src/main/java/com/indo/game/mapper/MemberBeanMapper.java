package com.indo.game.mapper;


import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


public interface MemberBeanMapper {


    /**
     * 修改用户余额,等等金额
     *
     * @param amount 余额变动值，等等金额（正为增加，负为减少）
     * @param userId 用户id
     */
    @Update("<script>" + "UPDATE `mem_baseinfo` m SET m.`goldnum` = m.`goldnum` + #{amount} , m.`wait_amount` = " +
            "(CASE WHEN m.`wait_amount` + #{waitamount} &lt; 0 THEN 0 WHEN m.`wait_amount` + #{waitamount} &gt;= 0 THEN m.`wait_amount` + #{waitamount} END) , " +
            "m.`pay_amount` = m.`pay_amount` + #{pamount},  m.`bet_amount` = m.`bet_amount` + #{bamount},  m.`no_withdrawal_amount` = (CASE WHEN m.`no_withdrawal_amount` + #{namount} &gt; 0 THEN m.`no_withdrawal_amount` + #{namount} WHEN m.`no_withdrawal_amount` + #{namount} &lt;= 0 THEN 0 END ) " +
            ",  m.`withdrawal_amount` = m.`withdrawal_amount` + #{wamount}" +
            " WHERE m.`memid` = #{userId} "
            + "</script>")
    void updateMemberAmount(@Param("amount") BigDecimal amount, @Param("pamount") BigDecimal pamount, @Param("bamount") BigDecimal bamount, @Param("namount") BigDecimal namount, @Param("wamount") BigDecimal wamount, @Param("waitamount") BigDecimal waitamount, @Param("userId") Integer userId);

    @Select("SELECT goldnum,no_withdrawal_amount as noWithdrawalAmount,withdrawal_amount as withdrawalAmount,bet_amount as betAmount,pay_amount as payAmount FROM app_member where id = #{memberId}")
    MemBaseinfo getMebBalanceById(@Param("memberId") Long memberId);

    @Select("SELECT goldnum FROM mem_baseinfo where id = #{memberId}")
    BigDecimal getBalanceById(@Param("memberId") Long memberId);

    /**
     * 查询用户信息
     */
    @Select("SELECT id,,nickname FROM `mem_baseinfo` where id = #{memberId} and mobileno = #{account}")
    MemBaseinfo getAppMemberInfo(@Param("memberId") Long memberId, @Param("account") String account);

    /**
     * 查询用户信息
     */
    @Select("select id as id,user_id as userId,username as username,password as password,balance as balance,type as type,create_time as createTime,login_time as loginTime,layer_no as layerNo from cpt_open_member where user_id = #{userId} and type = #{type}")
    CptOpenMember getCptOpenMember(@Param("userId") Integer memberId, @Param("type") String type);
}
