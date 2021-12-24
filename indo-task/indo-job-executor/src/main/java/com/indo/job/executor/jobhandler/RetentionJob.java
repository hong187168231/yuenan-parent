package com.indo.job.executor.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RetentionJob {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("RetentionJob")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello RetentionJob.");

        System.err.println("--------------------");
        // default success
    }
}
