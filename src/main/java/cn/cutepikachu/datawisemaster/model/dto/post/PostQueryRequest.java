package cn.cutepikachu.datawisemaster.model.dto.post;


import cn.cutepikachu.datawisemaster.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PostQueryRequest", description = "帖子")
public class PostQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("搜索关键词")
    private String searchText;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("标签列表（至少一个）")
    @Size(min = 1)
    private List<String> tags;
    @ApiModelProperty("用户id")
    @Min(1)
    private Long userId;

}
