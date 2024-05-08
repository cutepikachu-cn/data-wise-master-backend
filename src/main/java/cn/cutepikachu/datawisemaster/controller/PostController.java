package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.dto.post.PostAddRequest;
import cn.cutepikachu.datawisemaster.model.dto.post.PostEditRequest;
import cn.cutepikachu.datawisemaster.model.dto.post.PostQueryRequest;
import cn.cutepikachu.datawisemaster.model.dto.post.PostUpdateRequest;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 帖子 前端控制器
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private IPostService postService;

    @Resource
    private IUserService userService;

    /**
     * 创建
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody @Valid PostAddRequest postAddRequest) {
        Post post = new Post();
        BeanUtil.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        User loginUser = userService.getLoginUser();
        post.setUserId(loginUser.getId());
        boolean result = postService.save(post);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        Long newPostId = post.getId();
        return ResponseUtil.success(newPostId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<?> deletePost(@RequestBody @Valid DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtil.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        User user = userService.getLoginUser();
        ThrowUtil.throwIf(!oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(user), ResponseCode.NO_AUTH_ERROR);
        boolean result = postService.removeById(id);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR, "删除帖子失败");
        return ResponseUtil.success();
    }

    /**
     * 更新（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<?> updatePost(@RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        Post post = new Post();
        BeanUtil.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        Long id = postUpdateRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtil.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        boolean result = postService.updateById(post);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR, "更新帖子失败");
        return ResponseUtil.success();
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get/vo")
    public BaseResponse<PostVO> getPostVOById(@RequestParam long id) {
        Post post = postService.getById(id);
        ThrowUtil.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR, "帖子不存在");
        PostVO postVO = postService.getPostVO(post);
        return ResponseUtil.success(postVO);
    }

    /**
     * 分页获取列表（仅管理员）
     */
    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Post>> pagePost(@RequestBody @Valid PostQueryRequest postQueryRequest) {
        Integer current = postQueryRequest.getCurrent();
        Integer size = postQueryRequest.getPageSize();
        LambdaQueryWrapper<Post> lambdaQueryWrapper = postService.getLambdaQueryWrapper(postQueryRequest);
        Page<Post> postPage = postService.page(new Page<>(current, size), lambdaQueryWrapper);
        return ResponseUtil.success(postPage);
    }

    /**
     * 分页获取列表（封装类）
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<PostVO>> pagePostVO(@RequestBody @Valid PostQueryRequest postQueryRequest) {
        Integer current = postQueryRequest.getCurrent();
        Integer size = postQueryRequest.getPageSize();
        LambdaQueryWrapper<Post> lambdaQueryWrapper = postService.getLambdaQueryWrapper(postQueryRequest);
        Page<Post> postPage = postService.page(new Page<>(current, size), lambdaQueryWrapper);
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage);
        return ResponseUtil.success(postVOPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     */
    @PostMapping("/page/vo/self")
    public BaseResponse<Page<PostVO>> pageSelfPostVO(@RequestBody @Valid PostQueryRequest postQueryRequest) {
        User loginUser = userService.getLoginUser();
        postQueryRequest.setUserId(loginUser.getId());
        Integer current = postQueryRequest.getCurrent();
        Integer size = postQueryRequest.getPageSize();
        LambdaQueryWrapper<Post> lambdaQueryWrapper = postService.getLambdaQueryWrapper(postQueryRequest);
        Page<Post> postPage = postService.page(new Page<>(current, size), lambdaQueryWrapper);
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage);
        return ResponseUtil.success(postVOPage);
    }

    /**
     * 编辑帖子（用户）
     */
    @PostMapping("/edit")
    public BaseResponse<?> editPost(@RequestBody @Valid PostEditRequest postEditRequest) {
        Post post = new Post();
        BeanUtil.copyProperties(postEditRequest, post);
        List<String> tags = postEditRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        User loginUser = userService.getLoginUser();
        Long id = postEditRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtil.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        ThrowUtil.throwIf(!oldPost.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser), ResponseCode.NO_AUTH_ERROR);
        boolean result = postService.updateById(post);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR, "更新帖子信息失败");
        return ResponseUtil.success();
    }

}
