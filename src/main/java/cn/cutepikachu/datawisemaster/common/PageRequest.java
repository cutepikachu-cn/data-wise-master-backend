package cn.cutepikachu.datawisemaster.common;

import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @NotNull
    @Min(1)
    private int current = 1;

    /**
     * 页面大小
     */
    @NotNull
    @Range(min = 1, max = 20)
    private int pageSize = 5;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SortOrder.SORT_ORDER_ASC.getValue();

    @AssertTrue
    boolean isValidSortOrder() {
        if (StrUtil.isBlank(sortField)) {
            return true;
        }
        SortOrder sortOrderEnum = SortOrder.getEnumByValue(sortOrder);
        return sortOrderEnum != null;
    }
}
