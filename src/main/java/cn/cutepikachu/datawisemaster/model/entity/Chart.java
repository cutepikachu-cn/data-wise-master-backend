package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.enums.GenStatus;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 图表信息表
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-08 17:33:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "`chart`", autoResultMap = true)
@ApiModel(value = "Chart", description = "图表信息表")
public class Chart extends BaseEntity<Chart, ChartVO> implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("分析目标")
    @TableField("goal")
    private String goal;

    @ApiModelProperty("图表名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("图表类型")
    @TableField("chart_type")
    private String chartType;

    @ApiModelProperty("分析状态")
    @TableField("gen_status")
    private GenStatus genStatus;

    @ApiModelProperty("AI分析生成的图表数据")
    @TableField("gen_chart")
    private String genChart;

    @ApiModelProperty("AI分析的结论")
    @TableField("gen_result")
    private String genResult;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除")
    @TableField("is_delete")
    @TableLogic
    private Byte isDelete;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String GOAL = "goal";

    public static final String NAME = "name";

    public static final String DATA = "data";

    public static final String CHART_TYPE = "chart_type";

    public static final String GEN_CHART = "gen_chart";

    public static final String GEN_RESULT = "gen_result";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";
}
