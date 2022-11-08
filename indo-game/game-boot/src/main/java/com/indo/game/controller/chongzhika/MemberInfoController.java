package com.indo.game.controller.chongzhika;

import com.indo.common.result.Result;
import com.indo.core.pojo.req.chongzhika.MemberInfoReq;
import com.indo.common.web.util.IPUtils;
import com.indo.core.service.chongzhika.IMemberInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/member")
public class MemberInfoController {

    @Autowired
    private IMemberInfoService memberInfoService;

    /**
     * 会员激活卡
     * @param memberInfoReq
     * @return
     */
    @ApiOperation(value = "会员激活卡", httpMethod = "POST")
    @PostMapping(value = "/activationCard")
    public Result activationCard(HttpServletRequest request, MemberInfoReq memberInfoReq){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        MemberInfoReq memberInfoReq = new MemberInfoReq();
//        memberInfoReq.setActivationAcct("yixiu");//激活账号
        if(null==memberInfoReq.getIpAddress()||"".equals(memberInfoReq.getIpAddress())){
            memberInfoReq.setIpAddress(IPUtils.getIpAddr(request));//IP地址
        }
//        memberInfoReq.setCardNo("24live00000002");
//        memberInfoReq.setCardPwd("654432");
        return memberInfoService.addMemberInfo(memberInfoReq,countryCode);
    }

}
