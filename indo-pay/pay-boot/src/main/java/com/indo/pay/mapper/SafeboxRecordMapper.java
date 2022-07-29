package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.indo.pay.pojo.resp.SafeboxMoneyResp;
import com.indo.pay.pojo.resp.SafeboxRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SafeboxRecordMapper extends BaseMapper<SafeboxRecord> {

    /**
     * 查询用户保险箱和用户余额
     * */
    @Select("select * FROM safebox_cash where user_id = #{id}")
    SafeboxMoneyResp checkSafeboxBalance(Long id);

    /**
     * 增加一条记录用户保险箱金额到用户余额
     */
    @Insert("INSERT INTO safebox_change_record ( user_id, safe_ordertype, change_amount, before_amount, after_amount, create_time, order_number )\n" +
            "VALUES\n" +
            "\t( #{userId}, #{safeOrdertype},#{changeAmount},#{beforeAmount},#{afterAmount},#{createTime},#{orderNumber})")
    void insertSafeboxRecord(SafeboxRecord record);


    /**
     * 修改用户保险箱金额
     */
    @Update("UPDATE safebox_cash SET user_safemoney=#{userSafemoney} WHERE user_id=#{userId}")
    void updateUserSafeboxMoney(SafeboxMoneyResp req);

    /**
     * 增加一条用户保险箱金额
     */
    @Insert("INSERT INTO safebox_cash VALUES(#{userId},#{userSafemoney})")
    void insertUserSafeboxMoney(SafeboxMoneyResp req);

    /**
     * 查询用户保险箱记录
     * */
    @Select("SELECT * from safebox_change_record where user_id = #{userid}")
    Page<SafeboxRecord> selectSafeboxRecordById(Long userid);
}
