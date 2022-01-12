package com.indo.core.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @ClassName BaseController
 * @Desc 基本接口类
 * @Date 2021/3/27 17:07
 */
@Controller
@Slf4j
public class BaseController {

    @Resource
    protected HttpServletRequest request;


}
    