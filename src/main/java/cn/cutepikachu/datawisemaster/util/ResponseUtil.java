package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.ResponseCode;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ResponseUtil {

    /**
     * 成功
     *
     * @return 响应对象
     */
    public static BaseResponse<?> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS);
    }

    /**
     * 成功，带数据
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data);
    }

    /**
     * 成功，带信息
     *
     * @return 响应对象
     */
    public static BaseResponse<?> success(String message) {
        return new BaseResponse<>(ResponseCode.SUCCESS, message);
    }

    /**
     * 成功，带数据和信息
     *
     * @param message 信息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应对象
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data, message);
    }

    /**
     * 失败
     *
     * @param responseCode 响应码
     * @return 响应对象
     */
    public static BaseResponse<?> error(ResponseCode responseCode) {
        return new BaseResponse<>(responseCode);
    }

    /**
     * 失败，带信息
     *
     * @param responseCode 响应码
     * @param message      信息
     * @return 响应对象
     */
    public static BaseResponse<?> error(ResponseCode responseCode, String message) {
        return new BaseResponse<>(responseCode, message);
    }

    /**
     * 失败，带自定响应码和信息
     *
     * @param responseCode 响应码
     * @param message      信息
     * @return 响应对象
     */
    public static BaseResponse<?> error(Integer responseCode, String message) {
        return new BaseResponse<>(responseCode, message);
    }

}
