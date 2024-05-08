package cn.cutepikachu.datawisemaster.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 删除请求参数类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "删除请求参数")
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @NotNull
    @Min(1)
    private Long id;

}
