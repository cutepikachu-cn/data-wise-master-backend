package cn.cutepikachu.datawisemaster.model.enums;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public interface BaseEnum<T extends Serializable> {
    /**
     * @return 获取枚举值描述
     */
    String getText();

    /**
     * @return 获取值
     */
    T getValue();

    /**
     * 获取枚举类的所有枚举值
     *
     * @param enumClass 枚举类
     * @param <E>       枚举类型
     * @param <T>       值类型
     * @return 所有枚举值
     */
    static <E extends Enum<E> & BaseEnum<T>, T extends Serializable> List<T> getAllValues(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(BaseEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举类值
     *
     * @param enumClass 枚举类
     * @param value     值
     * @param <E>       枚举类型
     * @param <T>       值类型
     * @return 枚举类
     */
    static <E extends Enum<E> & BaseEnum<T>, T extends Serializable> E getEnumByValue(Class<E> enumClass, T value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
