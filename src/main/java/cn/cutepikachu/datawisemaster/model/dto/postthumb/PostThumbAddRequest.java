package cn.cutepikachu.datawisemaster.model.dto.postthumb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "帖子点赞请求")
public class PostThumbAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("帖子id")
    @NotNull
    @Min(1)
    private Long postId;

}
