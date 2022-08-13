package com.indo.job.executor.jobhandler;

import com.indo.common.utils.DateUtils;
import com.indo.core.service.game.s128.IS128TaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Slf4j
public class GameS128DjJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IS128TaskService is128TaskService;


    @XxlJob("gameS128DjJob")
    private void messageJob() {
        logger.info("is128SettleTask斗鸡定时拉取结算任务开始时间 Date:{}", DateUtils.format(new Date(),DateUtils.newFormat));
        is128TaskService.is128SettleTask();
        logger.info("is128SettleTask斗鸡定时拉取结算任务结束时间 Date:{}", DateUtils.format(new Date(),DateUtils.newFormat));
    }

}
