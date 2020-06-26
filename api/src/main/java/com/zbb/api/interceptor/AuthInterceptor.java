package com.zbb.api.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sunflower
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (1 == 1) {
            return true;
        }
        /*boolean haveAnnotation = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (request.getServletPath().contains("swagger")) {
            return true;
        }
        if (haveAnnotation) {
            *//* 检测是否有 @IngoreAuth 注解**//*
            IngoreAuth ingoreAuth = ((HandlerMethod) handler).getMethodAnnotation(IngoreAuth.class);
            if (ingoreAuth != null) {
                *//*有的话，不需要校验权限*//*
                return true;
            }

            String uId = null;
            Enumeration<String> enumeration = request.getHeaders("uId");
            if (enumeration.hasMoreElements() && enumeration != null) {
                uId = enumeration.nextElement();
            }
            if (StringUtils.isBlank(uId)) {
                throw new BusinessException("uId不能为空");
            }
            String userToken = null;
            enumeration = request.getHeaders("userToken");
            if (enumeration != null && enumeration.hasMoreElements()) {
                userToken = enumeration.nextElement();
            }
            if (StringUtils.isBlank(userToken)) {
                throw new BusinessException("token不能为空");
            }
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(uId);
            if (null == sysUser) {
                throw new BusinessException("用户不存在");
            }
            if (!userToken.equals(sysUser.getUserToken())) {
                throw new BusinessException("token不正确");
            }
            if ("1".equals(uId)) {
                return true;
            }
            *//*获取用户角色对应的菜单权限*//*
            List<SysMenuVo> roleMenuList = sysRoleService.queryMenuByRoleId(sysUser.getRoleId(), sysUser.getCompanyId());
            *//*菜单为空直接返回没有权限*//*
            if (CollectionUtils.isEmpty(roleMenuList)) {
                throw new BusinessException("没有权限");
            }
            *//*请求的url*//*
            String requestUrl = request.getRequestURL().toString();

            for (SysMenuVo roleMenu : roleMenuList) {
                if (roleMenu.isHasChecked() && StringUtils.isNotEmpty(roleMenu.getMenuUrl()) && requestUrl.contains(roleMenu.getMenuUrl())) {
                    return true;
                }
            }
            throw new BusinessException("没有权限");
        }*/
        return true;
    }
}
