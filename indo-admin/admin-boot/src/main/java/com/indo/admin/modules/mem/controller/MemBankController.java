package com.indo.admin.modules.mem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.admin.modules.mem.service.IMemBankService;
import com.indo.admin.common.util.MinioUtil;
import com.indo.common.enums.FileBusinessType;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBank;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * <p>
 * 银行配置Controller
 * </p>
 *
 * @author mzh
 * @since 2021-08-30
 */
@Api(tags = "银行配置接口")
@RestController
@RequestMapping("/api/v1/bank")
public class MemBankController {

    @Autowired
    private IMemBankService iMemBankService;
    @Autowired
    private MinioUtil minioUtil;

    @ApiOperation(value = "查询银行信息")
    @GetMapping(value = "/list")
    public Result getList(MemBank memBank){
        return Result.success(iMemBankService.list(new QueryWrapper<MemBank>().lambda().eq(MemBank::getIsDel,0)));
    }

    @ApiOperation(value = "查询单个信息")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id){
        return Result.success(iMemBankService.getById(id));
    }

    @ApiOperation(value = "添加银行信息")
    @PostMapping(value = "/add")
    public Result add(@Validated @RequestBody MemBank memBank) {
        memBank.setCreateTime(new Date());
        boolean flag = iMemBankService.save(memBank);
        if(flag){
            return Result.success(HttpStatus.OK);
        }else {
            return Result.failed();
        }

    }

    @ApiOperation(value = "修改银行信息")
    @PostMapping(value = "/update")
    public Result update(@RequestBody MemBank memBank) {
        memBank.setUpdateTime(new Date());
        boolean flag = iMemBankService.saveOrUpdate(memBank);
        if(flag){
            return Result.success(HttpStatus.OK);
        }else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "删除银行")
    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Long id) {
        boolean flag = iMemBankService.removeById(id);
        if(flag){
            return Result.success(HttpStatus.OK);
        }else {
            return Result.failed();
        }
    }

    @ApiOperation("银行信息管理上传")
    @PostMapping(value = "/pics")
    @ResponseBody
    public Result uploadProductBrand(@RequestParam(value = "files") MultipartFile[] files) {
        return minioUtil.uploadFiles(files, FileBusinessType.BANK_INFO.getInfo());
    }
}
