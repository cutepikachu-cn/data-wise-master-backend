package cn.cutepikachu.datawisemaster.aop;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.util.ArrayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private IUserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 获取所需的角色
        UserRole[] mustRoles = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户的权限
        User loginUser = userService.getLoginUser(request);
        // 必须有该权限才通过
        UserRole userRole = UserRole.getEnumByValue(loginUser.getUserRole());
        // 如果被封号，直接拒绝
        ThrowUtils.throwIf(userRole == UserRole.BAN, ResponseCode.NO_AUTH_ERROR);
        // 鉴权
        ThrowUtils.throwIf(!ArrayUtil.contains(mustRoles, userRole), ResponseCode.NO_AUTH_ERROR);

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
