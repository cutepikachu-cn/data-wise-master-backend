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
    ASCENDING("升序", "ASCENDING"),
    DESCENDING("降序", "DESCENDING"),
    UNSORTED("不排序", "UNSORTED");

    private final String text;
    @EnumValue
    @JsonValue
    private final String value;
}
