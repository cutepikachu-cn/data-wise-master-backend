package cn.cutepikachu.datawisemaster.model.dto.chart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "生成图表请求")
public class ChartGenRequest implements Serializable {
    @ApiModelProperty("分析目标")
    @NotEmpty
    @Length(max = 1024)
    private String goal;
    @ApiModelProperty("图表名称")
    @NotEmpty
    @Length(max = 128)
    private String name;
    @ApiModelProperty("图表类型")
    @NotEmpty
    @Length(max = 128)
    private String chartType;

    private static final long serialVersionUID = 1L;
}
