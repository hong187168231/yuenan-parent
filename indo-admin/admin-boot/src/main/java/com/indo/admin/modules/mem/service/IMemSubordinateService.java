package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.base.service.SuperService;
import com.indo.user.pojo.dto.MemSubordinateDto;
import com.indo.user.pojo.entity.MemSubordinate;
import com.indo.user.pojo.vo.MemSubordinateVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户邀请表 服务类
 * </p>
 *
 * @author sss
 * @since 2021-08-26
 */
public interface IMemSubordinateService extends SuperService<MemSubordinate> {

    List<MemSubordinateVo> selectMemSubordinate(Page<MemSubordinateVo> page, MemSubordinateDto memSubordinateDTO);

    void excelExport(HttpServletResponse response, List<Long> ids) throws IOException;
}
