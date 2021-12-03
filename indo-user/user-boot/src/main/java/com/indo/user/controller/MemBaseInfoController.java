package com.indo.user.controller;

import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.service.MemBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membaseinfo")
public class MemBaseInfoController {

    @Autowired
    private MemBaseInfoService memBaseInfoService;

    @PostMapping("/getMemBaseInfoById")
    public MemBaseinfo getMemBaseInfoById(int id){
      return memBaseInfoService.getMemBaseInfoById(id);
    }
}
