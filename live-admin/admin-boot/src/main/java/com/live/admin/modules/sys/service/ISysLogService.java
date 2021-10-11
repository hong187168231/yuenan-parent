package com.live.admin.modules.sys.service;

import com.live.admin.pojo.criteria.SysLogQueryCriteria;
import com.live.admin.pojo.entity.SysLog;
import com.live.common.mybatis.base.service.SuperService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author puff
 */
public interface ISysLogService extends SuperService<SysLog> {


    Object findAllByPageable(String nickname, Pageable pageable);

    /**
     * 分页查询
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(SysLogQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     *
     * @param criteria 查询条件
     * @return /
     */
    List<SysLog> queryAll(SysLogQueryCriteria criteria);

    /**
     * 查询用户日志
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return -
     */
    Object queryAllByUser(SysLogQueryCriteria criteria, Pageable pageable);

    /**
     * 保存日志数据
     *
     * @param username  用户
     * @param ip        请求IP
     * @param joinPoint /
     * @param log       日志实体
     */
    @Async
    void save(String username, String ip, ProceedingJoinPoint joinPoint, SysLog log, Long uid);

    /**
     * 查询异常详情
     *
     * @param id 日志ID
     * @return Object
     */
    Object findByErrDetail(Long id);


    /**
     * 删除所有错误日志
     */
    void delAllByError();

    /**
     * 删除所有INFO日志
     */
    void delAllByInfo();
}
