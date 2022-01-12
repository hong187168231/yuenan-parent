package com.indo.admin.modules.mem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.excel.MemBanRebateVoExcel;
import com.indo.admin.modules.mem.mapper.MemBanRebateMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.service.IMemBanRebateService;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.user.pojo.dto.MemBanRebateDto;
import com.indo.user.pojo.entity.MemBanRebate;
import com.indo.user.pojo.vo.MemBanRebateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 用户等级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Service
public class MemBanRebateServiceImpl extends SuperServiceImpl<MemBanRebateMapper, MemBanRebate> implements IMemBanRebateService {

    @Autowired
    private MemBanRebateMapper memBanRebateMapper;

    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;

    public List<MemBanRebateVo> selectMemBanRebate(Page<MemBanRebateVo> page, MemBanRebateDto dto) {
        return memBanRebateMapper.selectMemBanRebate(page, dto);
    }

    @Override
    public void excelExport(HttpServletResponse response, List<Long> ids) throws IOException {
        List<MemBanRebateVo> list;
        MemBanRebateDto dto = new MemBanRebateDto();
        if (!CollectionUtils.isEmpty(ids)) {
            dto.setIds(ids);
            list = memBanRebateMapper.selectMemBanRebate(new Page<>(), dto);
        } else {
            list = memBanRebateMapper.selectMemBanRebate(new Page<>(), dto);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("返点禁止", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemBanRebateVoExcel.class)
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

    @Override
    @Transactional
    public int saveAccounts(List<String> accounts, String remark) {
        List<Long> ids = memBaseInfoMapper.selectIdsByAccounts(accounts);
        MemBanRebate memBanRebate = new MemBanRebate();
        memBanRebate.setCreateTime(new Date());
        memBanRebate.setRemark(remark);
        for (Long id : ids) {
            memBanRebate.setMemId(id);
            memBanRebateMapper.insert(memBanRebate);
        }
        return 1;
    }

}