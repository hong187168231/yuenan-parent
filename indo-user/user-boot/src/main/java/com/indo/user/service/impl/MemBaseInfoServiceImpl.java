package com.indo.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.constant.RedisConstants;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.BaseUtil;
import com.indo.common.utils.CopyUtils;
import com.indo.common.utils.DeviceInfoUtil;
import com.indo.common.utils.NameGeneratorUtil;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.MemBaseInfoMapper;
import com.indo.user.mapper.MemSubordinateMapper;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.entity.MemSubordinate;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.MemBaseInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
    private MemSubordinateMapper memSubordinateMapper;

    @Autowired
    private RedisUtils redisUtil;

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
                selectOne(new LambdaQueryWrapper<MemBaseinfo>().eq(MemBaseinfo::getAccno, req.getAccount()));
        //判断密码是否正确
        if (!req.getPassword().equals(userInfo.getPassword())) {
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
        req.setPassword(req.getPassword().toLowerCase());
        // 验证邀请码
        if (StringUtils.isNotBlank(req.getInviteCode())) {
            MemBaseinfo baseinfo = this.baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getRInviteCode, req.getInviteCode()));
            if (Objects.isNull(baseinfo)) {
                return Result.failed("邀请码无效！");
            }
        }
        //查询是否存在当前用户
        if (this.getMemBaseInfoByAccount(req.getAccount()) != null) {
            return Result.failed("账号已存在！");
        }
        MemBaseinfo userInfo = new MemBaseinfo();
        userInfo.setAccno(req.getAccount());
        //userInfo.setSource(Integer.valueOf(DeviceInfoUtil.getSource()));
        userInfo.setPassword(req.getPassword());
        if (StringUtils.isNotBlank(req.getDeviceCode())) {
//            userInfo.setDeviceCode(req.getDeviceCode());
        }
        userInfo.setPhone(req.getMobile());
        userInfo.setRInviteCode(req.getInviteCode());
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
        String inviteCode = userInfo.getRInviteCode();
        //初始化
        Date nowDate = new Date();

        if (StringUtils.isBlank(userInfo.getNickName())) {
            userInfo.setNickName(NameGeneratorUtil.generate());
        }
        userInfo.setRInviteCode(productInviteCode());
//        userInfo.setLastLoginTime(nowDate);
        userInfo.setAcc_type(0);
//        userInfo.setLastLoginTime(nowDate);
//        userInfo.setHeadUrl("");
//        userInfo.setDeviceCode(DeviceInfoUtil.getDeviceId());
        this.baseMapper.insert(userInfo);
        //初始化钱包
        Long uid = userInfo.getId();
//        this.initData(uid, nowDate);

        //初始化用户下级
        MemSubordinate memSubordinate = new MemSubordinate();
        memSubordinate.setParentId(0L);
        memSubordinate.setHierarchy(1);
        if (StringUtils.isNotBlank(inviteCode)) {
            MemBaseinfo baseinfo = this.baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getRInviteCode, inviteCode));
            MemSubordinate subordinate = memSubordinateMapper.selectOne(new QueryWrapper<MemSubordinate>().lambda().eq(MemSubordinate::getMemId, baseinfo.getId()));
            subordinate.setLevelNum(subordinate.getLevelNum() + 1);
            subordinate.setLevelUserIds(StringUtils.isBlank(subordinate.getLevelUserIds()) ? userInfo.getId().toString() : subordinate.getLevelUserIds() + "," + userInfo.getId());
            subordinate.setUpdateTime(new Date());
            subordinate.setTeamNum(subordinate.getTeamNum() + 1);
            memSubordinate.setHierarchy(subordinate.getHierarchy() + 1);
            memSubordinateMapper.updateById(subordinate);
            memSubordinate.setParentId(baseinfo.getId());
            //更新团队数
            updateTeamNum(subordinate.getParentId());
        }
        memSubordinate.setMemId(userInfo.getId());
        memSubordinate.setCreateTime(new Date());
        memSubordinate.setLevelNum(0);
        memSubordinate.setTeamNum(0);
        memSubordinate.setIsDel(false);
        memSubordinateMapper.insert(memSubordinate);
        return userInfo;
    }


    @Override
    public MemBaseInfoVo getMemBaseInfoByAccount(String account) {
        MemBaseinfo memBaseinfo = this.baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccno, account));
        MemBaseInfoVo vo = new MemBaseInfoVo();
        BeanUtils.copyProperties(memBaseinfo, vo);
        return vo;
    }

    @Override
    public Result<MemBaseInfoVo> getMemInfo(MemInfoReq req) {

        return null;
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

    /**
     * 生成邀请码
     *
     * @return
     */
    private String productInviteCode() {
        String inviteCode = RandomUtil.randomString(3);
        redisUtil.incr(RedisConstants.MEM_GENERATE_INVITATION_CODE, 1);
        String redisStr = String.format("%03d", redisUtil.get(RedisConstants.MEM_GENERATE_INVITATION_CODE));
        return inviteCode + redisStr;
    }

    /**
     * 团队数更新
     *
     * @param inviteUserId
     */
    private void updateTeamNum(Long inviteUserId) {
        if (inviteUserId == 0) {
            return;
        }
        MemSubordinate subordinate = memSubordinateMapper.selectOne(new QueryWrapper<MemSubordinate>().lambda().eq(MemSubordinate::getMemId, inviteUserId));
        subordinate.setTeamNum(subordinate.getTeamNum() + 1);
        memSubordinateMapper.updateById(subordinate);
        updateTeamNum(subordinate.getParentId());
    }

}
