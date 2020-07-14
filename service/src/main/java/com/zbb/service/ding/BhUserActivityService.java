package com.zbb.service.ding;

import com.google.common.collect.Lists;
import com.zbb.dao.mapper.BhUserActivityMapper;
import com.zbb.entity.BhUserActivity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BhUserActivityService {

    @Resource
    private BhUserActivityMapper bhUserActivityMapper;


    /**
     * 获取用户的进行中的活动的排序
     *
     * @param userId 用户id
     * @return List<BhUserActivity>
     */
    public List<BhUserActivity> getBhUserActivityListByUserId(Long userId,String name){
        Example example = new Example(BhUserActivity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        if (StringUtils.isNotEmpty(name)){
            criteria.andEqualTo("activityName",name);
        }
        example.setOrderByClause("last_time desc");
        List<BhUserActivity> activities = bhUserActivityMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(activities)) {
            activities = Lists.newArrayList();
        }
        return activities;
    }

    /**
     * 编辑
     *
     * @param bhUserActivity 活动
     */
    public void updateBhUserActivity(BhUserActivity bhUserActivity){
        bhUserActivityMapper.updateByPrimaryKeySelective(bhUserActivity);
    }

    /**
     * insert
     *
     * @param bhUserActivity bhUserActivity
     */
    public void insertBhUserActivity(BhUserActivity bhUserActivity){
        bhUserActivityMapper.insertSelective(bhUserActivity);
    }
}
