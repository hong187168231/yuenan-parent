package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.dto.MemBaseInfoDto;
import com.live.user.pojo.entity.ManualDepositWithDraw;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;
import com.live.user.pojo.vo.MemBaseInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:10
 * @Version: 1.0.0
 * @Desc:
 */
@Mapper
public interface ManualDepositWithDrawMapper extends BaseMapper<ManualDepositWithDraw> {
    /**
     * 人工提取列表查询
     * @param page
     * @param dto
     * @return
     */
    List<ManualDepositWithDrawVO> queryList(@Param("page") Page<ManualDepositWithDrawVO> page, @Param("dto") ManualDepositWithDrawDto dto);

    /**
     * 人工提取写入
     * @param manualDepositWithDraw
     */
    void insertManualDepositWithDraw(ManualDepositWithDraw manualDepositWithDraw);
}
