package cn.cutepikachu.datawisemaster.model.dto.postfavour;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@ApiModel(description = "帖子收藏请求")
public class PostFavourAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("帖子id")
    @NotNull
    private Long postId;

}
