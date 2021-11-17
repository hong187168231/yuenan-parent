package com.indo.game.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

@Mapper //标记mapper文件位置，否则在Application.class启动类上配置mapper包扫描
@Repository
public interface OrderMapper {


    /**
     * 查询注单
     */


    @Update("<script>"
            + "update order_record set open_number=#{sgnumber} where issue=#{issue} and lottery_id =#{lotteryId} and status='NORMAL' and open_number=''  LIMIT  50000 "
            + "</script>")
    int updateOrderRecord(@Param("lotteryId") String lotteryId, @Param("issue") String issue, @Param("sgnumber") String sgnumber);

    /**
     * AG 棋牌   电竞 用户 登入到登出期间的有效投注额
     */

    @Select("SELECT group_concat(id) as ids,sum(valid_bet_amount) as betAmount FROM `ag_bet_order` where create_time >= #{beginTime}  and create_time <= #{endTime} and is_handle = 0 and user_id = #{userId}")
    Map<String, Object> agBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Select("<script>update ag_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void agUpdateBetData(@Param("orderIds") int[] orderIds);


    @Select("SELECT group_concat(id) as ids,sum(cell_score) as betAmount FROM `ky_bet_order` where create_time >= #{beginTime}  and create_time <= #{endTime} and is_handle = 0 and user_id = #{userId}")
    Map<String, Object> kyBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Select("<script>update ky_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void kyUpdateBetData(@Param("orderIds") int[] orderIds);


    @Select("SELECT group_concat(id) as ids,sum(total_amount) as betAmount FROM `es_bet_order` where create_time >= #{beginTime}  and create_time <= #{endTime} and is_handle = 0 and user_id = #{userId}")
    Map<String, Object> esBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Select("<script>update es_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void esUpdateBetData(@Param("orderIds") int[] orderIds);

    @Select("SELECT group_concat(id) as ids,sum(bet) as betAmount FROM `ae_bet_order` where is_handle = 0 and user_id = #{userId} and create_time >= #{beginTime} and create_time <= #{endTime}")
    Map<String, Object> aeBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Update("<script>update ae_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void aeUpdateBetData(@Param("orderIds") int[] orderIds);

    @Select("SELECT changetype from mem_goldchange where accno = #{accno} and changetype in(38,40,42,51,64,66) ORDER BY create_time desc LIMIT 1")
    Integer getLastInExternalGameType(@Param("accno") String accno);

    @Select("SELECT group_concat(id) as ids,sum(bet_amount) as betAmount FROM `mg_bet_order` where is_handle = 0 and user_id = #{userId} and created_at >= #{beginTime} and created_at <= #{endTime}")
    Map<String, Object> mgBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Update("<script>update mg_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void mgUpdateBetData(@Param("orderIds") int[] orderIds);

    @Select("SELECT group_concat(id) as ids,sum(bet) as betAmount FROM `db_bet_order` where is_handle = 0 and player_id = #{member_id} and last_modify_time >= #{beginTime} and last_modify_time <= #{endTime}")
    Map<String, Object> dbBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Update("<script>update db_bet_order set is_handle = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void dbUpdateBetData(@Param("orderIds") int[] orderIds);


    @Select("SELECT group_concat(id) as ids,sum(cost) as betAmount FROM `ag_fish_bet_order` where create_time >= #{beginTime}  and create_time <= #{endTime} and is_hald = 0 and member_id = #{userId}")
    Map<String, Object> agFishBetData(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Integer userId);

    @Select("<script>update ag_fish_bet_order set is_hald = 1 where id in <foreach item='orderId' index='index' collection='orderIds' open='(' separator=',' close=')'> #{orderId} </foreach> </script>")
    void agUpdateBetFishData(@Param("orderIds") int[] orderIds);
}
