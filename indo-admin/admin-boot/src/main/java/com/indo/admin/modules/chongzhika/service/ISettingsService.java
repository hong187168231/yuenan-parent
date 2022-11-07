package com.indo.admin.modules.chongzhika.service;


import com.indo.core.pojo.req.chongzhika.Result;
import com.indo.admin.pojo.req.chongzhika.SettingsReq;

public interface ISettingsService {

    public Result querySettings();

    public Result modifySettings(SettingsReq settingsReq);
}
