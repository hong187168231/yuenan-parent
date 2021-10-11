package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MembankRelationDTO;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.vo.MemBankRelationExportVO;
import com.indo.user.pojo.vo.MemBankRelationVO;

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
