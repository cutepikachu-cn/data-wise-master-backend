package cn.cutepikachu.datawisemaster.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(0, "成功"),

    // 客户端异常
    PARAMS_ERROR(41500, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40300, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    METHOD_NOT_ALLOW(40500, "请求方法错误"),

    // 服务端异常
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;
}
