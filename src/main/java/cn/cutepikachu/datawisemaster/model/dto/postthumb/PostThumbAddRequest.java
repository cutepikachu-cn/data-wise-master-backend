package cn.cutepikachu.datawisemaster.model.dto.postthumb;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class PostThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long postId;

    @Serial
    private static final long serialVersionUID = 1L;
}
