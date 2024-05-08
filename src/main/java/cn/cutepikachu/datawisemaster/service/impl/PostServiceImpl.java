package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.mapper.PostFavourMapper;
import cn.cutepikachu.datawisemaster.mapper.PostMapper;
import cn.cutepikachu.datawisemaster.mapper.PostThumbMapper;
import cn.cutepikachu.datawisemaster.model.dto.post.PostQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.PostFavour;
import cn.cutepikachu.datawisemaster.model.entity.PostThumb;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.SqlUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 帖子 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Resource
    private IUserService userService;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Override
    public LambdaQueryWrapper<Post> getLambdaQueryWrapper(PostQueryRequest postQueryRequest) {
        LambdaQueryWrapper<Post> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (postQueryRequest == null) {
            return lambdaQueryWrapper;
        }
        String searchText = postQueryRequest.getSearchText();
        String sortField = postQueryRequest.getSortField();
        SortOrder sortOrder = postQueryRequest.getSortOrder();
        Long id = postQueryRequest.getId();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTags();
        Long userId = postQueryRequest.getUserId();
        // 拼接查询条件
        if (StrUtil.isNotBlank(searchText)) {
            lambdaQueryWrapper.and(qw -> qw.like(Post::getTitle, searchText).or().like(Post::getContent, searchText));
        }
        lambdaQueryWrapper.like(StrUtil.isNotBlank(title), Post::getTitle, title);
        lambdaQueryWrapper.like(StrUtil.isNotBlank(content), Post::getContent, content);
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                lambdaQueryWrapper.like(Post::getTags, "\"" + tag + "\"");
            }
        }
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(id), Post::getId, id);
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(userId), Post::getUserId, userId);
        if (SqlUtil.validSortField(sortField)) {
            boolean isAsc = sortOrder == SortOrder.ASCENDING;
            switch (sortField.toLowerCase()) {
                case "thumbnum":
                    lambdaQueryWrapper.orderBy(true, isAsc, Post::getThumbNum);
                    break;
                case "favournum":
                    lambdaQueryWrapper.orderBy(true, isAsc, Post::getFavourNum);
                    break;
                case "createtime":
                    lambdaQueryWrapper.orderBy(true, isAsc, Post::getCreateTime);
                    break;
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public PostVO getPostVO(Post post) {
        PostVO postVO = post.toVO(PostVO.class);
        Long postId = post.getId();
        // 1. 关联查询用户信息
        Long userId = post.getUserId();
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        postVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getLoginUser();
        if (loginUser != null) {
            // 获取点赞
            LambdaQueryWrapper<PostThumb> postThumbLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postThumbLambdaQueryWrapper.eq(PostThumb::getPostId, postId);
            postThumbLambdaQueryWrapper.eq(PostThumb::getUserId, loginUser.getId());
            PostThumb postThumb = postThumbMapper.selectOne(postThumbLambdaQueryWrapper);
            postVO.setHasThumb(postThumb != null);
            // 获取收藏
            LambdaQueryWrapper<PostFavour> postFavourLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postFavourLambdaQueryWrapper.eq(PostFavour::getPostId, postId);
            postFavourLambdaQueryWrapper.eq(PostFavour::getUserId, loginUser.getId());
            PostFavour postFavour = postFavourMapper.selectOne(postFavourLambdaQueryWrapper);
            postVO.setHasFavour(postFavour != null);
        }
        return postVO;
    }

    @Override
    public Page<PostVO> getPostVOPage(Page<Post> postPage) {
        List<Post> postList = postPage.getRecords();
        Page<PostVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (CollUtil.isEmpty(postList)) {
            return postVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = postList.stream().map(Post::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> postIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> postIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUser();
        if (loginUser != null) {
            Set<Long> postIdSet = postList.stream().map(Post::getId).collect(Collectors.toSet());
            // 获取点赞
            LambdaQueryWrapper<PostThumb> postThumbLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postThumbLambdaQueryWrapper.in(PostThumb::getPostId, postIdSet);
            postThumbLambdaQueryWrapper.eq(PostThumb::getUserId, loginUser.getId());
            List<PostThumb> postPostThumbList = postThumbMapper.selectList(postThumbLambdaQueryWrapper);
            postPostThumbList.forEach(postPostThumb -> postIdHasThumbMap.put(postPostThumb.getPostId(), true));
            // 获取收藏
            LambdaQueryWrapper<PostFavour> postFavourLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postFavourLambdaQueryWrapper.in(PostFavour::getPostId, postIdSet);
            postFavourLambdaQueryWrapper.eq(PostFavour::getUserId, loginUser.getId());
            List<PostFavour> postFavourList = postFavourMapper.selectList(postFavourLambdaQueryWrapper);
            postFavourList.forEach(postFavour -> postIdHasFavourMap.put(postFavour.getPostId(), true));
        }
        // 填充信息
        List<PostVO> postVOList = postList.stream().map(post -> {
            PostVO postVO = post.toVO(PostVO.class);
            Long userId = post.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            postVO.setUser(userService.getUserVO(user));
            postVO.setHasThumb(postIdHasThumbMap.getOrDefault(post.getId(), false));
            postVO.setHasFavour(postIdHasFavourMap.getOrDefault(post.getId(), false));
            return postVO;
        }).collect(Collectors.toList());
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

}




