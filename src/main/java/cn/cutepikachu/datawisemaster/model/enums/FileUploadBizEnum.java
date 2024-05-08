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
public enum FileUploadBizEnum implements BaseEnum<String> {
    USER_AVATAR("用户头像", "USER_AVATAR");

    private final String text;
    @EnumValue
    @JsonValue
    private final String value;
}
