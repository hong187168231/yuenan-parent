package com.indo.admin.modules.chongzhika.service;


import com.indo.admin.pojo.req.chongzhika.CardInfoReq;
import com.indo.admin.pojo.req.chongzhika.Result;

public interface ICardInfoService {

    public Result insertCardInfoBatch(CardInfoReq cardInfoReq,String countryCode);

    public Result selectCardInfoByisActivation(String countryCode);

    public Result selectCardNoPrefix(String countryCode);
}
