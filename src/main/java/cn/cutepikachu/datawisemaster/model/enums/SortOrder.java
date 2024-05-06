package cn.cutepikachu.datawisemaster.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum SortOrder {
    SORT_ORDER_ASC("升序", "ascend"),
    SORT_ORDER_DESC("降序", "descend");

    private final String text;
    private final String value;

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SortOrder getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (SortOrder anEnum : SortOrder.values()) {
            if (anEnum.value.equals(value.toLowerCase())) {
                return anEnum;
            }
        }
        return null;
    }
}
