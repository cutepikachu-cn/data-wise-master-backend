package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.mapper.PostFavourMapper;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.service.IPostFavourService;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private IUserService userService;

    @Override
    public boolean doPostFavour(Long postId) {
        // 判断是否存在
        Post post = postService.getById(postId);
        ThrowUtil.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR);
        // 是否已帖子收藏
        User loginUser = userService.getLoginUser();
        Long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        IPostFavourService postFavourService = (IPostFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postFavourService.doPostFavourInner(userId, postId);
        }
    }

    @Override
    public Page<Post> pageFavourPost(Integer current, Integer pageSize, Long userId) {
        return baseMapper.listFavourPostByPage(new Page<>(current, pageSize), userId);
    }

    @Override
    public boolean doPostFavourInner(Long userId, Long postId) {
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
                return result;
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
                return result;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        }
    }

}




