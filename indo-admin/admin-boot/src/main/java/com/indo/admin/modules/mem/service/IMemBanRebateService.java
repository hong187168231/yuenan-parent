package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.mybatis.base.service.SuperService;
import com.indo.user.pojo.dto.MemBanRebateDto;
import com.indo.user.pojo.entity.MemBanRebate;
import com.indo.user.pojo.vo.MemBanRebateVo;
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
