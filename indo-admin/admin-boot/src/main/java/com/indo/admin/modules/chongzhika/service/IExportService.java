package com.indo.admin.modules.chongzhika.service;

import com.indo.core.pojo.req.chongzhika.Result;

import javax.servlet.http.HttpServletResponse;

public interface IExportService {

    public Result exportCardInfo(HttpServletResponse response, String cardNoPrefix,String countryCode);
}
