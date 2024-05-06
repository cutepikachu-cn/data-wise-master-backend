package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;

/**
 * 抛异常工具类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ResponseCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }


    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ResponseCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, Integer errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

}
