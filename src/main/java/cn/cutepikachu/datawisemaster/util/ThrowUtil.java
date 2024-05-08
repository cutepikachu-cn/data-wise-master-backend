package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;

/**
 * 抛异常工具类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ThrowUtil {

    /**
     * 条件成立则抛异常
     *
     * @param condition        抛出条件
     * @param runtimeException 异常对象
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     */
    public static void throwIf(boolean condition, ResponseCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }


    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ResponseCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, Integer errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

}
