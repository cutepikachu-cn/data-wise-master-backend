package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import cn.hutool.json.JSONUtil;
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
import java.util.List;

/**
 * <p>
 * 帖子
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
@TableName(value = "`post`", autoResultMap = true)
@ApiModel(value = "Post", description = "帖子")
public class Post extends BaseEntity<Post, PostVO> implements Serializable {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String TAGS = "tags";
    public static final String THUMB_NUM = "thumb_num";
    public static final String FAVOUR_NUM = "favour_num";
    public static final String USER_ID = "user_id";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
    public static final String IS_DELETE = "is_delete";
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("标题")
    @TableField("title")
    private String title;
    @ApiModelProperty("内容")
    @TableField("content")
    private String content;
    @ApiModelProperty("标签列表（json 数组）")
    @TableField("tags")
    private String tags;
    @ApiModelProperty("点赞数")
    @TableField("thumb_num")
    private Integer thumbNum;
    @ApiModelProperty("收藏数")
    @TableField("favour_num")
    private Integer favourNum;
    @ApiModelProperty("创建用户 id")
    @TableField("user_id")
    private Long userId;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @ApiModelProperty("是否删除")
    @TableField("is_delete")
    @TableLogic
    private Byte isDelete;

    @Override
    public PostVO toVO(Class<PostVO> postVOClass) {
        PostVO vo = super.toVO(postVOClass);
        List<String> tagList = JSONUtil.toList(this.getTags(), String.class);
        vo.setTagList(tagList);
        return vo;
    }
}
