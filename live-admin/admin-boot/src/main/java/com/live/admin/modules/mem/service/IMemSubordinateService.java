package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.common.mybatis.base.service.SuperService;
import com.live.user.pojo.dto.MemSubordinateDto;
import com.live.user.pojo.entity.MemSubordinate;
import com.live.user.pojo.vo.MemSubordinateVo;
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
