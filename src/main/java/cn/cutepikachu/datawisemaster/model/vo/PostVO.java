package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 帖子 VO
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 12:00:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostVO extends BaseVO<Post, PostVO> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("点赞数")
    private Integer thumbNum;
    @ApiModelProperty("收藏数")
    private Integer favourNum;
    @ApiModelProperty("创建用户 id")
    private Long userId;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("标签列表")
    private List<String> tagList;
    @ApiModelProperty("创建人信息")
    private UserVO user;
    @ApiModelProperty("是否已点赞")
    private Boolean hasThumb;
    @ApiModelProperty("是否已收藏")
    private Boolean hasFavour;

    @Override
    public Post toEntity(Class<Post> postClass) {
        Post entity = super.toEntity(postClass);
        String tagsStr = JSONUtil.toJsonStr(this.getTagList());
        entity.setTags(tagsStr);
        return entity;
    }
}
