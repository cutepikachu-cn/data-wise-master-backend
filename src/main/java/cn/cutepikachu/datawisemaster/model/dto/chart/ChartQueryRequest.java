package cn.cutepikachu.datawisemaster.model.dto.chart;

import cn.cutepikachu.datawisemaster.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "图表查询请求")
public class ChartQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @Min(1)
    private Long id;
    @ApiModelProperty("用户id")
    @Min(1)
    private Long userId;
    @ApiModelProperty("分析目标")
    @Length(min = 1)
    private String goal;
    @ApiModelProperty("图表名称")
    @Length(min = 1, max = 128)
    private String name;
    @ApiModelProperty("图表类型")
    @Length(min = 1, max = 128)
    private String chartType;
}
