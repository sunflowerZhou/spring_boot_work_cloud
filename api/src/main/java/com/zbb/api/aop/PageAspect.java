package com.zbb.api.aop;

import com.github.pagehelper.PageHelper;
import com.zbb.bean.PageDataBo;
import com.zbb.bean.Result;
import com.zbb.common.util.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:
 * 分页拦截
 *
 * @author sunflower
 * @date 2019/9/26
 * ————————————————————————
 */
@Aspect
@Component
public class PageAspect {

    @Resource
    private HttpServletRequest request;

    @Pointcut(value = "@annotation(com.zbb.api.aop.AddPage)")
    public void cutService() {
    }

    @Around("cutService()")
    public Object addPage(ProceedingJoinPoint point) throws Throwable {

        String handResult = pre(point);
        if (Constant.ERROR.equals(handResult)) {
            return Result.failResult("请传递正确的分页参数");
        }
        return point.proceed();
    }

    private String pre(ProceedingJoinPoint point) {
        String currentPage = request.getParameter("currentPage");
        String showCount = request.getParameter("showCount");
        if (StringUtils.isEmpty(currentPage) || StringUtils.isEmpty(showCount)) {
            //json调用方式，分页对象请继承PageDataBo对象
            Object[] args = point.getArgs();
            for (Object o : args) {
                if (o instanceof PageDataBo) {
                    PageDataBo pageDataBo = (PageDataBo) o;
                    currentPage = pageDataBo.getCurrentPage();
                    showCount = pageDataBo.getShowCount();
                }
            }
            if (StringUtils.isEmpty(currentPage) || StringUtils.isEmpty(showCount)) {
                return "error";
            }
        }
        PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(showCount));
        return Constant.SUCCESS;
    }
}
