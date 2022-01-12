package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.BaseUtil;
import com.indo.common.utils.NameGeneratorUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.MemAgentMapper;
import com.indo.user.mapper.MemBaseInfoMapper;
import com.indo.user.mapper.MemInviteCodeMapper;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.entity.MemInviteCode;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import com.indo.user.service.IMemLevelService;
import com.indo.user.service.MemBaseInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo> implements MemBaseInfoService {

    @Resource
    private MemBaseInfoMapper memBaseInfoMapper;

    @Autowired
    private MemAgentMapper memAgentMapper;

    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;


    @Autowired
    private IMemLevelService iMemLevelService;


    @Override
    public Result<AppLoginVo> appLogin(LoginReq req) {
        if (StringUtils.isBlank(req.getAccount())) {
            return Result.failed("请填写账号！");
        }
        if (StringUtils.isBlank(req.getPassword())) {
            return Result.failed("请填写密码！");
        }
        req.setPassword(req.getPassword().toLowerCase());
        MemBaseinfo userInfo = memBaseInfoMapper.
                selectOne(new LambdaQueryWrapper<MemBaseinfo>().eq(MemBaseinfo::getAccount, req.getAccount()));
        //判断密码是否正确
        if (!req.getPassword().equals(userInfo.getPasswordMd5())) {
            return Result.failed("密码错误！");
        }
        MemBaseinfoBo memBaseinfoBo = DozerUtil.map(userInfo, MemBaseinfoBo.class);
        String accToken = UserBusinessRedisUtils.createMemAccToken(memBaseinfoBo);
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
        if (req.getMobile().contains("-")) {
            req.getMobile().split("-");
        }


        req.setPassword(req.getPassword().toLowerCase());
        // 验证邀请码
        if (StringUtils.isNotBlank(req.getInviteCode())) {
            MemInviteCode memInviteCode = memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getInviteCode, req.getInviteCode()));
            if (Objects.isNull(memInviteCode)) {
                return Result.failed("邀请码无效！");
            }
        }
        //查询是否存在当前用户
        if (this.getByAccount(req.getAccount()) != null) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo userInfo = new MemBaseinfo();
        userInfo.setAccount(req.getAccount());
        //userInfo.setSource(Integer.valueOf(DeviceInfoUtil.getSource()));
        userInfo.setPassword(req.getPassword());
        userInfo.setPasswordMd5(req.getPassword());
        if (StringUtils.isNotBlank(req.getDeviceCode())) {
//            userInfo.setDeviceCode(req.getDeviceCode());
        }
        userInfo.setPhone(req.getMobile());
        userInfo.setInviteCode(req.getInviteCode());
        //保存注册信息
        initRegister(userInfo);
        MemBaseinfoBo memBaseinfoBo = DozerUtil.map(userInfo, MemBaseinfoBo.class);
        String accToken = UserBusinessRedisUtils.createMemAccToken(memBaseinfoBo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken);
        return Result.success(appLoginVo);

    }


    /**
     * 始化用户信息
     *
     * @param userInfo
     * @return
     */
    private MemBaseinfo initRegister(MemBaseinfo userInfo) {
        String inviteCode = userInfo.getInviteCode();
        //初始化
        Date nowDate = new Date();

        if (StringUtils.isBlank(userInfo.getNickName())) {
            userInfo.setNickName(NameGeneratorUtil.generate());
        }
//        userInfo.setInviteCode(productInviteCode());
//        userInfo.setLastLoginTime(nowDate);
        userInfo.setAccType(1);
//        userInfo.setLastLoginTime(nowDate);
//        userInfo.setHeadUrl("");
//        userInfo.setDeviceCode(DeviceInfoUtil.getDeviceId());
        this.baseMapper.insert(userInfo);
        //初始化钱包
        Long uid = userInfo.getId();
//        this.initData(uid, nowDate);

        if (StringUtils.isNotBlank(inviteCode)) {
            initMemAgent(userInfo, inviteCode);
            initMemParentAgent(userInfo, inviteCode);
        }
        return userInfo;
    }


    public void initMemAgent(MemBaseinfo memBaseinfo, String inviteCode) {
        MemInviteCode memInviteCode = this.memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getInviteCode, inviteCode));
        MemAgent memAgent = new MemAgent();
        memAgent = new MemAgent();
        memAgent.setMemId(memBaseinfo.getId());
        memAgent.setIsDel(false);
        memAgent.setParentId(memInviteCode.getMemId());
        memAgent.setSuperior(memInviteCode.getAccount());
        memAgentMapper.insert(memAgent);
    }

    public void initMemParentAgent(MemBaseinfo memBaseinfo, String inviteCode) {
        MemInviteCode memInviteCode = this.memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getInviteCode, inviteCode));
        MemAgent memAgent = memAgentMapper.selectOne(new QueryWrapper<MemAgent>().lambda().eq(MemAgent::getMemId, memInviteCode.getMemId()));
        memAgent.setSubNum(memAgent.getSubNum() + 1);
        memAgent.setLevelUserIds(StringUtils.isBlank(memAgent.getLevelUserIds()) ? memBaseinfo.getId().toString() : memAgent.getLevelUserIds() + "," + memBaseinfo.getId());
        memAgent.setTeamNum(memAgent.getTeamNum() + 1);
        memAgentMapper.updateById(memAgent);

    }

    @Override
    public MemBaseInfoVo getMemBaseInfo(String account) {
        MemBaseinfoBo cacheMemBaseInfo = this.getMemCacheBaseInfo(account);
        MemBaseInfoVo vo = DozerUtil.map(cacheMemBaseInfo, MemBaseInfoVo.class);
        return vo;
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
        return this.baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public boolean updateHeadImage(String headImage, LoginInfo loginUser) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setHeadImage(headImage);
        memBaseinfo.setId(loginUser.getId());
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
        this.baseMapper.updateById(memBaseinfo);
    }


    @Override
    public MemBaseinfo getMemBaseInfoById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public MemBaseinfo getByAccount(String account) {
        return baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, account));
    }

    @Override
    public MemBaseinfo findByMobile(String mobile) {
        return baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getPhone, mobile));
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
    public MemTradingVo tradingInfo(Long memId) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(memId);
        MemTradingVo memTradingVo = DozerUtil.map(memBaseinfo, MemTradingVo.class);
        return memTradingVo;
    }


}
