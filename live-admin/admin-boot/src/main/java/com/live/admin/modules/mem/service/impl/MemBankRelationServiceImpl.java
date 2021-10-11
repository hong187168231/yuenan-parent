package com.live.admin.modules.mem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.live.admin.modules.mem.mapper.MemBankRelationMapper;
import com.live.common.mybatis.base.PageResult;
import com.live.user.pojo.dto.MembankRelationDTO;
import com.live.admin.modules.mem.service.IMemBankRelationService;
import com.live.user.pojo.entity.MemBankRelation;
import com.live.user.pojo.vo.MemBankRelationExportVO;
import com.live.user.pojo.vo.MemBankRelationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemBankRelationServiceImpl extends ServiceImpl<MemBankRelationMapper, MemBankRelation> implements IMemBankRelationService {

    @Autowired
    private MemBankRelationMapper memBankRelationMapper;

    @Override
    public PageResult<MemBankRelationVO> queryList(MembankRelationDTO membankRelationDTO) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != membankRelationDTO.getPage() && null != membankRelationDTO.getLimit()){
            pageNum = membankRelationDTO.getPage();
            pageSize = membankRelationDTO.getLimit();
        }
        Page<MemBankRelationVO> page =  new Page<>(pageNum, pageSize);
        List<MemBankRelationVO> list = memBankRelationMapper.queryList(page, membankRelationDTO);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public void exportList(HttpServletResponse response,List<Long> ids) throws IOException {
        List<MemBankRelationExportVO> dataList = new ArrayList<>();

        List<MemBankRelationVO> list = memBankRelationMapper.getListByIds(ids);

        list.forEach(data -> {
            MemBankRelationExportVO vo = new MemBankRelationExportVO();
            BeanUtils.copyProperties(data,vo);
            dataList.add(vo);
        });

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("用户银行卡信息", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemBankRelationExportVO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("Sheet1")
                    .doWrite(dataList);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            e.printStackTrace();
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
}
