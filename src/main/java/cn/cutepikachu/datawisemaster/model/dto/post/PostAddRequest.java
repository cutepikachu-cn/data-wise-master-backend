package cn.cutepikachu.datawisemaster.model.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "帖子添加请求")
public class PostAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("标题")
    @NotBlank
    @Length(max = 80)
    private String title;
    @ApiModelProperty("内容")
    @NotBlank
    @Length(max = 8192)
    private String content;
    @ApiModelProperty("标签列表")
    @NotNull
    @Size(min = 1)
    private List<String> tags;

}
