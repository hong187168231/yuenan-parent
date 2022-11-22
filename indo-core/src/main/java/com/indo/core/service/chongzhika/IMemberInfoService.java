package com.indo.core.service.chongzhika;


import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.core.pojo.req.chongzhika.MemberInfoReq;
import com.indo.core.pojo.req.chongzhika.PageRequest;

public interface IMemberInfoService {
    public Result addMemberInfo(MemberInfoReq memberInfoReq, String countryCode);
    public PageResult selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest, String countryCode);
    public Result isHandleCard(MemberInfoReq memberInfoReq,String countryCode);
    public PageResult selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest, String countryCode);
}
