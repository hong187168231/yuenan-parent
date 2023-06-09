package com.indo.core.service.chongzhika;

import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.core.pojo.entity.chongzhika.CardInfo;
import com.indo.core.pojo.entity.chongzhika.MemberInfo;
import com.indo.core.pojo.entity.chongzhika.Settings;
import com.indo.core.pojo.req.chongzhika.MemberInfoReq;
import com.indo.core.pojo.req.chongzhika.PageRequest;
import com.indo.core.mapper.chongzhika.CardInfoMapper;
import com.indo.core.mapper.chongzhika.MemberInfoMapper;
import com.indo.core.mapper.chongzhika.SettingsMapper;
import com.indo.core.pojo.vo.chongzhika.MemberInfoVo;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MemberInfoServiceImpl implements IMemberInfoService {
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private CardInfoMapper cardInfoMapper;
    @Resource
    private SettingsMapper settingsMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result addMemberInfo(MemberInfoReq memberInfoReq, String countryCode){
        if(null==memberInfoReq.getCardNo()||"".equals(memberInfoReq.getCardNo())){
//            result.failed("充值卡卡号不能为空");
            return Result.failed(MessageUtils.get("a100029",countryCode));
        }

        if(null==memberInfoReq.getCardPwd()||"".equals(memberInfoReq.getCardPwd())){
//            result.failed("充值卡密码不能为空");
            Result.failed(MessageUtils.get("a100030",countryCode));
        }
        if(null==memberInfoReq.getActivationAcct()||"".equals(memberInfoReq.getActivationAcct())){
//            result.failed("会员账号不能为空");
            return Result.failed(MessageUtils.get("a100031",countryCode));
        }
        if(null==memberInfoReq.getIpAddress()||"".equals(memberInfoReq.getIpAddress())){
//            return Result.failed("IP地址不能为空");
            return Result.failed(MessageUtils.get("a100032",countryCode));
        }
        Settings settings = settingsMapper.selectSettingsByid("1");
        if("0".equals(settings.getSwhLitIpActi())){//限制同一个ip激活开关 0开 1关闭
            List<MemberInfo> IPList = memberInfoMapper.selectMemberInfoByIp(memberInfoReq.getIpAddress());
            if(null!=IPList){
                if(IPList.size()>=settings.getLimitIpActi()){;//限制同一个ip激活数量
//                    return Result.failed("同一个ip只能激活（"+settings.getLimitIpActi()+"）个卡密");
                    return Result.failed(MessageUtils.get("a100033",countryCode)+" （"+settings.getLimitIpActi()+"） "+MessageUtils.get("a100034",countryCode));
                }
            }
        }
        //终身
        if ("0".equals(settings.getSwhLitAcctActi())){//限制同一个id账号激活开关 0开 1关闭
            List<MemberInfo> activationAcctList = memberInfoMapper.selectMemberInfoByActivationAcct(memberInfoReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActi()){//限制同一个id账号只能激活数量
//                    return Result.failed("同一个会员账号只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    return Result.failed(MessageUtils.get("a100035",countryCode)+"（"+settings.getLitAcctActi()+"） "+MessageUtils.get("a100036",countryCode));
                }
            }
        }
        //每天
        if ("0".equals(settings.getSwhLitAcctActiDay())){//限制同一个id账号激活开关 0开 1关闭
            List<MemberInfo> activationAcctList = memberInfoMapper.selectMemberInfoByActivationAcctToDays(memberInfoReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActiDay()){//限制同一个id账号只能激活数量
//                    return Result.failed("同一个会员账号每天只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    return Result.failed(MessageUtils.get("a100037",countryCode)+" ("+settings.getLitAcctActiDay()+") "+MessageUtils.get("a100038",countryCode));
                }
            }
        }


            CardInfo cardInfo = cardInfoMapper.selectCardInfoByCardNoAndCardPwd(memberInfoReq.getCardNo(),memberInfoReq.getCardPwd());
            if(null==cardInfo){
//                return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）不存在或者卡号密码错误");
                return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100040",countryCode));
            }
            if("1".equals(cardInfo.getIs_delete())){
//                return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）已经删除");
                return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100041",countryCode));
            }
            if("0".equals(cardInfo.getIsExp())){
//                return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）还未分发，暂不能激活");
                return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100042",countryCode));
            }
            if("1".equals(cardInfo.getIsActivation())){
//                return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）已经激活，同一张卡不能重复激活");
                return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100043",countryCode));
            }
            if("1".equals(cardInfo.getIsHandle())){
//                return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）已经处理，同一张卡不能重复激活");
                return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100044",countryCode));
            }
            cardInfo.setIsActivation("1");//0否   1激活
            cardInfo.setActivationAcct(memberInfoReq.getActivationAcct());//激活账号
            cardInfo.setUpdate_time(DateUtils.getDateTime(DateUtils.newFormat));
            cardInfoMapper.updateCardInfo(cardInfo);
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setActivationAcct(memberInfoReq.getActivationAcct());//激活账号
            memberInfo.setIpAddress(memberInfoReq.getIpAddress());//IP地址
            memberInfo.setDeviceInfo(memberInfoReq.getDeviceInfo());//设备信息
            memberInfo.setCardNoPrefix(cardInfo.getCardNoPrefix());//前缀
            memberInfo.setCardNo(memberInfoReq.getCardNo());//卡号=前缀+序号
            memberInfo.setCardPwd(memberInfoReq.getCardPwd());
            memberInfo.setIs_delete("0");//0否   1删除
            memberInfo.setIsHandle("0");//0否   1处理
            memberInfo.setCardAmount(cardInfo.getCardAmount());//卡面金额
            memberInfo.setAdditionalAmount(cardInfo.getAdditionalAmount());//增送金额
            memberInfo.setCreate_time(DateUtils.getDateTime(DateUtils.newFormat));
            memberInfoMapper.insertMemberInfo(memberInfo);
