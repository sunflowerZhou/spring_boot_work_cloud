package com.zbb.api.web;

import com.github.pagehelper.PageInfo;
import com.zbb.bean.Result;
import com.zbb.bo.CodeManagementBo;
import com.zbb.service.ding.AutoGroupBuildService;
import com.zbb.vo.CodeManagementVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

/**
 * @author sunflower
 */
@Api("自动建群")
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
    public String edit(@RequestBody CodeManagementBo codeManagementBo) throws Exception{
        if (codeManagementBo.getId() == null){
            codeManagementBo.setCodeUrl(address);
        }
        return autoGroupBuildService.edit(codeManagementBo);
    }


    @RequestMapping(value = "scanCodeJump",method = RequestMethod.GET)
    @ApiOperation(value = "扫码跳转", httpMethod = "GET")
    public void scanCodeJump(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String auto = autoGroupBuildService.joinGroupAuto(id,null);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("<a href='https://www.baidu.com'>对不起！此二维码已经失效。</a>");
        response.getWriter().write("<img src='http://htt.nat100.top/img/qr_code/a345a140320449e8b9206b62059a7d86.png'/>");
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "string", required = true, value = "每页显示数", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "page", dataType = "string", required = true, value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "codeName", dataType = "string", value = "活码名称", defaultValue = "1")
    })
    public String getCodeManagementList(String userId,String codeName,String pageSize,String page){
        PageInfo<CodeManagementVo> list = autoGroupBuildService.getCodeManagementList(userId, codeName, Integer.parseInt(page),Integer.parseInt(pageSize));
        return Result.succResult(list);
    }

    @RequestMapping(value = "deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量删除配置（无法删除已启用状态）", httpMethod = "POST")
    public String deleteBatch(String ids){
        String[] split = ids.split(",");
        autoGroupBuildService.deleteBatch(split);
        return Result.succResult("OK");
    }

}
