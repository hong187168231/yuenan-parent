package com.indo.admin.modules.mem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.pojo.dto.MemBetInfoDTO;
import com.indo.admin.pojo.vo.mem.MemBetInfoVo;
import com.indo.common.enums.AccountTypeEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.ShareCodeUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.constant.AppConstants;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.encrypt.MD5;
import com.indo.admin.pojo.req.mem.MemAddReq;
import com.indo.admin.pojo.req.mem.MemBaseInfoReq;
import com.indo.admin.pojo.req.mem.MemEditStatusReq;
import com.indo.admin.pojo.req.mem.MemEditReq;
import com.indo.admin.pojo.vo.mem.MemBaseInfoVo;
import com.indo.admin.pojo.vo.mem.MemBaseDetailVO;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.entity.*;
import com.indo.core.pojo.vo.LoanRecordVo;
import com.indo.core.service.ILoanRecordService;
import com.indo.core.service.impl.LoanRecordServiceImpl;
import com.indo.core.util.BusinessRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员基础信息表 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
@Service
@Slf4j
public class MemBaseinfoServiceImpl extends SuperServiceImpl<MemBaseinfoMapper, MemBaseinfo> implements IMemBaseinfoService {

    @Resource
    private MemBaseinfoMapper memBaseInfoMapper;
    @Resource
    private AgentRelationMapper agentRelationMapper;
    @Resource
    private MemLevelMapper memLevelMapper;
    @Resource
    private MemInviteCodeMapper memInviteCodeMapper;
    @Resource
    private ILoanRecordService loanRecordService;
    @Override
    public Page<MemBaseInfoVo> queryList(MemBaseInfoReq req) {
        Page<MemBaseInfoVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, req);
        Date now = new Date();
        list.forEach(item -> {
            String lastActive = (String) AdminBusinessRedisUtils.hget(AppConstants.USER_ACTIVE_KEY, item.getAccount());
            if (StringUtils.isNotBlank(lastActive)) {
                item.setLeaveDays(DateUtils.daysBetween(lastActive, DateUtils.getDateString(now)));
            } else {
                item.setLeaveDays(DateUtils.daysBetween(item.getLastLoginTime(), now));
            }
            if(StringUtils.isNotEmpty(item.getMemLevel())){
               MemLevel memLevel = memLevelMapper.selectById(Long.valueOf(item.getMemLevel()));
               if(memLevel!=null){
                   item.setLevel(memLevel.getLevel());
               }
            }
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setMemLevel(Integer.valueOf(item.getMemLevel()));
            loginInfo.setId(item.getId());
            LoanRecordVo loanRecordVo = loanRecordService.findMemLoanInfo(loginInfo);
            if(loanRecordVo!=null){
                item.setLoanRecordVo(loanRecordVo);
            }
        });


        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public void addMemBaseInfo(MemAddReq req) {
        MemBaseInfoBO curentMem = this.memBaseInfoMapper.findMemBaseInfoByAccount(req.getAccount());
        if (curentMem != null) {
            throw new BizException(ResultCode.DATA_DUPLICATION);
        }
        MemBaseInfoBO supperMem = null;
        if (StringUtils.isNotBlank(req.getSuperAccno())) {
            supperMem = this.memBaseInfoMapper.findMemBaseInfoByAccount(req.getSuperAccno());
            if (supperMem == null || !supperMem.getAccType().equals(2)) {
                throw new BizException(ResultCode.ACCOUNT_ERROR);
            }
        }
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        AgentRelation memAgent = new AgentRelation();
        BeanUtils.copyProperties(req, memBaseinfo);
        memBaseinfo.setPasswordMd5(MD5.md5(req.getPassword()));
        memBaseinfo.setAccType(AccountTypeEnum.AGENT.getValue());
        if (baseMapper.insert(memBaseinfo) > 0) {
            saveMemInviteCode(memBaseinfo);
            memAgent.setMemId(memBaseinfo.getId());
            memAgent.setAccount(memBaseinfo.getAccount());
            memAgent.setTeamNum(1);
            memAgent.setSubUserIds(memBaseinfo.getId() + "");
            if(supperMem!=null){
                memAgent.setParentId(supperMem.getId());
                memAgent.setSuperior(supperMem.getAccount());
            }
            memAgent.setStatus(0);
            if (memBaseinfo.getAccType().equals(2)) {
                memAgent.setStatus(1);
            }
            int row = agentRelationMapper.insert(memAgent);
            if (row > 0) {
                //给上级增加下级相关参数
                if(supperMem!=null){
                    initMemParentAgent(memBaseinfo, supperMem.getId());
                }
            }
        }
    }
    /**
     * 插入会员邀请码
     *
     * @param memBaseinfo
     */
    @Override
    public void saveMemInviteCode(MemBaseinfo memBaseinfo) {
        MemInviteCode memInviteCode = new MemInviteCode();
        String code = ShareCodeUtil.inviteCode(memBaseinfo.getId());
        memInviteCode.setMemId(memBaseinfo.getId());
        memInviteCode.setAccount(memBaseinfo.getAccount());
        memInviteCode.setInviteCode(code.toLowerCase());
        memInviteCode.setStatus(1);
        boolean inviteFlag = memInviteCodeMapper.insert(memInviteCode) > 0;
        if (!inviteFlag) {
            throw new BizException(ResultCode.INVITATION_CODE_ERROR);
        }
    }

