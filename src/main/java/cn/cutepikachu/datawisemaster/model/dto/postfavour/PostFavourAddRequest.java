package cn.cutepikachu.datawisemaster.model.dto.postfavour;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class PostFavourAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    @NotNull
    private Long postId;

    @Serial
    private static final long serialVersionUID = 1L;
}
