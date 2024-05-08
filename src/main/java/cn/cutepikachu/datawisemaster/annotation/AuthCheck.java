package cn.cutepikachu.datawisemaster.annotation;

import cn.cutepikachu.datawisemaster.model.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某些角色
     *
     * @return 角色数组
     */
    UserRole[] mustRole() default UserRole.USER;

}
