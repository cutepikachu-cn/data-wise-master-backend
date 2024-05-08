package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
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
     * 帖子收藏/取消收藏
     *
     * @param postId 帖子id
     * @return 释放收藏/取消是否成功
     */
    boolean doPostFavour(Long postId);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param current  当前页数
     * @param pageSize 每页大小
     * @param userId   用户id
     * @return 用户收藏帖子分页对象
     */
    Page<Post> pageFavourPost(Integer current, Integer pageSize,
                              Long userId);

    /**
     * 帖子收藏（内部服务）
     *
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否收藏/取消收藏成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean doPostFavourInner(Long userId, Long postId);
}
