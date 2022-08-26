package com.indo.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.RebateStatVO;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.*;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.*;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.service.IMemAgentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-11
 */
@Service
public class AppAgentServiceImpl extends SuperServiceImpl<AgentRelationMapper, AgentRelation> implements IMemAgentService {


	@Autowired
	private AgentApplyMapper agentApplyMapper;
	@Autowired
	private AgentRelationMapper agentRelationMapper;
	@Autowired
	private AgentRebateRecordMapper memRebateRecordMapper;
	@Autowired
	private AgentRebateMapper agentRebateMapper;
	@Autowired
	private AgentCashApplyMapper agentCashApplyMapper;
	@Autowired
	private MemBankRelationMapper memBankRelationMapper;
	@Autowired
	private MemBaseInfoMapper memBaseInfoMapper;

	@Override
	public boolean apply(MemAgentApplyReq req, LoginInfo loginInfo) {
		String uuidKey = UserBusinessRedisUtils.get(req.getUuid());
		if (StringUtils.isEmpty(uuidKey) || !req.getImgCode().equalsIgnoreCase(uuidKey)) {
			throw new BizException("图像验证码错误！");
		}
		LambdaQueryWrapper<AgentApply> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AgentApply::getAccount, loginInfo.getAccount());
		AgentApply agentApply = agentApplyMapper.selectOne(wrapper);
		if (ObjectUtil.isNotNull(agentApply)) {
			if (agentApply.getStatus().equals(GlobalConstants.AGENT_APPLY_STATUS_AUDIT)) {
				throw new BizException("代理申请审核中，请勿重复提交!");
			}
			if (agentApply.getStatus().equals(GlobalConstants.AGENT_APPLY_STATUS_PASS)) {
				throw new BizException("您已经是代理了哦呦!");
			}
			if (agentApply.getStatus().equals(GlobalConstants.AGENT_APPLY_STATUS_REJECT)) {
				throw new BizException("代理申请被拒,请联系客服处理!");
			}
		}
		agentApply = new AgentApply();
		agentApply.setMemId(loginInfo.getId());
		agentApply.setAccount(loginInfo.getAccount());
		agentApply.setStatus(0);
		return agentApplyMapper.insert(agentApply) > 0;
	}

	@Override
	public Integer applyStatus(LoginInfo loginInfo) {
		Integer status = -1;
		LambdaQueryWrapper<AgentApply> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AgentApply::getAccount, loginInfo.getAccount());
		AgentApply agentApply = agentApplyMapper.selectOne(wrapper);
		if (ObjectUtil.isNotNull(agentApply)) {
			status = agentApply.getStatus();
		}
		return status;
	}

	@Override
	public boolean
	takeRebate(BigDecimal rebateAmount, Long memBankId, LoginInfo loginInfo) {
		LambdaQueryWrapper<AgentRebate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AgentRebate::getMemId, loginInfo.getId());
		AgentRebate agentRebate = agentRebateMapper.selectOne(wrapper);
		if (null == agentRebate) {
			throw new BizException("无返佣金额");
		}
		// 提现金额必须大于0
		if (rebateAmount.intValue() < 100) {
			throw new BizException("佣金提款金额必须在100以上!");
		}
		MemBank memBank = memBankRelationMapper.selectById(memBankId);
		if (memBank == null) {
			throw new BizException("银行卡信息为空!");
		}
		AgentCashApply agentCashApply = new AgentCashApply();
		agentCashApply.setMemId(loginInfo.getId());
		agentCashApply.setAccount(loginInfo.getAccount());
		agentCashApply.setMemLevel(loginInfo.getMemLevel());
		agentCashApply.setBankCardNo(memBank.getBankCardNo());
		agentCashApply.setBankName(memBank.getBankName());
		agentCashApply.setBranchBank(memBank.getBankBranch());
		agentCashApply.setCashStatus(0);
		agentCashApply.setCity(memBank.getCity());
		agentCashApply.setIfsc(memBank.getIfsc());
		return agentCashApplyMapper.insert(agentCashApply) > 0;
	}

	@Override
	public Page<AgentSubVO> subordinatePage(SubordinateAppReq req, LoginInfo loginInfo) {
		Page<AgentSubVO> page = new Page<>(req.getPage(), req.getLimit());
		List<AgentSubVO> agentSubVOList = agentRelationMapper.subordinateList(page, Arrays.asList(loginInfo.getId()));
		if (CollectionUtils.isEmpty(agentSubVOList)) {
			return page;
		}

		AgentSubVO agentSubVO = agentSubVOList.get(0);
		if (StringUtils.isBlank(agentSubVO.getSubUserIds())) {
			return page;
		}

		if (StringUtils.isNotEmpty(req.getSubAccount())) {
			MemBaseInfoBO memBaseInfoBO = memBaseInfoMapper.findMemBaseInfoByAccount(req.getSubAccount());
			if (memBaseInfoBO == null) {
			    return page;
      }

      List<String> subUserIdList = Arrays.stream(agentSubVO.getSubUserIds().split(",")).collect(Collectors.toList());
			if (!subUserIdList.contains(memBaseInfoBO.getId()+"")) {
          return page;
      }
			agentSubVOList = agentRelationMapper.subordinateList(page, Arrays.asList(memBaseInfoBO.getId()));
		} else {
			if (StringUtils.isNotEmpty(agentSubVO.getSubUserIds())) {
				List<Long> memIds = Arrays.asList(agentSubVO.getSubUserIds().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
				agentSubVOList = agentRelationMapper.subordinateList(page, memIds);
			}
		}
		page.setRecords(agentSubVOList);
		return page;
	}


	@Override
	public AgentRebateInfoVO rebateInfo(LoginInfo loginInfo) {
		AgentRebateInfoVO infoVO = new AgentRebateInfoVO();
		BigDecimal rebateAmount = new BigDecimal("0.00");
		LambdaQueryWrapper<AgentRebate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AgentRebate::getMemId, loginInfo.getId());
		AgentRebate agentRebate = agentRebateMapper.selectOne(wrapper);
		if (null == agentRebate) {
			infoVO.setRebateAmount(rebateAmount);
		} else {
			infoVO.setRebateAmount(agentRebate.getRebateAmount());
		}
		return infoVO;
	}

	@Override
	public Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req, LoginInfo loginInfo) {
		req.setAccount(loginInfo.getAccount());
		Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
		List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
		page.setRecords(list);
		return page;
	}

	@Override
	public RebateStatVO rebateStat(String beginTime, String endTime, LoginInfo loginInfo) {
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.MONTH, -3);  //设置为前3月
		dBefore = calendar.getTime();   //得到前3月的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
		beginTime = sdf.format(dBefore);    //格式化前3月的时间
		endTime = sdf.format(dNow); //格式化当前时间

		RebateStatVO statVO = new RebateStatVO();
		BigDecimal rebate = agentRelationMapper.selectRebateByTime(loginInfo.getAccount(), beginTime, endTime);
		Integer teamNum = agentRelationMapper.selectTeamNum(loginInfo.getAccount());
		BigDecimal teamRecharge = agentRelationMapper.selectTeamRecharge(loginInfo.getAccount(), beginTime, endTime);
		Integer dayAdd = agentRelationMapper.selectDayAddNum(loginInfo.getAccount());
		BigDecimal teamBet = agentRelationMapper.selectTeamBet(loginInfo.getAccount(), beginTime, endTime);

		calendar.setTime(dNow);
		calendar.add(Calendar.MONTH, -1);  //设置为前1月
		dBefore = calendar.getTime();   //得到前1月的时间
		beginTime = sdf.format(dBefore);    //格式化前1月的时间
		endTime = sdf.format(dNow); //格式化当前时间
		Integer monthAdd = agentRelationMapper.selectMonthAddNum(loginInfo.getAccount(), beginTime, endTime);

		statVO.setRebateAmount(rebate);
		statVO.setTeamNum(teamNum == null ? 0 : teamNum);
		statVO.setTeamRecharge(teamRecharge);
		statVO.setDayAddNum(dayAdd);
		statVO.setMonthAddNum(monthAdd);
		statVO.setTeamBet(teamBet);
		return statVO;
	}
}
