package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.entity.PostThumb;
import cn.cutepikachu.datawisemaster.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 帖子点赞 服务类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
public interface IPostThumbService extends IService<PostThumb> {
    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    int doPostThumbInner(long userId, long postId);
}
