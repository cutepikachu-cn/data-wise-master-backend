package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.entity.Chart;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图表信息表 VO
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-08 17:33:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChartVO extends BaseVO<Chart, ChartVO> implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("分析目标")
    private String goal;
    @ApiModelProperty("图表名称")
    private String name;
    @ApiModelProperty("原始数据")
    private List<Map<String, String>> data;
    @ApiModelProperty("图表类型")
    private String chartType;
    @ApiModelProperty("AI分析生成的图表数据")
    private String genChart;
    @ApiModelProperty("AI分析的结论")
    private String genResult;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("是否删除")
    private Byte isDelete;
}
