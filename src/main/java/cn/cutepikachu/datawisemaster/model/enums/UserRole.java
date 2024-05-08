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
public enum UserRole implements BaseEnum<String> {
    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("封禁", "ban");

    private final String text;
    @EnumValue
    @JsonValue
    private final String value;
}
