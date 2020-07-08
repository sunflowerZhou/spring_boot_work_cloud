package com.zbb.api.web;

import com.zbb.bean.Result;
import com.zbb.bo.CodeManagementBo;
import com.zbb.entity.CodeManagement;
import com.zbb.service.ding.AutoGroupBuildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sunflower
 */
@Api(value = "自动建群")
@Controller
@RequestMapping("/ding/group")
public class AutoGroupBuildController {

    @Resource
    private AutoGroupBuildService autoGroupBuildService;

    @Value("${address}")
    public String address;

    @RequestMapping(value = "edit",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "编辑模版(返回二维码路径)", httpMethod = "POST")
    public String edit(@RequestBody CodeManagementBo codeManagementBo) throws Exception {
        if (codeManagementBo.getId() == null){
            codeManagementBo.setCodeUrl(address);
        }
        String edit = autoGroupBuildService.edit(codeManagementBo);
        return Result.succResult(edit);
    }

    @RequestMapping(value = "scanCodeJump",method = RequestMethod.GET)
    @ApiOperation(value = "扫码跳转", httpMethod = "GET")
    public void scanCodeJump(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String auto = autoGroupBuildService.joinGroupAuto(id);
        response.getWriter().write("1000"+id);
    }

    @RequestMapping(value = "joinGroupAuto",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "加入群聊（自动建群）", httpMethod = "POST")
    public String joinGroupAuto(@RequestBody CodeManagementBo codeManagementBo) throws Exception {

        return Result.succResult("ok");
    }

    @RequestMapping(value = "getCodeManagementList",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "根据用户ID获取活码配置", httpMethod = "POST")
    public String getCodeManagementList(String userId){
        List<CodeManagement> list = autoGroupBuildService.getCodeManagementList(userId);
        return Result.succResult(list);
    }

    @RequestMapping(value = "deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量删除配置（无法删除已启用状态）", httpMethod = "POST")
    public String deleteBatch(List<String> ids){
        autoGroupBuildService.deleteBatch(ids);
        return Result.succResult("OK");
    }

}
