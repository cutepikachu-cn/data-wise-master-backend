package cn.cutepikachu.datawisemaster.exception;


import cn.cutepikachu.datawisemaster.common.ResponseCode;
import lombok.Getter;

/**
 * 自定义业务异常类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 异常码
     */
    private final int code;

    public BusinessException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ResponseCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
