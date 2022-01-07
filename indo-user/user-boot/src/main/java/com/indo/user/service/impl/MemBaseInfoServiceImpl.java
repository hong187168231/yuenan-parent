package com.indo.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.indo.common.constant.RedisConstants;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.BaseUtil;
import com.indo.common.utils.CopyUtils;
import com.indo.common.utils.DeviceInfoUtil;
import com.indo.common.utils.NameGeneratorUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.web.exception.BizException;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.MemAgentMapper;
import com.indo.user.mapper.MemBaseInfoMapper;
import com.indo.user.mapper.MemInviteCodeMapper;
import com.indo.user.mapper.MemSubordinateMapper;
import com.indo.user.pojo.entity.*;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import com.indo.user.service.IMemLevelService;
import com.indo.user.service.MemBaseInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        String accToken = UserBusinessRedisUtils.createMemAccToken(userInfo);
        //返回登录信息
        LoginInfo loginInfo = new LoginInfo();
        try {
            CopyUtils.conver(userInfo, loginInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken, userInfo);
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
        String accToken = UserBusinessRedisUtils.createMemAccToken(userInfo);
        //返回登录信息
        AppLoginVo appLoginVo = this.getAppLoginVo(accToken, userInfo);
        //清空图片验证码
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
    public MemBaseInfoVo getMemBaseInfo(Long id) {
        MemBaseinfo memBaseinfo = this.baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getId, id));
        if (null == memBaseinfo) {
            return null;
        }
        MemBaseInfoVo vo = new MemBaseInfoVo();
        BeanUtils.copyProperties(memBaseinfo, vo);
        vo.setAccount(memBaseinfo.getAccount());
        return vo;
    }

    @Override
    public Result<MemBaseInfoVo> getMemInfo(MemInfoReq req) {

        return null;
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
    private AppLoginVo getAppLoginVo(String token, MemBaseinfo memBaseInfo) {
        AppLoginVo appLoginVo = new AppLoginVo();
        appLoginVo.setToken(token);
//        appLoginVo.setNickName(userInfo.getNickname());
//        appLoginVo.setAccount(userInfo.getAccount());
//        appLoginVo.setMobilePhone(userInfo.getMobilePhone());
//        appLoginVo.setUid(userInfo.getId().toString());
//        appLoginVo.setSex(userInfo.getSex() == null ? 1 : userInfo.getSex());
//        appLoginVo.setHeadImage(minioConfig.getBaseUrl(userInfo.getHeadImage()));
//        Optional<Anchor> anchor = Optional.ofNullable(anchorRepositoryImpl.findAnchorByUid(userInfo.getId()));
//        if (anchor.isPresent()) {
//            appLoginVo.setRoomId(anchor.get().room_id);
//        }
//        Long days = DateUtils.getPastDaysWithNow(userInfo.getCreateTime().getTime());
//        appLoginVo.setRegisterDay(days.intValue());
        return appLoginVo;
    }


    @Override
    public int updateMemberAmount(BigDecimal amount, BigDecimal canAmount, Long userId) {
        int row = baseMapper.updateMemberAmount(amount, canAmount, userId);
        UserBusinessRedisUtils.deleteAppMember(userId);
        return row;
    }

    @Override
    public MemTradingVo tradingInfo(Long memId) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(memId);
        MemTradingVo memTradingVo = new MemTradingVo();
        BeanUtils.copyProperties(memBaseinfo, memTradingVo);
        return memTradingVo;
    }

    @Override
    public void upLevel(String payLoad) {
        Message message = JSONObject.parseObject(payLoad, Message.class);
        Long memId = (Long) message.getAttributes().get("memId");
        MemBaseinfo memBaseinfo = baseMapper.selectById(memId);
        if (memBaseinfo.getTotalDeposit().intValue() > 10000
                && memBaseinfo.getTotalBet().intValue() > 10000) {
            Integer level = iMemLevelService.getLevelByCondition(memBaseinfo.getTotalDeposit(), memBaseinfo.getTotalBet());
            if (level > memBaseinfo.getMemLevel()) {
                memBaseinfo.setMemLevel(level);
                baseMapper.updateById(memBaseinfo);
            }
        }
    }

}
