package cn.cutepikachu.datawisemaster.model.dto.chart;

import cn.cutepikachu.datawisemaster.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest  extends PageRequest implements Serializable {
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
     * 图表类型
     */
    private String chartType;

    @Serial
    private static final long serialVersionUID = 1L;
}
