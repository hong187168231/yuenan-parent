package com.indo.game.mapper.mem;

import com.indo.common.mybatis.base.mapper.SuperMapper;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    /**
     * 修改用户余额,等等金额
     *
     * @param amount 余额变动值，等等金额（正为增加，负为减少）
     * @param accno  用户账号
     */
    @Update("UPDATE mem_baseinfo m\n" +
            "        SET m.goldnum = (\n" +
            "          CASE\n" +
            "          WHEN  m.goldnum + #{amount} <![CDATA[ < ]]> 0 THEN\n" +
            "          0\n" +
            "          WHEN m.goldnum + #{amount} <![CDATA[ >= ]]> 0 THEN\n" +
            "          m.goldnum + #{amount}\n" +
            "          END\n" +
            "        ),\n" +
            "         m.wait_amount = (\n" +
            "            CASE\n" +
            "            WHEN m.wait_amount + #{waitamount} <![CDATA[ < ]]> 0 THEN\n" +
            "                0\n" +
            "            WHEN m.wait_amount + #{waitamount} <![CDATA[ >= ]]> 0 THEN\n" +
            "                m.wait_amount + #{waitamount}\n" +
            "            END\n" +
            "        ),\n" +
            "         m.pay_amount = m.pay_amount + #{pamount},\n" +
            "         m.bet_amount = m.bet_amount + #{bamount},\n" +
            "         m.no_withdrawal_amount = (\n" +
            "            CASE\n" +
            "            WHEN m.no_withdrawal_amount + #{namount}  <![CDATA[ > ]]> 0 THEN\n" +
            "                m.no_withdrawal_amount + #{namount}\n" +
            "            WHEN m.no_withdrawal_amount + #{namount} <![CDATA[ <= ]]> 0 THEN\n" +
            "                0\n" +
            "            END\n" +
            "        ),\n" +
            "        m.consume_amount = (\n" +
            "            CASE\n" +
            "            WHEN m.consume_amount + #{consumeAcmount}  <![CDATA[ > ]]> 0 THEN\n" +
            "                m.consume_amount + #{consumeAcmount}\n" +
            "            WHEN m.consume_amount + #{consumeAcmount} <![CDATA[ <= ]]> 0 THEN\n" +
            "                0\n" +
            "            END\n" +
            "        ),\n" +
            "         m.withdrawal_amount = m.withdrawal_amount + #{wamount}\n" +
            "        WHERE\n" +
            "            m.accno = #{accno}")
    int updatePersonalFinancialInfo(@Param("amount") BigDecimal amount, @Param("pamount") BigDecimal pamount, @Param("bamount") BigDecimal bamount, @Param("namount") BigDecimal namount, @Param("consumeAcmount") BigDecimal consumeAcmount, @Param("wamount") BigDecimal wamount, @Param("waitamount") BigDecimal waitamount, @Param("accno") String accno);
}
