package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.dto.post.PostQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 帖子 服务类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
public interface IPostService extends IService<Post> {
    /**
     * 获取查询条件
     *
     * @param postQueryRequest 查询请求参数
     */
    LambdaQueryWrapper<Post> getLambdaQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 获取封装
     *
     * @param post 对象
     * @return 封装对象
     */
    PostVO getPostVO(Post post);

    /**
     * 分页获取封装
     *
     * @param postPage 分页对象
     * @return 分页封装都西昂
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage);
}
