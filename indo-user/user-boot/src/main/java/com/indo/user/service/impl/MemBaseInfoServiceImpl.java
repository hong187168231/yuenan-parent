package com.indo.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.BaseUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.MemInviteCode;
import com.indo.core.util.BusinessRedisUtils;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.AgentRelationMapper;
import com.indo.user.mapper.MemBaseInfoMapper;
import com.indo.user.mapper.MemInviteCodeMapper;
import com.indo.user.pojo.bo.MemTradingBO;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.MemBaseInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo> implements MemBaseInfoService {


    @Autowired
    private AgentRelationMapper memAgentMapper;

    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;

    @Override
    public Result<AppLoginVo> appLogin(LoginReq req) {
        if (StringUtils.isBlank(req.getAccount())) {
            return Result.failed("请填写账号！");
        }
        if (StringUtils.isBlank(req.getPassword())) {
            return Result.failed("请填写密码！");
        }
        req.setPassword(req.getPassword().toLowerCase());
        MemBaseInfoBO userInfo = findMemBaseInfo(req.getAccount());
        //判断密码是否正确
        if (!req.getPassword().equals(userInfo.getPasswordMd5())) {
            return Result.failed("密码错误！");
        }
        String accToken = UserBusinessRedisUtils.createMemAccToken(userInfo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken);
        return Result.success(appLoginVo);
    }

    @Override
    public boolean logout(LogOutReq req) {
        UserBusinessRedisUtils.delMemAccToken(req);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<AppLoginVo> register(RegisterReq req) {
        if (StringUtils.isBlank(req.getAccount())) {
            return Result.failed("请填写账号！");
        }
        if (!BaseUtil.checkIsNumOrletter(req.getAccount())) {
            return Result.failed("请输入4-11位字母或数字的用户名");
        }
        if (StringUtils.isBlank(req.getPassword())) {
            return Result.failed("请填写密码！");
        }
        if (StringUtils.isBlank(req.getConfirmPassword())) {
            return Result.failed("请填写确认密码！");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            return Result.failed("两次密码填写不一样！");
        }
        String uuidKey = UserBusinessRedisUtils.get(req.getUuid());
        if (StringUtils.isEmpty(uuidKey) || !req.getImgCode().equalsIgnoreCase(uuidKey)) {
            return Result.failed("图像验证码错误！");
        }
        // 验证邀请码
        MemInviteCode memInviteCode = null;
        if (StringUtils.isNotBlank(req.getInviteCode())) {
            LambdaQueryWrapper<MemInviteCode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemInviteCode::getInviteCode, req.getInviteCode());
            memInviteCode = memInviteCodeMapper.selectOne(wrapper);
            if (ObjectUtil.isNull(memInviteCode)) {
                return Result.failed("邀请码无效！");
            }
        }
        //查询是否存在当前用户
        if (this.findMemBaseInfo(req.getAccount()) != null) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo userInfo = new MemBaseinfo();
        userInfo.setAccount(req.getAccount());
        //userInfo.setSource(Integer.valueOf(DeviceInfoUtil.getSource()));
        userInfo.setPassword(req.getPassword());
        userInfo.setPasswordMd5(req.getPassword());
        if (StringUtils.isNotBlank(req.getDeviceCode())) {
            // userInfo.setDeviceCode(req.getDeviceCode());
        }
//        userInfo.setInviteCode(req.getInviteCode());
        //保存注册信息
        initRegister(userInfo, memInviteCode);
        MemBaseInfoBO memBaseinfoBo = DozerUtil.map(userInfo, MemBaseInfoBO.class);
        String accToken = UserBusinessRedisUtils.createMemAccToken(memBaseinfoBo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken);
        return Result.success(appLoginVo);

    }


    /**
     * 始化用户信息
     *
     * @param memBaseinfo
     * @return
     */
    public MemBaseinfo initRegister(MemBaseinfo memBaseinfo, MemInviteCode parentInviteCode) {
        Date nowDate = new Date();
        memBaseinfo.setAccType(1);
        memBaseinfo.setLastLoginTime(nowDate);
        //userInfo.setDeviceCode(DeviceInfoUtil.getDeviceId());
        this.baseMapper.insert(memBaseinfo);
        if (ObjectUtil.isNotNull(parentInviteCode)) {
            initMemAgent(memBaseinfo, parentInviteCode);
            initMemParentAgent(memBaseinfo, parentInviteCode);
        }
        return memBaseinfo;
    }


    public void initMemAgent(MemBaseinfo memBaseinfo, MemInviteCode parentInviteCode) {
        AgentRelation agentRelation = new AgentRelation();
        agentRelation.setMemId(memBaseinfo.getId());
        agentRelation.setIsDel(false);
        agentRelation.setParentId(parentInviteCode.getMemId());
        agentRelation.setSuperior(parentInviteCode.getAccount());
        memAgentMapper.insert(agentRelation);
    }


    public void initMemParentAgent(MemBaseinfo memBaseinfo, MemInviteCode parentInviteCode) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AgentRelation::getMemId, parentInviteCode.getMemId())
                .eq(AgentRelation::getIsDel, false);
        AgentRelation parentAgent = memAgentMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(wrapper)) {
            throw new BizException("该邀请人未成为代理");
        }
        String subUserIds = StringUtils.isBlank(parentAgent.getSubUserIds()) ?
                memBaseinfo.getId() + "" : parentAgent.getSubUserIds() + "," + memBaseinfo.getId();
        parentAgent.setSubUserIds(subUserIds);
        parentAgent.setTeamNum(parentAgent.getTeamNum() + 1);
        memAgentMapper.updateById(parentAgent);

    }


    @Override
    public boolean updatePassword(UpdatePasswordReq req, LoginInfo loginUser) {
        MemBaseinfo memBaseinfo = this.baseMapper.selectById(loginUser.getId());
        if (!memBaseinfo.getPasswordMd5().equals(req.getOldPassword())) {
            throw new BizException("旧密码输错误");
        }
        if (memBaseinfo.getPasswordMd5().equals(req.getNewPassword())) {
            throw new BizException("新密码和旧密码不能一样");
        }
        memBaseinfo.setPasswordMd5(req.getNewPassword());
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setPasswordMd5(memBaseinfo.getPasswordMd5());
        refreshMemBaseInfo(memBaseInfoDTO, loginUser.getAccount());
        return this.baseMapper.updateById(memBaseinfo) > 0;
    }


    @Override
    public MemBaseInfoVo getMemBaseInfo(String account) {
        MemBaseInfoBO cacheMemBaseInfo = this.getMemCacheBaseInfo(account);
        if (cacheMemBaseInfo == null) {
            cacheMemBaseInfo = findMemBaseInfo(account);
        }
        MemBaseInfoVo vo = DozerUtil.map(cacheMemBaseInfo, MemBaseInfoVo.class);
        return vo;
    }


    private void refreshMemBaseInfo(MemBaseInfoDTO memBaseInfoDTO, String account) {
        MemBaseInfoBO memBaseInfoBO = this.getMemCacheBaseInfo(account);
        if (null == memBaseInfoBO) {
            memBaseInfoBO = findMemBaseInfo(account);
        }
        DozerUtil.map(memBaseInfoDTO, memBaseInfoBO);
        BusinessRedisUtils.saveMemBaseInfo(memBaseInfoBO);

    }

    @Override
    public boolean updateHeadImage(String headImage, LoginInfo loginUser) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setHeadImage(headImage);
        memBaseinfo.setId(loginUser.getId());
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setHeadImage(memBaseinfo.getHeadImage());
        refreshMemBaseInfo(memBaseInfoDTO, loginUser.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public void updateBaseInfo(UpdateBaseInfoReq req, LoginInfo loginUser) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setPhone(req.getPhone());
        memBaseinfo.setBirthday(req.getBirthday());
        memBaseinfo.setFaceBook(req.getFacebook());
        memBaseinfo.setWhatsApp(req.getWhatsapp());
        memBaseinfo.setId(loginUser.getId());

        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        DozerUtil.map(memBaseinfo, memBaseInfoDTO);
        refreshMemBaseInfo(memBaseInfoDTO, loginUser.getAccount());
        this.baseMapper.updateById(memBaseinfo);
    }


    @Override
    public MemBaseInfoBO findMemBaseInfo(String account) {
        return this.baseMapper.findMemBaseInfoByAccount(account);
    }

    /**
     * 功能描述: 返回登录信息
     *
     * @auther:
     * @param: [token, userInfo]
     * @return: com.cp.common.vo.AppLoginVo
     * @date: 2020/8/5 14:59
     */
    private AppLoginVo getAppLoginVo(String token) {
        AppLoginVo appLoginVo = new AppLoginVo();
        appLoginVo.setToken(token);
        return appLoginVo;
    }


    @Override
    public MemTradingBO tradingInfo(String account) {
        return this.baseMapper.tradingInfo(account);
    }

}
