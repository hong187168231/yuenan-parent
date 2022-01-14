package com.indo.core.base.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    