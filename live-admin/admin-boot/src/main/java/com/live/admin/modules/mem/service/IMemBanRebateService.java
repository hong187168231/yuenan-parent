package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.common.mybatis.base.service.SuperService;
import com.live.user.pojo.dto.MemBanRebateDto;
import com.live.user.pojo.entity.MemBanRebate;
import com.live.user.pojo.vo.MemBanRebateVo;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户等级表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
public interface IMemBanRebateService extends SuperService<MemBanRebate> {

    int saveAccounts(List<String> accounts,String remark);

    List<MemBanRebateVo> selectMemBanRebate(Page<MemBanRebateVo> p, MemBanRebateDto dto);

    void excelExport(HttpServletResponse response, List<Long> ids) throws IOException;
}
