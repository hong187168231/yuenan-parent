package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.common.mybatis.base.service.SuperService;
import com.live.common.result.Result;
import com.live.user.pojo.dto.WithdrawDto;
import com.live.user.pojo.entity.Withdraw;
import com.live.user.pojo.vo.WithdrawVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IWithdrawService extends SuperService<Withdraw> {

    /**
     * 分页查询
     */
    List<WithdrawVo> queryList(Page<WithdrawVo> p, WithdrawDto dto);

    /**
     * 数据导出
     */
    void export(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 提现申请分页查询
     * @param p
     * @param dto
     * @return
     */
    List<WithdrawVo> queryApplyList(Page<WithdrawVo> p, WithdrawDto dto);

}
