package cn.cutepikachu.datawisemaster.model.enums;

import cn.hutool.core.util.ObjUtil;
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
public enum FileUploadBizEnum {

    USER_AVATAR("用户头像", "user_avatar"),
    COMMODITY_IMG("商品图片", "commodity_img");

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
    public static FileUploadBizEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (FileUploadBizEnum anEnum : FileUploadBizEnum.values()) {
            if (anEnum.value.equals(value.toLowerCase())) {
                return anEnum;
            }
        }
        return null;
    }

}
