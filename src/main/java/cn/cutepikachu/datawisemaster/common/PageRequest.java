package cn.cutepikachu.datawisemaster.common;

import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页请求类
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "分页请求参数")
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("当前页码")
    @NotNull
    @Min(1)
    private Integer current = 1;
    @ApiModelProperty("每页条数")
    @NotNull
    @Range(min = 1, max = 20)
    private Integer pageSize = 5;
    @ApiModelProperty("排序字段")
    private String sortField;
    @ApiModelProperty("排序方式")
    private SortOrder sortOrder;
}
