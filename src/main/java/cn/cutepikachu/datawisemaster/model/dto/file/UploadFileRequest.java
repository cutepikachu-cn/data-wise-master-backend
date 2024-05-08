package cn.cutepikachu.datawisemaster.model.dto.file;

import cn.cutepikachu.datawisemaster.model.enums.FileUploadBizEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "文件上传请求")
public class UploadFileRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("文件上传业务类型")
    @NotNull
    private FileUploadBizEnum biz;
}
