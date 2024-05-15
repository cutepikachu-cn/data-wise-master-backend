package cn.cutepikachu.datawisemaster.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-15 19:17:00
 */
@Getter
@AllArgsConstructor
public enum GenStatus implements BaseEnum<String> {
    WAIT("排队等待中", "WAIT"),
    RUNNING("正在分析", "RUNNING"),
    SUCCEED("分析成功", "SUCCEED"),
    ERROR("分析错误", "ERROR");

    private final String text;
    @EnumValue
    @JsonValue
    private final String value;
}
