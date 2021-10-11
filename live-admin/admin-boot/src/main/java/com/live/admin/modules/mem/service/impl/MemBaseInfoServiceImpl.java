package com.live.admin.modules.mem.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.mem.excel.MemBaseInfoVoExcel;
import com.live.admin.modules.mem.excel.MemPlayWithInfoVoExcel;
import com.live.admin.modules.report.excel.AgentReportVoExcel;
import com.live.admin.modules.report.excel.MemReportVoExcel;
import com.live.admin.modules.mem.mapper.MemBaseInfoMapper;
import com.live.admin.modules.mem.mapper.MemSubordinateMapper;
import com.live.admin.modules.mem.service.IMemBaseInfoService;
import com.live.admin.pojo.vo.AgentVo;
import com.live.common.constant.RedisConstants;
import com.live.common.mybatis.base.PageResult;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.result.Result;
import com.live.common.utils.NameGeneratorUtil;
import com.live.user.pojo.dto.AgentReportDto;
import com.live.user.pojo.dto.MemBaseInfoDto;
import com.live.user.pojo.dto.MemReportDto;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.entity.MemSubordinate;
import com.live.user.pojo.vo.AgentReportVo;
import com.live.user.pojo.vo.MemBaseInfoVo;
import com.live.user.pojo.vo.MemReportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:11
 * @Version: 1.0.0
 * @Desc:
 */
