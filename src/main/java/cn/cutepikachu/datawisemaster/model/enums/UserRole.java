package cn.cutepikachu.datawisemaster.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum UserRole {
    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("封禁", "ban");

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
    public static UserRole getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRole anEnum : UserRole.values()) {
            if (anEnum.value.equals(value.toLowerCase())) {
                return anEnum;
            }
        }
        return null;
    }
}
