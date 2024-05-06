package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.mapper.PostFavourMapper;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.service.IPostFavourService;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帖子收藏 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour> implements IPostFavourService {

    @Resource
    private IPostService postService;

    /**
     * 帖子收藏
     *
     * @param postId
     * @param loginUser
     * @return
     */
    @Override
    public int doPostFavour(long postId, User loginUser) {
        // 判断是否存在
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR);
        // 是否已帖子收藏
        long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        IPostFavourService postFavourService = (IPostFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postFavourService.doPostFavourInner(userId, postId);
        }
    }

    @Override
    public Page<Post> pageFavourPost(long current, long pageSize, long userId) {
        return baseMapper.listFavourPostByPage(new Page<>(current, pageSize), userId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    public int doPostFavourInner(long userId, long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setUserId(userId);
        postFavour.setPostId(postId);
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(postFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            result = this.remove(postFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .gt(Post::getFavourNum, 0)
                        .setSql("favour_num = favour_num - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        } else {
            // 帖子未收藏
            result = this.save(postFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .setSql("favour_num = favour_num + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        }
    }
}




