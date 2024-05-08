package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.vo.PostFavourVO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 帖子收藏
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-08 00:15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "`post_favour`", autoResultMap = true)
@ApiModel(value = "PostFavour", description = "帖子收藏")
public class PostFavour extends BaseEntity<PostFavour, PostFavourVO> implements Serializable {

    public static final String ID = "id";
    public static final String POST_ID = "post_id";
    public static final String USER_ID = "user_id";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("帖子 id")
    @TableField("post_id")
    private Long postId;
    @ApiModelProperty("创建用户 id")
    @TableField("user_id")
    private Long userId;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
