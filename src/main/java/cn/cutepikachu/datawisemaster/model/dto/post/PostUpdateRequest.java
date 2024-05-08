package cn.cutepikachu.datawisemaster.model.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "更新帖子请求")
public class PostUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @NotNull
    @Min(1)
    private Long id;
    @ApiModelProperty("标题")
    @Length(min = 4, max = 80)
    private String title;
    @ApiModelProperty("内容")
    @Length(min = 4, max = 8192)
    private String content;
    @ApiModelProperty("标签列表")
    @Size(min = 1)
    private List<String> tags;

}
