package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.ResponseCode;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @return
     */
    public static BaseResponse<?> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS);
    }

    /**
     * 成功，带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data);
    }

    /**
     * 成功，带信息
     *
     * @return
     */
    public static BaseResponse<?> success(String message) {
        return new BaseResponse<>(ResponseCode.SUCCESS, message);
    }

    /**
     * 成功，带数据和信息
     *
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data, message);
    }

    /**
     * 失败
     *
     * @param responseCode
     * @return
     */
    public static BaseResponse<?> error(ResponseCode responseCode) {
        return new BaseResponse<>(responseCode);
    }

    /**
     * 失败，带信息
     *
     * @param responseCode
     * @param message
     * @return
     */
    public static BaseResponse<?> error(ResponseCode responseCode, String message) {
        return new BaseResponse<>(responseCode, message);
    }

    /**
     * 失败，带自定响应码和信息
     *
     * @param responseCode
     * @param message
     * @return
     */
    public static BaseResponse<Boolean> error(Integer responseCode, String message) {
        return new BaseResponse<>(responseCode, message);
    }

}
