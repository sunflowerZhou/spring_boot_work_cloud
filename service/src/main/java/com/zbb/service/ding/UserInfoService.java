package com.zbb.service.ding;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户相关信息
 *
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoService {

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

}
