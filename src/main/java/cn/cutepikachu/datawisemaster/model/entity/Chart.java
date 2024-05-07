package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serial;

/**
 * <p>
 * 图表信息表
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "`chart`", autoResultMap = true)
@Schema(name = "Chart", description = "图表信息表")
public class Chart extends BaseEntity<Chart, ChartVO> implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户id")
    @TableField("`user_id`")
    private Long userId;

    @Schema(description = "分析目标")
    @TableField("`goal`")
    private String goal;

    @Schema(description = "图表名称")
    @TableField("`name`")
    private String name;

    @Schema(description = "原始数据")
    @TableField("`data`")
    private String data;

    @Schema(description = "图表类型")
    @TableField("`chart_type`")
    private String chartType;

    @Schema(description = "AI分析生成的图表数据")
    @TableField("`gen_chart`")
    private String genChart;

    @Schema(description = "AI分析的结论")
    @TableField("`gen_result`")
    private String genResult;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "是否删除")
    @TableField("`is_delete`")
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
