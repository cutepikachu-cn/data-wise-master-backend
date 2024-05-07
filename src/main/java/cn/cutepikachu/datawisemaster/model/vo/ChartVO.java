package cn.cutepikachu.datawisemaster.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.io.Serial;

/**
 * <p>
 * 图表信息表 VO
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChartVO extends BaseVO<Chart, ChartVO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 分析目标
     */
    private String goal;
    /**
     * 图表名称
     */
    private String name;
    /**
     * 原始数据
     */
    private String data;
    /**
     * 图表类型
     */
    private String chartType;
    /**
     * AI分析生成的图表数据
     */
    private String genChart;
    /**
     * AI分析的结论
     */
    private String genResult;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
