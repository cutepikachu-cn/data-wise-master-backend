package cn.cutepikachu.datawisemaster.model.dto.postfavour;


import cn.cutepikachu.datawisemaster.common.PageRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 帖子收藏查询请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户 id
     */
    @NotNull
    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;
}