//            return Result.failed("充值卡（"+memberInfoReq.getCardNo()+"）激活成功，充值金额（"+cardInfo.getCardAmount()+"），增送金额（"+cardInfo.getAdditionalAmount()+"）");
//            return Result.failed(MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100045",countryCode)+"（"+cardInfo.getCardAmount()+"），"+MessageUtils.get("a100046",countryCode)+"（"+cardInfo.getAdditionalAmount()+"）");
        String msg = MessageUtils.get("a100039",countryCode)+"（"+memberInfoReq.getCardNo()+"）"+MessageUtils.get("a100045",countryCode)+"（"+cardInfo.getCardAmount()+"），"+MessageUtils.get("a100046",countryCode)+"（"+cardInfo.getAdditionalAmount()+"）";
        return Result.success(memberInfo,msg);

    }
    public PageResult selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest,String countryCode){
        PageResult result = new PageResult();


        try {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setCardNo(memberInfoReq.getCardNo());
            memberInfo.setActivationAcct(memberInfoReq.getActivationAcct());
            memberInfo.setIpAddress(memberInfoReq.getIpAddress());
            memberInfo.setIsHandle(memberInfoReq.getIsHandle());
            memberInfo.setCardNoPrefix(memberInfoReq.getCardNoPrefix());
            String sql = "";
            if(null!=memberInfoReq.getStartingTime()&&!"".equals(memberInfoReq.getStartingTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDateNewFormat(memberInfoReq.getStartingTime()),DateUtils.webFormat)+"'";
            }
            if(null!=memberInfoReq.getEndTime()&&!"".equals(memberInfoReq.getEndTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDateNewFormat(memberInfoReq.getEndTime()),DateUtils.webFormat)+"'";
            }
            List<MemberInfo> memberInfoList = memberInfoMapper.selectMemberInfo(memberInfoReq.getActivationAcct(),memberInfoReq.getCardNoPrefix(),memberInfoReq.getIpAddress(),memberInfoReq.getCardNo(),memberInfoReq.getIsHandle(),sql);
            List<MemberInfo> memberInfoList1 = new ArrayList<MemberInfo>();
            if(memberInfoList != null && memberInfoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                memberInfoList1 = memberInfoMapper.selectPageMemberInfo(memberInfoReq.getActivationAcct(),memberInfoReq.getCardNoPrefix(),memberInfoReq.getIpAddress(),memberInfoReq.getCardNo(),memberInfoReq.getIsHandle(),startIndex,pageRequest.getPageSize(),sql);
                result.setPageNo(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalCount(memberInfoList.size());//记录总数
                result.setTotalPage((memberInfoList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
//            result.failed(MessageUtils.get("a100047",countryCode));
            result.setData(memberInfoList1);
        } catch (Exception e) {
//            result.failed(MessageUtils.get("a100048",countryCode));
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result isHandleCard(MemberInfoReq memberInfoReq,String countryCode){
        if(null==memberInfoReq.getCardNo()||"".equals(memberInfoReq.getCardNo())){
            return Result.failed(MessageUtils.get("a100029",countryCode));
        }
        List<MemberInfo> list = memberInfoMapper.selectMemberInfoByCardNo(memberInfoReq.getCardNo());
        if(null!=list && list.size()>0) {
            for (int i=0;i<list.size();i++) {
                MemberInfo memberInfo = list.get(i);
                if (i==list.size()-1) {
                    String isActivation;//0否   1激活
                    String isHandle;
                    if ("2".equals(memberInfoReq.getIsHandle())) {//拒绝
                        isActivation = "0";
                        isHandle = "2";
                        memberInfoReq.setIsHandle("0");
                        memberInfoReq.setIs_delete("1");
                    } else {
                        isHandle = "1";
                        memberInfoReq.setIsHandle("1");
                        isActivation = "1";
                        memberInfoReq.setIs_delete("0");
                    }
                    cardInfoMapper.updateIsHandleByCardNo(memberInfoReq.getIsHandle(), isActivation, DateUtils.getDateTime(DateUtils.newFormat), memberInfoReq.getCardNo());
                    memberInfoMapper.updateIsHandleByCardNo(isHandle, memberInfoReq.getIs_delete(), memberInfoReq.getRemark(), DateUtils.getDateTime(DateUtils.newFormat), memberInfoReq.getUserid(), memberInfoReq.getUsername(), memberInfo.getId());
//                    return Result.failed(MessageUtils.get("a100049",countryCode));
                    return Result.success(memberInfo);
                }else {
                    memberInfoMapper.deleteMemberInfo(memberInfo.getId());
                }
            }
        }else {
            return Result.failed(MessageUtils.get("a100050",countryCode)+"(" + memberInfoReq.getCardNo() + ")"+MessageUtils.get("a100051",countryCode));
        }
        return Result.success();
    }

    public PageResult selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest, String countryCode){
        PageResult result = new PageResult();
        try {
            String sql = "";
            if(null!=memberInfoReq.getStartingTime()&&!"".equals(memberInfoReq.getStartingTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+ DateUtils.formatDate(DateUtils.parseDateNewFormat(memberInfoReq.getStartingTime()),DateUtils.webFormat)+"'";
            }
            if(null!=memberInfoReq.getEndTime()&&!"".equals(memberInfoReq.getEndTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDateNewFormat(memberInfoReq.getEndTime()),DateUtils.webFormat)+"'";
            }
            List<MemberInfoVo> memberInfovoList = memberInfoMapper.selectStatiActivationCard(memberInfoReq.getCardNoPrefix(),sql);
            List<MemberInfoVo> memberInfovoList1 = new ArrayList<MemberInfoVo>();
            if(memberInfovoList != null && memberInfovoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                memberInfovoList1 = memberInfoMapper.selectPageStatiActivationCard(memberInfoReq.getCardNoPrefix(),sql,startIndex,pageRequest.getPageSize());
                result.setPageNo(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalCount(memberInfovoList.size());//记录总数
                result.setTotalPage((memberInfovoList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
//            result.failed(MessageUtils.get("a100052",countryCode));
            result.setData(memberInfovoList1);
        } catch (Exception e) {
//            result.failed(MessageUtils.get("a100053",countryCode));
            e.printStackTrace();
        }
        return result;

    }
}