    @Override
    public Page<MemBetInfoVo> findMemBetInfoPage(MemBetInfoDTO memBetInfoDTO) {
        Page<MemBetInfoVo> page = new Page<>(memBetInfoDTO.getPage(), memBetInfoDTO.getLimit());
        page =memBaseInfoMapper.findMemBetInfo(page,memBetInfoDTO);
        page.getRecords().forEach(l->{
            l.setWithdrawalBetAmount(l.getTotalRechargeAmount().add(l.getTotalGiftAmount()).add(l.getOtherAmount()));
            if((l.getWithdrawalBetAmount().subtract(l.getTotalBetAmount())).compareTo(BigDecimal.ZERO)>=0){
                l.setLack(l.getTotalBetAmount().subtract(l.getWithdrawalBetAmount()));
            }else{
                l.setLack(BigDecimal.ZERO);
            }
        });
        return page;
    }

    public void initMemParentAgent(MemBaseinfo memBaseinfo, Long parentId) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AgentRelation::getMemId, parentId)
                .eq(AgentRelation::getStatus, 1);
        AgentRelation parentAgent = agentRelationMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(wrapper)) {
            throw new BizException(ResultCode.INVITEE_NO_AGENT_ERROR);
        }
        String subUserIds = StringUtils.isBlank(parentAgent.getSubUserIds()) ?
                memBaseinfo.getId() + "" : parentAgent.getSubUserIds() + "," + memBaseinfo.getId();
        parentAgent.setSubUserIds(subUserIds);
        parentAgent.setTeamNum(parentAgent.getTeamNum() + 1);
        agentRelationMapper.updateById(parentAgent);
    }

    @Override
    public boolean editMemBaseInfo(MemEditReq req) {
        MemBaseinfo memBaseinfo = checkMemIsExist(req.getId());
        memBaseinfo.setId(req.getId());
        DozerUtil.map(req, memBaseinfo);
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        DozerUtil.map(req, memBaseInfoDTO);
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public boolean updateMemLevel(Long memId, Integer memLevel) {
        MemBaseinfo memBaseinfo = checkMemIsExist(memId);
        memBaseinfo.setMemLevel(memLevel);
        memBaseinfo.setId(memId);
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setMemLevel(memLevel);
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public MemBaseinfo getMemBaseInfo(Long uid) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(uid);
        return memBaseinfo;
    }

    @Override
    public MemBaseDetailVO getMemBaseInfoByAccount(String account) {
        MemBaseinfo memBaseinfo = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, account));
        if(memBaseinfo==null){
            throw new BizException(ResultCode.DATA_NONENTITY);
        }
        MemBaseDetailVO memBaseDetailVO = new MemBaseDetailVO();
        BeanUtils.copyProperties(memBaseinfo, memBaseDetailVO);
        return memBaseDetailVO;
    }

    @Override
    @Transactional
    public boolean editStatus(MemEditStatusReq req) {
        MemBaseinfo memBaseinfo = checkMemIsExist(req.getId());
        Integer status = memBaseinfo.getStatus();
        Integer prohibitLogin = memBaseinfo.getProhibitLogin();
        DozerUtil.map(req, memBaseinfo);
        if (baseMapper.updateById(memBaseinfo) > 0) {
            MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
            refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
            if (status.equals(1) || status.equals(2) ||
                    prohibitLogin.equals(0)) {
                AdminBusinessRedisUtils.delMemAccToken(memBaseinfo.getAccount());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(Long memId) {
        MemBaseinfo memBaseinfo = checkMemIsExist(memId);
        memBaseinfo.setPasswordMd5(MD5.md5("12345678"));
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setPasswordMd5(memBaseinfo.getPasswordMd5());
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }


    @Override
    public void refreshMemBaseInfo(MemBaseInfoDTO memBaseInfoDTO, String account) {
        MemBaseInfoBO memBaseInfoBO = this.getMemCacheBaseInfo(account);
        if (null == memBaseInfoBO) {
            memBaseInfoBO = this.baseMapper.findMemBaseInfoByAccount(account);
        }
        BeanUtil.copyProperties(memBaseInfoDTO, memBaseInfoBO,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        BusinessRedisUtils.saveMemBaseInfo(memBaseInfoBO);
    }
    /**
     * 查询重复IP用户信息
     * @param req
     * @return
     */
    @Override
    public Page findIpRepeatPage(MemBaseInfoReq req) {
        Page<MemBaseInfoBO> page = new Page<>(req.getPage(), req.getLimit());
        page = baseMapper.findIpRepeatAll(page,req);
        return page;
    }

    public MemBaseinfo checkMemIsExist(Long id) {
        MemBaseinfo memBaseinfo = this.baseMapper.selectById(id);
        if (memBaseinfo == null) {
            throw new BizException(ResultCode.DATA_NONENTITY);
        }
        return memBaseinfo;
    }
}
