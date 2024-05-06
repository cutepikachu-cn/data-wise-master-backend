package cn.cutepikachu.datawisemaster.mapper;

import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 帖子收藏 Mapper 接口
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Mapper
public interface PostFavourMapper extends BaseMapper<PostFavour> {
    /**
     * 分页查询收藏帖子列表
     *
     * @param page
     * @param userId
     * @return
     */
    Page<Post> listFavourPostByPage(IPage<Post> page, @Param("userId") long userId);
}




