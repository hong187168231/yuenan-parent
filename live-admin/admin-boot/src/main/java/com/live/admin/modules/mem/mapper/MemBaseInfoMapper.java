package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.pojo.vo.AgentVo;
import com.live.user.pojo.dto.AgentReportDto;
import com.live.user.pojo.dto.MemBaseInfoDto;
import com.live.user.pojo.dto.MemReportDto;
import com.live.user.pojo.entity.MemBaseinfo;
import com.live.user.pojo.vo.AgentReportVo;
import com.live.user.pojo.vo.MemBaseInfoVo;
import com.live.user.pojo.vo.MemReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author: Mr.liu
 * @Date: 2021/8/30 16:10
 * @Version: 1.0.0
 * @Desc:
 */
@Mapper
public interface MemBaseInfoMapper extends BaseMapper<MemBaseinfo> {
    /**
     * 分页查询用户信息
     *
     * @param page
     * @param dto
     * @return
     */
    List<MemBaseInfoVo> queryList(@Param("page") Page<MemBaseInfoVo> page, @Param("dto") MemBaseInfoDto dto);

    /**
     * 添加用户
     *
     * @param memBaseinfo
     * @return
     */
    void insertSelective(MemBaseinfo memBaseinfo);

    /**
     * 修改用户
     *
     * @param memBaseinfo
     * @return
     */
    int updateByPrimaryKeySelective(MemBaseinfo memBaseinfo);

    /**
     * 根据账号查询ids
     *
     * @param list
     * @return
     */
    List<Long> selectIdsByAccounts(List<String> list);

    /**
     * 根据账号查询NickName
     *
     * @param list
     * @return
     */
    List<String> selectNickNameByAccounts(List<String> list);

    /**
     * 会员列表分页
     *
     * @param page
     * @param dto
     * @return
     */
    // todo 缺少返回类数据
    List<MemReportVo> memReportList(@Param("page") Page<MemReportVo> page, @Param("dto") MemReportDto dto);

    /**
     * 代理导出数据列表分页
     *
     * @param page
     * @param dto
     * @return
     */
    // todo 缺少返回类数据
    List<AgentReportVo> agentReportList(@Param("page") Page<AgentReportVo> page, @Param("dto") AgentReportDto dto);

    /**
     * 代理列表分页
     *
     * @param page
     * @param dto
     * @return
     */
    List<AgentVo> agentPage(@Param("page") Page<AgentVo> page, @Param("dto") MemBaseInfoDto dto);

    /**
     * 下级列表
     * @param page
     * @param dto
     * @return
     */
    List<MemBaseInfoVo> subordinateMemPage(@Param("page") Page<MemBaseInfoVo> page, @Param("dto") MemBaseInfoDto dto);
}
