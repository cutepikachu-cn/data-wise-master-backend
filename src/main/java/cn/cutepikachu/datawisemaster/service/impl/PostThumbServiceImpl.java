package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.mapper.PostThumbMapper;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostThumb;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IPostThumbService;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帖子点赞 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb> implements IPostThumbService {

    @Resource
    private IPostService postService;

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    @Override
    public int doPostThumb(long postId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR);
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        IPostThumbService postThumbService = (IPostThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postThumbService.doPostThumbInner(userId, postId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    public int doPostThumbInner(long userId, long postId) {
        PostThumb postThumb = new PostThumb();
        postThumb.setUserId(userId);
        postThumb.setPostId(postId);
        QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>(postThumb);
        PostThumb oldPostThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldPostThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .gt(Post::getThumbNum, 0)
                        .setSql("thumb_num = thumb_num - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(postThumb);
            if (result) {
                // 点赞数 + 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .setSql("thumb_num = thumb_num + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        }
    }
}




