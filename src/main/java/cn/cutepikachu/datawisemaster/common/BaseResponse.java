package cn.cutepikachu.datawisemaster.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private boolean success;
    private int code;
    private T data;
    private String message;

    /**
     * 响应，响应码
     *
     * @param responseCode
     */
    public BaseResponse(ResponseCode responseCode) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, responseCode.getMessage());
    }

    /**
     * 响应，带数据
     *
     * @param responseCode
     * @param data
     */
    public BaseResponse(ResponseCode responseCode, T data) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, responseCode.getMessage());
    }

    /**
     * 响应，带信息
     *
     * @param responseCode
     * @param message
     */
    public BaseResponse(ResponseCode responseCode, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, message);
    }

    /**
     * 响应，带信息和数据
     *
     * @param responseCode
     * @param data
     * @param message
     */
    public BaseResponse(ResponseCode responseCode, T data, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, message);
    }

    /**
     * 响应，带自定响应码和信息
     *
     * @param responseCode
     * @param message
     */
    public BaseResponse(Integer responseCode, String message) {
        this(responseCode.equals(0), responseCode, null, message);
    }
}
