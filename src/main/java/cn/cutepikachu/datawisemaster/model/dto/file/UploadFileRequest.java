package cn.cutepikachu.datawisemaster.model.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UploadFileRequest implements Serializable {
    /**
     * 业务
     */
    @NotNull
    private String biz;

    @Serial
    private static final long serialVersionUID = 1L;

}
