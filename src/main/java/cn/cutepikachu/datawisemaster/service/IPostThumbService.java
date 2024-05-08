package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.entity.PostThumb;
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
     * 点赞 / 取消点赞
     *
     * @param postId 帖子id
     * @return 是否点赞/取消点赞成功
     */
    boolean doPostThumb(Long postId);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否点赞成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean doPostThumbInner(Long userId, Long postId);
}
