package com.indo.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.admin.api.SysIpLimitClient;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DeviceInfoUtil;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.IPUtils;
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
import com.indo.user.service.AppMemBaseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppMemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo> implements AppMemBaseInfoService {


    @Autowired
    private AgentRelationMapper memAgentMapper;

    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;

    @Resource
    private SysIpLimitClient sysIpLimitClient;

    @Override
    public boolean checkAccount(String account) {
        LambdaQueryWrapper<MemBaseinfo> checkWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(account)) {
            checkWrapper.eq(MemBaseinfo::getAccount, account);
        }
        return baseMapper.selectCount(checkWrapper) > 0;
    }

    @Override
    public Result<AppLoginVo> appLogin(LoginReq req) {
        //黑名单校验
        List<SysIpLimit> list =sysIpLimitClient.findSysIpLimitByType(1).getData();
        if(list!=null||list.size()>0){
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String clientIP = IPUtils.getIpAddr(request);
            Boolean status = false;
            for(SysIpLimit l :list){
                if(l.getIp().equals(clientIP)){
                    status=true;
                }
            }
            if(status){
                throw new BizException("非法的IP登录");
            }
        }
        if (StringUtils.isBlank(req.getAccount())) {
            return Result.failed("请填写账号！");
        }
        if (StringUtils.isBlank(req.getPassword())) {
            return Result.failed("请填写密码！");
        }
        req.setPassword(req.getPassword().toLowerCase());
        MemBaseInfoBO userInfo = findMemBaseInfo(req.getAccount());
        if (ObjectUtil.isNull(userInfo)) {
            return Result.failed("该账号不存在！");
        }
        //判断密码是否正确
        if (!req.getPassword().equals(userInfo.getPasswordMd5())) {
            return Result.failed("账号或密码错误！");
        }
        if (userInfo.getProhibitLogin().equals(1) || !userInfo.getStatus().equals(0)) {
            throw new BizException("你暂时不能登录,请联系管理员");
        }
        modifyLogin(userInfo);
        String accToken = UserBusinessRedisUtils.createMemAccToken(userInfo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken, userInfo);
        return Result.success(appLoginVo);
    }


    public void modifyLogin(MemBaseInfoBO baseInfoBO) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setLastLoginTime(new Date());
        memBaseinfo.setClientIp(DeviceInfoUtil.getIp());
        memBaseinfo.setId(baseInfoBO.getId());
        this.baseMapper.updateById(memBaseinfo);
    }

    @Override
    public boolean logout(LogOutReq req) {
        UserBusinessRedisUtils.delMemAccToken(req);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<AppLoginVo> register(RegisterReq req) {
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
            MemBaseInfoBO superiorMem = findMemBaseInfo(memInviteCode.getAccount());
            if (superiorMem.getProhibitInvite().equals(1)) {
                throw new BizException("该邀请人已被禁止发展下级");
            }
        }
        //查询是否存在当前用户
        if (this.findMemBaseInfo(req.getAccount()) != null) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo userInfo = new MemBaseinfo();
        userInfo.setAccount(req.getAccount());
        userInfo.setPassword(req.getPassword());
        userInfo.setPasswordMd5(req.getPassword());
        if (StringUtils.isNotBlank(DeviceInfoUtil.getDeviceId())) {
            userInfo.setDeviceCode(DeviceInfoUtil.getDeviceId());
        }
        if (StringUtils.isNotBlank(DeviceInfoUtil.getSource())) {
            userInfo.setRegisterSource(DeviceInfoUtil.getSource());
        }
        //注册送30000越南盾注册金
        userInfo.setBalance(new BigDecimal(30000));
        //保存注册信息
        initRegister(userInfo, memInviteCode);
        MemBaseInfoBO memBaseinfoBo = DozerUtil.map(userInfo, MemBaseInfoBO.class);
        String accToken = UserBusinessRedisUtils.createMemAccToken(memBaseinfoBo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken, memBaseinfoBo);
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
        agentRelation.setAccount(memBaseinfo.getAccount());
        agentRelation.setStatus(0);
        agentRelation.setParentId(parentInviteCode.getMemId());
        agentRelation.setSuperior(parentInviteCode.getAccount());
        memAgentMapper.insert(agentRelation);
    }


    public void initMemParentAgent(MemBaseinfo memBaseinfo, MemInviteCode parentInviteCode) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AgentRelation::getMemId, parentInviteCode.getMemId())
                .eq(AgentRelation::getStatus, 1);
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
    @Transactional
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
        BeanUtil.copyProperties(memBaseInfoDTO, memBaseInfoBO,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        BusinessRedisUtils.saveMemBaseInfo(memBaseInfoBO);

    }

    @Override
    @Transactional
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
    @Transactional
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
     * 返回登录信息
     *
     * @param token
     * @param memBaseInfoBO
     * @return
     */
    private AppLoginVo getAppLoginVo(String token, MemBaseInfoBO memBaseInfoBO) {
        AppLoginVo appLoginVo = new AppLoginVo();
        appLoginVo.setToken(token);
        appLoginVo.setHeadImage(memBaseInfoBO.getHeadImage());
        return appLoginVo;
    }

    @Override
    public MemTradingBO tradingInfo(String account) {
        return this.baseMapper.tradingInfo(account);
    }

}
