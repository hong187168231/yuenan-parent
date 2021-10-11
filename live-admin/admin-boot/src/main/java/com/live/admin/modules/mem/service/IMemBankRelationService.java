package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.mybatis.base.PageResult;
import com.live.user.pojo.dto.MembankRelationDTO;
import com.live.user.pojo.entity.MemBankRelation;
import com.live.user.pojo.vo.MemBankRelationExportVO;
import com.live.user.pojo.vo.MemBankRelationVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IMemBankRelationService extends IService<MemBankRelation> {

    /**
     * 分页查询
     * @param membankRelationDTO
     * @return
     */
    PageResult<MemBankRelationVO> queryList(MembankRelationDTO membankRelationDTO);

    /**
     * 数据导出
     * @param ids
     * @return
     */
    void exportList(HttpServletResponse response,List<Long> ids) throws IOException;
}
