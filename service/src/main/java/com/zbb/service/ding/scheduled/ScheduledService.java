package com.zbb.service.ding.scheduled;

import com.zbb.service.ding.BhInvitationUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author sunflower
 *
 * 定时任务
 */
@Slf4j
@Component
public class ScheduledService {

    @Resource
    private BhInvitationUserService bhInvitationUserService;

    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(cron = "0 0 */1 * * ?")
    public void scheduled(){
        bhInvitationUserService.inviteInfo();
    }

}
