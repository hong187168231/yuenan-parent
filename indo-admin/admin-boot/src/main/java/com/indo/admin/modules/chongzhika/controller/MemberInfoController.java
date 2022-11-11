package com.indo.admin.modules.chongzhika.controller;

import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.service.chongzhika.IMemberInfoService;
import com.indo.core.pojo.req.chongzhika.MemberInfoReq;
import com.indo.core.pojo.req.chongzhika.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/member")
@Api(tags = "激活卡密")
@AllArgsConstructor
@Slf4j
public class MemberInfoController {

    @Autowired
    private IMemberInfoService memberInfoService;

//    /**
//     * 会员激活卡
//     * @param memberInfoReq
//     * @return
//     */
//    @ApiOperation(value = "会员激活卡", httpMethod = "POST")
//    @PostMapping(value = "/activationCard")
//    public Result activationCard(HttpServletRequest request, MemberInfoReq memberInfoReq){
//        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
//        String countryCode = request.getHeader("countryCode");
////        MemberInfoReq memberInfoReq = new MemberInfoReq();
////        memberInfoReq.setActivationAcct("yixiu");//激活账号
//        if(null==memberInfoReq.getIpAddress()||"".equals(memberInfoReq.getIpAddress())){
//            memberInfoReq.setIpAddress(IPUtils.getIpAddr(request));//IP地址
//        }
////        memberInfoReq.setCardNo("24live00000002");
////        memberInfoReq.setCardPwd("654432");
//        return memberInfoService.addMemberInfo(memberInfoReq,countryCode);
//    }

    /**
     * 查询激活信息
     * @param memberInfoReq
     * @return
     */
    @ApiOperation(value = "查询激活信息", httpMethod = "POST")
    @PostMapping(value = "/queryActivationCard")
    public PageResult queryActivationCard(HttpServletRequest request, MemberInfoReq memberInfoReq, PageRequest pageRequest){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return memberInfoService.selectActivationCard(memberInfoReq,pageRequest,countryCode);
    }

    /**
     * 查询统计激活信息
     * @param memberInfoReq
     * @return
     */
    @ApiOperation(value = "查询统计激活信息", httpMethod = "POST")
    @PostMapping(value = "/queryStatiActivationCard")
    public PageResult queryStatiActivationCard(HttpServletRequest request, MemberInfoReq memberInfoReq, PageRequest pageRequest){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return memberInfoService.selectStatiActivationCard(memberInfoReq,pageRequest,countryCode);
    }

    /**
     * 处理卡
     * @param memberInfoReq
     * @return
     */
    @ApiOperation(value = "处理卡", httpMethod = "POST")
    @PostMapping(value = "/isHandleCard")
    public Result isHandleCard(HttpServletRequest request,MemberInfoReq memberInfoReq){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        User user = UserContext.getCurrebtUser();
//        if(null==user) {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("用户登录已失效请重新登录");
//            return result;
//        }
//        if("1".equals(user.getAuthority())){//1普通会员
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("您是普通会员，此功能只对管理员开放。");
//            return result;
//        }
        memberInfoReq.setUsername(JwtUtils.getUsername());
        memberInfoReq.setUserid(JwtUtils.getUserId());
//        if(null==memberInfoReq.getUserid()||"".equals(memberInfoReq.getUserid())){
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg(MessageUtils.get("a100002",countryCode));
//            return result;
//        }
//        if(null==memberInfoReq.getUsername()||"".equals(memberInfoReq.getUsername())){
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg(MessageUtils.get("a100003",countryCode));
//            return result;
//        }
        return memberInfoService.isHandleCard(memberInfoReq,countryCode);
    }
}
