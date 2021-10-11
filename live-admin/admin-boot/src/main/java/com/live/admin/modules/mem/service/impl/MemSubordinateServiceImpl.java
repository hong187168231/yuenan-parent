package com.live.admin.modules.mem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.mem.excel.MemSubordinateVoExcel;
import com.live.admin.modules.mem.mapper.MemSubordinateMapper;
import com.live.admin.modules.mem.service.IMemSubordinateService;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.user.pojo.dto.MemSubordinateDto;
import com.live.user.pojo.entity.MemSubordinate;
import com.live.user.pojo.vo.MemSubordinateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 用户邀请表 服务实现类
 * </p>
 *
 * @author sss
 * @since 2021-08-26
 */
@Service
public class MemSubordinateServiceImpl extends SuperServiceImpl<MemSubordinateMapper, MemSubordinate> implements IMemSubordinateService {

    @Autowired
    private MemSubordinateMapper memBanRebateMapper;

    public List<MemSubordinateVo> selectMemSubordinate(Page<MemSubordinateVo> page, MemSubordinateDto memSubordinateDTO) {
        return memBanRebateMapper.selectMemSubordinate(page, memSubordinateDTO);
    }

    @Override
    public void excelExport(HttpServletResponse response, List<Long> ids) throws IOException {
        List<MemSubordinateVo> list;
        MemSubordinateDto dto = new MemSubordinateDto();
        if (!CollectionUtils.isEmpty(ids)) {
            dto.setIds(ids);
            list = memBanRebateMapper.selectMemSubordinate(new Page<>(), dto);
        } else {
            list = memBanRebateMapper.selectMemSubordinate(new Page<>(), dto);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("邀请码信息", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemSubordinateVoExcel.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("列表")
                    .doWrite(list);
        } catch (Exception e) {
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