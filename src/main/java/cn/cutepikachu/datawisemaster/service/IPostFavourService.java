package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
import cn.cutepikachu.datawisemaster.model.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 帖子收藏 服务类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
public interface IPostFavourService extends IService<PostFavour> {
    /**
     * 帖子收藏
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostFavour(long postId, User loginUser);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param userId
     * @return
     */
    Page<Post> pageFavourPost(long current, long pageSize,
                              long userId);

    /**
     * 帖子收藏（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    int doPostFavourInner(long userId, long postId);
}
