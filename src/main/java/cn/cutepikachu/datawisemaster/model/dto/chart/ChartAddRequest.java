package cn.cutepikachu.datawisemaster.model.dto.chart;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class ChartAddRequest implements Serializable {
    /**
     * 分析目标
     */
    @NotEmpty
    @Length(max = 1024)
    private String goal;
    /**
     * 图表名称
     */
    @NotEmpty
    @Length(max = 128)
    private String name;
    /**
     * 原始数据
     */
    @NotEmpty
    private String data;
    /**
     * 图表类型
     */
    @NotEmpty
    @Length(max = 128)
    private String chartType;

    @Serial
    private static final long serialVersionUID = 1L;
}
