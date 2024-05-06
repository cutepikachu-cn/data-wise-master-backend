package cn.cutepikachu.datawisemaster.model.dto.post;


import cn.cutepikachu.datawisemaster.common.PageRequest;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    @Size(min = 1)
    private List<String> tags;

    /**
     * 至少有一个标签
     */
    @Size(min = 1)
    private List<String> orTags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 收藏用户 id
     */
    private Long favourUserId;

    @Serial
    private static final long serialVersionUID = 1L;
}
