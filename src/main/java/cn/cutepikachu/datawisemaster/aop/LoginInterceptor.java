package cn.cutepikachu.datawisemaster.aop;

import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录拦截
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Aspect
@Order(1000)
@Component
public class LoginInterceptor {

    @Resource
    private IUserService userService;

    @Around("execution(* cn.cutepikachu.datawisemaster.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 获取登录用户
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUserPermitNull(request);
        // 存储用户信息
        UserHolder.saveUser(loginUser);
        Object result = point.proceed();
        // 移除用户信息
        UserHolder.removeUser();
        return result;
    }

}
