package cn.cutepikachu.datawisemaster.model.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class PostUpdateRequest implements Serializable {

    @NotNull
    private Long id;

    @NotBlank
    @Length(max = 80)
    private String title;

    @NotBlank
    @Length(max = 8192)
    private String content;

    @NotNull
    @Size(min = 1)
    private List<String> tags;

    @Serial
    private static final long serialVersionUID = 1L;
}
