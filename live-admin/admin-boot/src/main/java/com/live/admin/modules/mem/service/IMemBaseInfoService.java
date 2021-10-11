package com.live.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.admin.pojo.vo.AgentVo;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.user.pojo.dto.AgentReportDto;
import com.live.user.pojo.dto.MemBaseInfoDto;
import com.live.user.pojo.dto.MemReportDto;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.vo.AgentReportVo;
import com.live.user.pojo.vo.MemBaseInfoVo;
import com.live.user.pojo.vo.MemReportVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:10
 * @Version: 1.0.0
 * @Desc:
 */
public interface IMemBaseInfoService extends IService<MemBaseinfo> {
    /**
     * 分页查询用户
     * @param memBaseInfoDto
     * @return
     */
    PageResult<MemBaseInfoVo> queryList(MemBaseInfoDto memBaseInfoDto);

    /**
     * 导出 excel
     * @param ids
     */
    void excelExport(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 新增会员
     * @param memBaseInfoDto
     * @return
     */
    Result addGeneralUser(MemBaseInfoDto memBaseInfoDto);

    /**
     * 新增代理
     * @param memBaseInfoDto
     * @return
     */
    Result addAgentUser(MemBaseInfoDto memBaseInfoDto);

    /**
     * 修改用户状态
     * @param id
     * @param status
     * @param prohibitInvite
     * @param prohibitInvestment
     * @param prohibitDisbursement
     * @param prohibitRecharge
     * @return
     */
    Result changeStatus(Long id,Integer status,Integer prohibitInvite,Integer prohibitInvestment,Integer prohibitDisbursement,Integer prohibitRecharge);

    /**
     * 导出 excel
     * @param ids
     */
    void exportPlayWithList(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 代理列表
     * @param memBaseInfoDto
     * @return
     */
    PageResult<AgentVo> agentPage(MemBaseInfoDto memBaseInfoDto);

    /**
     * 下级用户
     * @param memBaseInfoDto
     * @return
     */
    PageResult<MemBaseInfoVo> subordinateMemPage(MemBaseInfoDto memBaseInfoDto);

    /**
     * 会员报表
     */
    List<MemReportVo> memReportList(Page<MemReportVo> page, MemReportDto dto);

    /**
     * 会员报表导出 excel
     * @param ids
     */
    void memReportExport(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 清除密码错误次数
     * @param id
     * @return
     */
    int clearPwdErrorNum(Long id);

    /**
     * 代理报表
     */
    List<AgentReportVo> agentReportList(Page<AgentReportVo> page, AgentReportDto dto);

    /**
     * 代理报表导出 excel
     * @param ids
     */
    void agentReportExport(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 校验用户名称是否存在
     * @param memBaseInfoDto
     * @return 存在的用户名称和用户ID集合
     */
    List<MemBaseinfo> checkList(MemBaseInfoDto memBaseInfoDto);
}
