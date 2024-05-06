package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.entity.PostThumb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 帖子点赞 VO
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 12:00:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostThumbVO extends BaseVO<PostThumb, PostThumbVO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 帖子 id
     */
    private Long postId;
    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

