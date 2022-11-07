package com.indo.core.service.chongzhika;


import com.indo.core.pojo.req.chongzhika.MemberInfoReq;
import com.indo.core.pojo.req.chongzhika.PageRequest;
import com.indo.core.pojo.req.chongzhika.Result;

public interface IMemberInfoService {
    public Result addMemberInfo(MemberInfoReq memberInfoReq, String countryCode);
    public Result selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest, String countryCode);
    public Result isHandleCard(MemberInfoReq memberInfoReq,String countryCode);
    public Result selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest,String countryCode);
}
