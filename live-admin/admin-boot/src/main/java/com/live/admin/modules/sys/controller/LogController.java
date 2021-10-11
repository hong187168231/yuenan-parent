
package com.live.admin.modules.sys.controller;

import com.live.admin.modules.sys.service.ISysLogService;
import com.live.admin.pojo.criteria.SysLogQueryCriteria;
import com.live.common.annotation.Log;
import com.live.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author puff
 * @date 2021-8-24
 */
@RestController
@RequestMapping("/api/logs")
@Api(tags = "监控：日志管理")
@SuppressWarnings("unchecked")
public class LogController {

    @Resource
    private ISysLogService logService;

    @GetMapping
    @ApiOperation("日志查询")
    public ResponseEntity<Object> getLogs(SysLogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("INFO");
        criteria.setType(0);
        return new ResponseEntity<>(logService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/mlogs")
    public ResponseEntity getApiLogs(SysLogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("INFO");
        criteria.setType(1);
        return new ResponseEntity(logService.findAllByPageable(criteria.getBlurry(), pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity<Object> getUserLogs(SysLogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("INFO");
        criteria.setBlurry(JwtUtils.getUsername());
        return new ResponseEntity<>(logService.queryAllByUser(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    public ResponseEntity<Object> getErrorLogs(SysLogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("ERROR");
        return new ResponseEntity<>(logService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    public ResponseEntity<Object> getErrorLogs(@PathVariable Long id) {
        return new ResponseEntity<>(logService.findByErrDetail(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/del/error")
    @Log("删除所有ERROR日志")
    @ApiOperation("删除所有ERROR日志")
    public ResponseEntity<Object> delAllByError() {
        logService.delAllByError();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/del/info")
    @Log("删除所有INFO日志")
    @ApiOperation("删除所有INFO日志")
    public ResponseEntity<Object> delAllByInfo() {
        logService.delAllByInfo();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
