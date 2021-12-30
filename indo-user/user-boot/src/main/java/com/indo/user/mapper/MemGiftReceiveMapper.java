package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.user.pojo.entity.MemGiftReceive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动类型表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Mapper
public interface MemGiftReceiveMapper extends BaseMapper<MemGiftReceive> {


    @Select("SELECT COUNT(1) from mem_gift_receive m WHERE m.mem_id = #{memId} and m.gift_code = #{giftCode} AND m.up_level = #{}")
    int countVipUpLevelGift(@Param("memId") Long memId, @Param("giftCode") String giftCode, @Param("upLevel") Integer upLevel);


    @Select("SELECT COUNT(1) from mem_gift_receive m WHERE m.mem_id = #{memId} " +
            " and m.gift_code = #{giftCode} and m.create_time > = #{beginTime} and m.create_time <= #{endTime}")
    int countVipTimeIntervalGift(@Param("memId") Long memId, @Param("giftCode") String giftCode,
                                 @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(1) from mem_gift_receive m WHERE m.mem_id = #{memId} and m.gift_type = #{giftType} and m.gift_code = #{giftCode} ")
    int countRegisterGift(@Param("memId") Long memId, @Param("giftType") Integer giftType, @Param("giftCode") String giftCode);


}
