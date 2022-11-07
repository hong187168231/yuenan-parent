package com.indo.admin.modules.chongzhika.controller;

import com.indo.admin.modules.chongzhika.service.ISettingsService;
import com.indo.core.pojo.req.chongzhika.Result;
import com.indo.admin.pojo.req.chongzhika.SettingsReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private ISettingsService settingsService;

    /**
     * 查询后台设置
     * @return
     */
    @ApiOperation(value = "查询后台设置", httpMethod = "GET")
    @GetMapping(value = "/querySettings")
    public Result querySettings(HttpServletRequest request){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        User user = UserContext.getCurrebtUser();
//        if(null==user) {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("用户登录已失效请重新登录");
//            return result;
//        }
        return settingsService.querySettings();
    }

    /**
     * 修改后台设置
     * @param settingsReq
     * @return
     */
    @ApiOperation(value = "修改后台设置", httpMethod = "POST")
    @PostMapping(value = "/modifySettings")
    public Result modifySettings(HttpServletRequest request,SettingsReq settingsReq){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        User user = UserContext.getCurrebtUser();
//        if(null==user) {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("用户登录已失效请重新登录");
//            return result;
//        }
        return settingsService.modifySettings(settingsReq);
    }
}
