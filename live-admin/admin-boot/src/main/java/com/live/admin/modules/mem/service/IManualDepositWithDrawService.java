package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.dto.MemBaseInfoDto;
import com.live.user.pojo.entity.ManualDepositWithDraw;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;
import com.live.user.pojo.vo.MemBaseInfoVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:10
 * @Version: 1.0.0
 * @Desc:
 */
public interface IManualDepositWithDrawService extends IService<ManualDepositWithDraw> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<ManualDepositWithDrawVO> queryList(Page<ManualDepositWithDrawVO> page, ManualDepositWithDrawDto dto);

    /**
     * 人工提取写入
     * @param dto
     */
    Result insert(ManualDepositWithDrawDto dto);
}
