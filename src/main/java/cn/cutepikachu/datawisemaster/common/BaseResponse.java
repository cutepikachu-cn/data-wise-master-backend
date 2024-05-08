package cn.cutepikachu.datawisemaster.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础响应类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@AllArgsConstructor
@ApiModel(description = "响应对象")
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("响应信息")
    private String message;

    /**
     * 响应，响应码
     *
     * @param responseCode 响应码
     */
    public BaseResponse(ResponseCode responseCode) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, responseCode.getMessage());
    }

    /**
     * 响应，带数据
     *
     * @param responseCode 响应码
     * @param data         数据
     */
    public BaseResponse(ResponseCode responseCode, T data) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, responseCode.getMessage());
    }

    /**
     * 响应，带信息
     *
     * @param responseCode 响应码
     * @param message      信息
     */
    public BaseResponse(ResponseCode responseCode, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, message);
    }

    /**
     * 响应，带信息和数据
     *
     * @param responseCode 响应码
     * @param data         数据
     * @param message      信息
     */
    public BaseResponse(ResponseCode responseCode, T data, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, message);
    }

    /**
     * 响应，带自定响应码和信息
     *
     * @param responseCode 响应码
     * @param message      信息
     */
    public BaseResponse(Integer responseCode, String message) {
        this(responseCode == 0, responseCode, null, message);
    }

}