@Service
public class MemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo> implements IMemBaseInfoService {
    @Autowired
    private MemBaseInfoMapper memBaseInfoMapper;
    @Autowired
    private MemSubordinateMapper memSubordinateMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult<MemBaseInfoVo> queryList(MemBaseInfoDto memBaseInfoDto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != memBaseInfoDto.getPage() && null != memBaseInfoDto.getLimit()) {
            pageNum = memBaseInfoDto.getPage();
            pageSize = memBaseInfoDto.getLimit();
        }
        Page<MemBaseInfoVo> page = new Page<>(pageNum, pageSize);
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, memBaseInfoDto);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public void excelExport(HttpServletResponse response, List<Long> ids) throws IOException {
        List<MemBaseInfoVo> baseInfoList;
        MemBaseInfoDto memBaseInfoDto = new MemBaseInfoDto();
        if (!CollectionUtils.isEmpty(ids)) {
            memBaseInfoDto.setIds(ids);
            baseInfoList = memBaseInfoMapper.queryList(null, memBaseInfoDto);
        } else {
            baseInfoList = memBaseInfoMapper.queryList(null, memBaseInfoDto);
        }
        baseInfoList = baseInfoList.stream().sorted(Comparator.comparing(MemBaseInfoVo::getId)).collect(Collectors.toList());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户信息", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemBaseInfoVoExcel.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("用户列表")
                    .doWrite(baseInfoList);
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
    @Transactional(rollbackFor = Exception.class)
    public Result addGeneralUser(MemBaseInfoDto memBaseInfoDto) {
        MemBaseinfo one = memBaseInfoMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, memBaseInfoDto.getAccount()));
        if (Objects.nonNull(one)) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo baseinfo = new MemBaseinfo();
        BeanUtils.copyProperties(memBaseInfoDto, baseinfo);
        baseinfo.setPassword(SecureUtil.md5(baseinfo.getPassword()));
        baseinfo.setStatus(true);
        baseinfo.setInviteCode(productInviteCode());
        baseinfo.setNickName(NameGeneratorUtil.generate());
        memBaseInfoMapper.insertSelective(baseinfo);
        return handSubordinateRelation(memBaseInfoDto.getSuperiorAgent(), baseinfo.getId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAgentUser(MemBaseInfoDto memBaseInfoDto) {
        MemBaseinfo one = memBaseInfoMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, memBaseInfoDto.getAccount()));
        if (Objects.nonNull(one)) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo baseinfo = new MemBaseinfo();
        BeanUtils.copyProperties(memBaseInfoDto, baseinfo);
        baseinfo.setStatus(true);
        baseinfo.setPassword(SecureUtil.md5(baseinfo.getPassword()));
        baseinfo.setIdentityType(1);
        baseinfo.setInviteCode(productInviteCode());
        baseinfo.setNickName(NameGeneratorUtil.generate());
        memBaseInfoMapper.insertSelective(baseinfo);
        //层级关系
        MemSubordinate subordinate = new MemSubordinate();
        subordinate.setMemId(baseinfo.getId());
        subordinate.setParentId(0L);
        subordinate.setCreateTime(new Date());
        subordinate.setLevelNum(0);
        subordinate.setTeamNum(0);
        subordinate.setHierarchy(1);
        memSubordinateMapper.insert(subordinate);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changeStatus(Long id,Integer status,Integer prohibitInvite,Integer prohibitInvestment,Integer prohibitDisbursement,Integer prohibitRecharge) {
        if (Objects.isNull(id)) {
            return Result.failed("id不能为空！");
        }
        MemBaseinfo baseinfo = new MemBaseinfo();
        baseinfo.setId(id);
        if (Objects.nonNull(status)) {
            baseinfo.setStatus(status == 0 ? false : true);
        }
        if (Objects.nonNull(prohibitInvite)) {
            baseinfo.setProhibitInvite(prohibitInvite == 0 ? false : true);
        }
        if (Objects.nonNull(prohibitInvestment)) {
            baseinfo.setProhibitInvestment(prohibitInvestment == 0 ? false : true);
        }
        if (Objects.nonNull(prohibitDisbursement)) {
            baseinfo.setProhibitDisbursement(prohibitDisbursement == 0 ? false : true);
        }
        if (Objects.nonNull(prohibitRecharge)) {
            baseinfo.setProhibitRecharge(prohibitRecharge == 0 ? false : true);
        }
        int result = memBaseInfoMapper.updateById(baseinfo);
        if (result > 0) {
            return Result.success();
        }
        return Result.failed();
    }

    /**
     * 生成邀请码
     *
     * @return
     */
    private String productInviteCode() {
        String inviteCode = RandomUtil.randomString(3);
        redisTemplate.opsForValue().increment(RedisConstants.MEM_GENERATE_INVITATION_CODE, 1);
        String redisStr = String.format("%03d", redisTemplate.opsForValue().get(RedisConstants.MEM_GENERATE_INVITATION_CODE));
        return inviteCode + redisStr;
    }

    /**
     * 处理层级关系
     *
     * @param superiorAgent 上级代理账号
     * @param id            用户ID
     */
    private Result handSubordinateRelation(String superiorAgent, Long id) {
        MemBaseinfo baseinfo = memBaseInfoMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, superiorAgent));
        if (Objects.isNull(baseinfo)) {
            return Result.failed("上级代理账号不存在！");
        }
        MemSubordinate subordinateDb = memSubordinateMapper.selectOne(new QueryWrapper<MemSubordinate>().lambda().eq(MemSubordinate::getMemId, baseinfo.getId()));
        if (Objects.isNull(subordinateDb)) {
            return Result.failed("代理账号无效！");
        }
        subordinateDb.setLevelNum(subordinateDb.getLevelNum() + 1);
        subordinateDb.setTeamNum(subordinateDb.getTeamNum() + 1);
        subordinateDb.setLevelUserIds(StringUtils.isBlank(subordinateDb.getLevelUserIds()) ? id.toString() : subordinateDb.getLevelUserIds() + "," + id.toString());
        subordinateDb.setUpdateTime(new Date());
        memSubordinateMapper.updateById(subordinateDb);

        MemSubordinate subordinate = new MemSubordinate();
        subordinate.setMemId(id);
        subordinate.setParentId(baseinfo.getId());
        subordinate.setCreateTime(new Date());
        subordinate.setLevelNum(0);
        subordinate.setTeamNum(0);
        subordinate.setHierarchy(0);
        memSubordinateMapper.insert(subordinate);
        return Result.success();
    }

    @Override
    public void exportPlayWithList(HttpServletResponse response, List<Long> ids) throws IOException {
        List<MemBaseInfoVo> baseInfoList;
        MemBaseInfoDto memBaseInfoDto = new MemBaseInfoDto();
        memBaseInfoDto.setIdentityType(2);
        if (!CollectionUtils.isEmpty(ids)) {
            memBaseInfoDto.setIds(ids);
            baseInfoList = memBaseInfoMapper.queryList(null, memBaseInfoDto);
        } else {
            baseInfoList = memBaseInfoMapper.queryList(null, memBaseInfoDto);
        }
        baseInfoList = baseInfoList.stream().sorted(Comparator.comparing(MemBaseInfoVo::getId)).collect(Collectors.toList());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("带玩账号信息", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemPlayWithInfoVoExcel.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("带玩账号列表")
                    .doWrite(baseInfoList);
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
    public PageResult<AgentVo> agentPage(MemBaseInfoDto memBaseInfoDto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != memBaseInfoDto.getPage() && null != memBaseInfoDto.getLimit()) {
            pageNum = memBaseInfoDto.getPage();
            pageSize = memBaseInfoDto.getLimit();
        }
        Page<AgentVo> page = new Page<>(pageNum, pageSize);
        List<AgentVo> list = memBaseInfoMapper.agentPage(page, memBaseInfoDto);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public PageResult<MemBaseInfoVo> subordinateMemPage(MemBaseInfoDto memBaseInfoDto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != memBaseInfoDto.getPage() && null != memBaseInfoDto.getLimit()) {
            pageNum = memBaseInfoDto.getPage();
            pageSize = memBaseInfoDto.getLimit();
        }
        Page<MemBaseInfoVo> page = new Page<>(pageNum, pageSize);
        List<MemBaseInfoVo> list = memBaseInfoMapper.subordinateMemPage(page, memBaseInfoDto);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public List<MemReportVo> memReportList(Page<MemReportVo> page, MemReportDto dto) {
        return memBaseInfoMapper.memReportList(page,dto);
    }

    @Override
    public void memReportExport(HttpServletResponse response, List<Long> ids) throws IOException {
        List<MemReportVo> list;
        MemReportDto dto = new MemReportDto();
        if (!CollectionUtils.isEmpty(ids)) {
            dto.setIds(ids);
            list = memBaseInfoMapper.memReportList(null, dto);
        } else {
            list = memBaseInfoMapper.memReportList(null, dto);
        }
        list = list.stream().sorted(Comparator.comparing(MemReportVo::getId)).collect(Collectors.toList());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("会员报表", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MemReportVoExcel.class)
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
    public int clearPwdErrorNum(Long id) {
        MemBaseinfo baseinfo = new MemBaseinfo();
        baseinfo.setId(id);
        baseinfo.setPassErrorNum(0);
        return memBaseInfoMapper.updateById(baseinfo);
    }

    @Override
    public List<AgentReportVo> agentReportList(Page<AgentReportVo> page, AgentReportDto dto) {
        return memBaseInfoMapper.agentReportList(page,dto);
    }

    @Override
    public void agentReportExport(HttpServletResponse response, List<Long> ids) throws IOException {
        List<AgentReportVo> list;
        AgentReportDto dto = new AgentReportDto();
        if (!CollectionUtils.isEmpty(ids)) {
            dto.setIds(ids);
            list = memBaseInfoMapper.agentReportList(null, dto);
        } else {
            list = memBaseInfoMapper.agentReportList(null, dto);
        }
        list = list.stream().sorted(Comparator.comparing(AgentReportVo::getId)).collect(Collectors.toList());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("代理报表", "UTF-8").replaceAll("\\+", "%20");
            fileName = fileName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), AgentReportVoExcel.class)
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
    public List<MemBaseinfo> checkList(MemBaseInfoDto memBaseInfoDto) {

        List<MemBaseinfo> list = new ArrayList<>();
        String accounts = memBaseInfoDto.getAccounts();
        if(StringUtils.isNotBlank(accounts)){

            Arrays.stream(accounts.split("/")).forEach(account -> {

                // 单个用户查询
                MemBaseinfo baseInfo = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, account));
                if (Objects.nonNull(baseInfo)) {
                    list.add(baseInfo);
                }

            });
        }
        return list;
    }
}
