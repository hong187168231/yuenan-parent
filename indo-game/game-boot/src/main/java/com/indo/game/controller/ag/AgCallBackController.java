package com.indo.game.controller.ag;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.http.param.MediaType;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.ag.AgCallBackTransfer;
import com.indo.game.pojo.dto.ag.AgCallBackTransferReq;
import com.indo.game.service.ag.AgCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ag/callBack")
public class AgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AgCallbackService agCallbackService;

    /**
     * 延后转账(Post Transfer) - 視讯游戏 API
     */
    @RequestMapping(value="/rest/integration/postTransfer",consumes = MediaType.APPLICATION_XML,method=RequestMethod.POST,produces = MediaType.APPLICATION_XML)
    @ResponseBody
    @AllowAccess
    public Object postTransfer(@RequestBody AgCallBackTransferReq agCallBackTransferReq, HttpServletRequest request) {


        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("agCallBack  postTransfer 回调请求数据,IP:{} params:{}",ip,JSONObject.toJSONString(agCallBackTransferReq));
        AgCallBackTransfer agCallBackTransfer = agCallBackTransferReq.getRecord();
        Object object = agCallbackService.bet(agCallBackTransfer,ip);
        logger.info("agCallBack  postTransfer 回调返回数据, params:{}",object);
        return object;
    }

}
