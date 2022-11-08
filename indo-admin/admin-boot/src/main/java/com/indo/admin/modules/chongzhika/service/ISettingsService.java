package com.indo.admin.modules.chongzhika.service;


import com.indo.admin.pojo.req.chongzhika.SettingsReq;
import com.indo.common.result.Result;

public interface ISettingsService {

    public Result querySettings();

    public Result modifySettings(SettingsReq settingsReq);
}
