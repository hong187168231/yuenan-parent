/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.indo.admin.modules.sys.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageInfo;
import com.indo.admin.modules.sys.mapper.SysLogMapper;
import com.indo.admin.modules.sys.service.ISysLogService;
import com.indo.admin.pojo.criteria.SysLogQueryCriteria;
import com.indo.admin.pojo.dto.LogErrorDTO;
import com.indo.admin.pojo.dto.LogSmallDTO;
import com.indo.admin.pojo.entity.SysLog;
import com.indo.common.annotation.Log;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.QueryHelpPlus;
import com.indo.common.utils.ValidationUtil;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author puff
 * @date 2021-8-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysLogServiceImpl extends SuperServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    private SysLogMapper logMapper;

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Object findAllByPageable(String nickname, Pageable pageable) {
//        getPage(pageable);
        PageInfo<SysLog> page = new PageInfo(logMapper.findAllByPageable(nickname));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public Object queryAll(SysLogQueryCriteria criteria, Pageable pageable) {

//        getPage(pageable);
        PageInfo<SysLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        String status = "ERROR";
        if (status.equals(criteria.getLogType())) {
            map.put("content", dozerUtil.convert(page.getList(), LogErrorDTO.class));
            map.put("totalElements", page.getTotal());
        }
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }

    @Override
    public List<SysLog> queryAll(SysLogQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SysLog.class, criteria));
    }

    @Override
    public Object queryAllByUser(SysLogQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
        PageInfo<SysLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", dozerUtil.convert(page.getList(), LogSmallDTO.class));
        map.put("totalElements", page.getTotal());
        return map;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String ip, ProceedingJoinPoint joinPoint,
                     SysLog log, Long uid) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        //类型 0-后台 1-前台
        log.setType(aopLog.type());
        if (uid != null) {
            log.setUid(uid);
        }
        assert log != null;
        log.setRequestIp(ip);

        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).get("username").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.setAddress(IPAddressUtil.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        this.save(log);
    }

    @Override
    public Object findByErrDetail(Long id) {
        SysLog log = this.getById(id);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        logMapper.deleteByLogType("ERROR");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logMapper.deleteByLogType("INFO");
    }
}
