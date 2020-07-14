package com.zbb.api.web;

import com.zbb.bean.Result;
import com.zbb.entity.BhUserActivity;
import com.zbb.service.ding.BhUserActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunflower
 */
@Api("活动管理")
@Controller
@RequestMapping("/ding/activity")
public class BhUserActivityController {

    @Resource
    private BhUserActivityService bhUserActivityService;

    @RequestMapping(value = "getBhUserActivityListByUserId",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取用户的进行中的活动的排序", httpMethod = "POST")
    public String getBhUserActivityListByUserId(Long userId){
        List<BhUserActivity> list = bhUserActivityService.getBhUserActivityListByUserId(userId,null);
        return Result.succResult(list);
    }

    @RequestMapping(value = "deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量删除配置（无法删除已启用状态）", httpMethod = "POST")
    public String deleteBatch(@RequestBody BhUserActivity bhUserActivity){
        bhUserActivityService.updateBhUserActivity(bhUserActivity);
        return Result.succResult("OK");
    }

}
