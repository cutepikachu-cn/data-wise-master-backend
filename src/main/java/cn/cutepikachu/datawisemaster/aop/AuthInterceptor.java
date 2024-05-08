package cn.cutepikachu.datawisemaster.aop;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.util.ArrayUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 权限拦截
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Aspect
@Order(1001)
@Component
public class AuthInterceptor {

    @Resource
    private IUserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限注解
     * @return Object
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 获取所需的角色
        UserRole[] mustRoles = authCheck.mustRole();
        // 获取当前登录用户的角色
        UserRole userRole = userService.getLoginUser().getUserRole();
        // 如果被封号，直接拒绝
        ThrowUtil.throwIf(userRole == UserRole.BAN, ResponseCode.NO_AUTH_ERROR);
        // 鉴权
        ThrowUtil.throwIf(!ArrayUtil.contains(mustRoles, userRole), ResponseCode.NO_AUTH_ERROR);

        // 通过权限校验，放行
        return joinPoint.proceed();
    }

}
