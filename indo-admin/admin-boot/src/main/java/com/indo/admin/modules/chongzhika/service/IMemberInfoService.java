package com.indo.admin.modules.chongzhika.service;

import com.indo.admin.pojo.req.chongzhika.MemberInfoReq;
import com.indo.admin.pojo.req.chongzhika.PageRequest;
import com.indo.admin.pojo.req.chongzhika.Result;

public interface IMemberInfoService {
    public Result addMemberInfo(MemberInfoReq memberInfoReq,String countryCode);
    public Result selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest,String countryCode);
    public Result isHandleCard(MemberInfoReq memberInfoReq,String countryCode);
    public Result selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest,String countryCode);
}
