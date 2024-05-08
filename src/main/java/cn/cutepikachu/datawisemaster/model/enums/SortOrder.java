package cn.cutepikachu.datawisemaster.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum SortOrder implements BaseEnum<String> {
    SORT_ORDER_ASC("升序", "ascend"),
    SORT_ORDER_DESC("降序", "descend"),
    UNSORTED("不排序", "unsorted");

    private final String text;
    @EnumValue
    @JsonValue
    private final String value;
}
