package cn.cutepikachu.datawisemaster.model.dto.postfavour;


import cn.cutepikachu.datawisemaster.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 帖子收藏查询请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PostFavourQueryRequest", description = "帖子收藏查询请求")
public class PostFavourQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    @NotNull
    @Min(1)
    private Long userId;

}
