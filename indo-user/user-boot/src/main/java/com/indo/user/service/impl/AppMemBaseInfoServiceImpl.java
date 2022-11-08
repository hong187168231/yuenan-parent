package com.indo.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.indo.common.enums.AccountTypeEnum;
import com.indo.common.enums.GiftTypeEnum;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.DeviceInfoUtil;
import com.indo.common.utils.ShareCodeUtil;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.*;
import com.indo.core.pojo.vo.LoanRecordVo;
import com.indo.core.service.ILoanRecordService;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.util.BusinessRedisUtils;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.MemBaseInfoMapper;
import com.indo.user.mapper.MemGiftReceiveMapper;
import com.indo.user.mapper.MemInviteCodeMapper;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.AppMemBaseInfoService;
import com.indo.user.service.IMemAgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppMemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo>
		implements AppMemBaseInfoService {


	@Resource
	private IMemAgentService memAgentService;

	@Resource
	private MemInviteCodeMapper memInviteCodeMapper;

	@Resource
	private MemLevelMapper memLevelMapper;

	@Resource
	private MemGiftReceiveMapper memGiftReceiveMapper;

	@Resource
	private IMemGoldChangeService iMemGoldChangeService;

	@Resource
	private ILoanRecordService loanRecordService;

	@Value("${google.client_id}")
	private String googleClientId;

	@Override
	public boolean checkAccount(String account) {
		LambdaQueryWrapper<MemBaseinfo> checkWrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotBlank(account)) {
			checkWrapper.eq(MemBaseinfo::getAccount, account);
		}
		return baseMapper.selectCount(checkWrapper) > 0;
	}

	@Override
	public Result<AppLoginVo> appLogin(LoginReq req, HttpServletRequest request) {
		//黑名单校验
//        List<SysIpLimit> list =sysIpLimitClient.findSysIpLimitByType(1).getData();
//        if(!CollectionUtils.isEmpty(list)){
//            // 获取请求信息
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//            String clientIP = IPUtils.getIpAddr(request);
//            Boolean status = false;
//            for(SysIpLimit l :list){
//                if(l.getIp().equals(clientIP)){
//                    status=true;
//                }
//            }
//            if(status){
//                throw new BizException("非法的IP登录");
//            }
//        }
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
			String countryCode = request.getHeader("countryCode");
			throw new BizException(MessageUtils.get("u170010", countryCode));
		}
		modifyLogin(userInfo);
		String accToken = UserBusinessRedisUtils.createMemAccToken(userInfo);
		//返回登录信息
		AppLoginVo appLoginVo = this.getAppLoginVo(accToken, userInfo);
		return Result.success(appLoginVo);
	}

	/**
	 * 谷歌登录
	 *
	 * @return
	 */
	private AppLoginVo googleLogin(String token) throws GeneralSecurityException, IOException {
		final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(Collections.singletonList(googleClientId))
				.build();
		GoogleIdToken idToken = verifier.verify(token);
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = payload.getSubject();
			log.info("google Login User ID: " + userId);
			String email = payload.getEmail();
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");
		} else {
			log.info("Invalid ID token.");
			throw new BizException("Invalid ID token");
		}
		return null;
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
	public Result<AppLoginVo> register(RegisterReq req, HttpServletRequest request) {
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
				String countryCode = request.getHeader("countryCode");
				throw new BizException(MessageUtils.get("u170011", countryCode));
			}
		}
		//查询是否存在当前用户
		if (this.findMemBaseInfo(req.getAccount()) != null) {
			return Result.failed("账号已存在！");
		}
		MemBaseinfo userInfo = new MemBaseinfo();
		userInfo.setAccount(req.getAccount());
		userInfo.setAccType(AccountTypeEnum.AGENT.getValue());
		userInfo.setPassword(req.getPassword());
		userInfo.setPasswordMd5(req.getPassword());
		userInfo.setRealName(req.getRealUserName());
		userInfo.setEmail(req.getEmail());
		// 11对应等级1级
		userInfo.setMemLevel(11);
		if (StringUtils.isNotBlank(DeviceInfoUtil.getDeviceId())) {
			userInfo.setDeviceCode(DeviceInfoUtil.getDeviceId());
		}
		if (StringUtils.isNotBlank(DeviceInfoUtil.getSource())) {
			userInfo.setRegisterSource(DeviceInfoUtil.getSource());
		}
		//保存注册信息
		initRegister(userInfo, memInviteCode);
		MemBaseInfoBO memBaseinfoBo = DozerUtil.map(userInfo, MemBaseInfoBO.class);
		String accToken = UserBusinessRedisUtils.createMemAccToken(memBaseinfoBo);
		//注册送30000越南盾注册金
		MemGiftReceive memGiftReceive = new MemGiftReceive();
		memGiftReceive.setMemId(memBaseinfoBo.getId());
		memGiftReceive.setGiftType(GiftTypeEnum.register.getCode());
		memGiftReceive.setGiftCode(GiftTypeEnum.register.name());
		memGiftReceive.setGiftName(GiftTypeEnum.register.getName());
		memGiftReceive.setGiftAmount(new BigDecimal(30000));
		memGiftReceiveMapper.insert(memGiftReceive);
		MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
		agentRebateChange.setChangeAmount(memGiftReceive.getGiftAmount());
		agentRebateChange.setTradingEnum(TradingEnum.INCOME);
		agentRebateChange.setGoldchangeEnum(GoldchangeEnum.register);
		agentRebateChange.setUserId(memBaseinfoBo.getId());
		iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
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
		memBaseinfo.setLastLoginTime(nowDate);
		this.baseMapper.insert(memBaseinfo);
		if (ObjectUtil.isNotNull(parentInviteCode)) {
			initMemAgent(memBaseinfo, parentInviteCode);
		}

		MemInviteCode memInviteCode = new MemInviteCode();
		memInviteCode.setAccount(memBaseinfo.getAccount());
		String code = ShareCodeUtil.inviteCode(memBaseinfo.getId());
		memInviteCode.setInviteCode(code.toLowerCase());
		memInviteCode.setMemId(memBaseinfo.getId());
		memInviteCode.setStatus(1);
		memInviteCodeMapper.insert(memInviteCode);
		return memBaseinfo;
	}


	public void initMemAgent(MemBaseinfo memBaseinfo, MemInviteCode parentInviteCode) {
		LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper();
		wrapper.eq(AgentRelation::getParentId, parentInviteCode.getMemId())
				.eq(AgentRelation::getStatus, 1);
		AgentRelation parentRelationAgent = memAgentService.getOne(wrapper);
		if (parentRelationAgent == null) {
			AgentRelation agentRelation = new AgentRelation();
			agentRelation.setMemId(memBaseinfo.getId());
			agentRelation.setAccount(memBaseinfo.getAccount());
			agentRelation.setStatus(1);
			agentRelation.setSubUserIds(memBaseinfo.getId() + "");
			agentRelation.setTeamNum(1);
			agentRelation.setParentId(parentInviteCode.getMemId());
			agentRelation.setSuperior(parentInviteCode.getAccount());
			agentRelation.setUpdateTime(new Date());
			memAgentService.save(agentRelation);
		} else {
			String subUserIds = StringUtils.isBlank(parentRelationAgent.getSubUserIds()) ?
					memBaseinfo.getId() + "" : parentRelationAgent.getSubUserIds() + "," + memBaseinfo.getId();
			parentRelationAgent.setSubUserIds(subUserIds);
			parentRelationAgent.setTeamNum(parentRelationAgent.getTeamNum() + 1);
			parentRelationAgent.setUpdateTime(new Date());
			memAgentService.updateById(parentRelationAgent);
		}
	}

	@Override
	@Transactional
	public boolean updatePassword(UpdatePasswordReq req, LoginInfo loginUser, HttpServletRequest request) {
		MemBaseinfo memBaseinfo = this.baseMapper.selectById(loginUser.getId());
		if (!memBaseinfo.getPasswordMd5().equals(req.getOldPassword())) {
			String countryCode = request.getHeader("countryCode");
			throw new BizException(MessageUtils.get("u170012", countryCode));
		}
		if (memBaseinfo.getPasswordMd5().equals(req.getNewPassword())) {
			String countryCode = request.getHeader("countryCode");
			throw new BizException(MessageUtils.get("u170013", countryCode));
		}
		memBaseinfo.setPasswordMd5(req.getNewPassword());
		MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
		memBaseInfoDTO.setPasswordMd5(memBaseinfo.getPasswordMd5());
		refreshMemBaseInfo(memBaseInfoDTO, loginUser.getAccount());
		return this.baseMapper.updateById(memBaseinfo) > 0;
	}


	@Override
	public MemBaseInfoVo getMemBaseInfo(String account, HttpServletRequest request) {
		MemBaseInfoBO cacheMemBaseInfo = this.getMemCacheBaseInfo(account);
		if (cacheMemBaseInfo == null) {
			cacheMemBaseInfo = findMemBaseInfo(account);
		}
		MemBaseInfoVo vo = DozerUtil.map(cacheMemBaseInfo, MemBaseInfoVo.class);
		LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper();
		wrapper.eq(MemLevel::getId, vo.getMemLevel());
		MemLevel memLevel = memLevelMapper.selectOne(wrapper);
		vo.setLevel((memLevel == null || memLevel.getLevel() == null) ? 1 : memLevel.getLevel());
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setId(cacheMemBaseInfo.getId());
		loginInfo.setMemLevel(memLevel == null ? 11 : memLevel.getId().intValue());
		LoanRecordVo loanRecordVo = loanRecordService.findMemLoanInfo(loginInfo,request);
		vo.setLoanAmount(loanRecordVo == null ? BigDecimal.ZERO : loanRecordVo.getArrears());
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
		MemBaseInfoBO memBaseInfoBOFromDb = this.findMemBaseInfo(loginUser.getAccount());
		if (memBaseInfoBOFromDb == null) {
			return;
		}
		MemBaseinfo memBaseinfo = new MemBaseinfo();
		memBaseinfo.setPhone(req.getPhone());
		memBaseinfo.setBirthday(req.getBirthday());
		memBaseinfo.setFaceBook(req.getFacebook());
		memBaseinfo.setWhatsApp(req.getWhatsapp());
		memBaseinfo.setEmail(req.getEmail());
		if (org.apache.commons.lang3.StringUtils.isBlank(memBaseInfoBOFromDb.getRealName())) {
			memBaseinfo.setRealName(req.getRealUserName());
		}
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
