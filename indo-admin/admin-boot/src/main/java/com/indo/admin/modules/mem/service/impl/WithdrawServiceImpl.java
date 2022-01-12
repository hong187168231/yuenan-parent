package com.indo.admin.modules.mem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.excel.WithdrawalVoExcel;
import com.indo.admin.modules.mem.mapper.WithdrawMapper;
import com.indo.admin.modules.mem.service.IWithdrawService;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.user.pojo.dto.WithdrawDto;
import com.indo.user.pojo.entity.Withdraw;
import com.indo.user.pojo.vo.WithdrawVo;
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

@Service
public class WithdrawServiceImpl extends SuperServiceImpl<WithdrawMapper, Withdraw> implements IWithdrawService {

    @Autowired
    private WithdrawMapper withdrawalMapper;

    @Override
    public List<WithdrawVo> queryList(Page<WithdrawVo> p, WithdrawDto dto) {
        return withdrawalMapper.queryList(p, dto);
    }

    @Override
    public void export(HttpServletResponse response, List<Long> ids) throws IOException {
        List<WithdrawVo> list;
        WithdrawDto dto = new WithdrawDto();
        if (!CollectionUtils.isEmpty(ids)) {
            dto.setIds(ids);
            list = withdrawalMapper.queryList(new Page<>(), dto);
        } else {
            list = withdrawalMapper.queryList(new Page<>(), dto);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("提现记录", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), WithdrawalVoExcel.class)
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
    public List<WithdrawVo> queryApplyList(Page<WithdrawVo> p, WithdrawDto dto) {
        return withdrawalMapper.queryApplyList(p, dto);
    }

}
