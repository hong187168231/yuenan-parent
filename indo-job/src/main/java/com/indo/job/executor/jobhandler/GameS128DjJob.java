package com.indo.job.executor.jobhandler;

import com.indo.core.service.game.s128.IS128TaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class GameS128DjJob {


    @Autowired
    private IS128TaskService is128TaskService;


    @XxlJob("gameS128DjJob")
    private void messageJob() {
    }

}
