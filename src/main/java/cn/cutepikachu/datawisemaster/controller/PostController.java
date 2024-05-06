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
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody @Valid PostAddRequest postAddRequest, HttpServletRequest request) {
        Post post = new Post();
        BeanUtil.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        boolean result = postService.save(post);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        long newPostId = post.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody @Valid DeleteRequest deleteRequest, HttpServletRequest request) {
        long id = deleteRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        User user = userService.getLoginUser(request);
        ThrowUtils.throwIf(!oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(request), ResponseCode.NO_AUTH_ERROR);
        boolean result = postService.removeById(id);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "删除帖子失败");
        return ResultUtils.success(true);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Boolean> updatePost(@RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        Post post = new Post();
        BeanUtil.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        long id = postUpdateRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<PostVO> getPostVOById(@RequestParam long id, HttpServletRequest request) {
        Post post = postService.getById(id);
        ThrowUtils.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR, "帖子不存在");
        PostVO postVO = postService.getPostVO(post, request);
        return ResultUtils.success(postVO);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Post>> pagePost(@RequestBody @Valid PostQueryRequest postQueryRequest) {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<Post> postPage = postService.page(new Page<>(current, size),
                postService.getLambdaQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<PostVO>> pagePostVO(@RequestBody PostQueryRequest postQueryRequest,
                                                 HttpServletRequest request) {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        LambdaQueryWrapper<Post> lambdaQueryWrapper = postService.getLambdaQueryWrapper(postQueryRequest);
        Page<Post> postPage = postService.page(new Page<>(current, size), lambdaQueryWrapper);
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/page/vo/self")
    public BaseResponse<Page<PostVO>> pageSelfPostVO(@RequestBody @Valid PostQueryRequest postQueryRequest,
                                                     HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        LambdaQueryWrapper<Post> lambdaQueryWrapper = postService.getLambdaQueryWrapper(postQueryRequest);
        Page<Post> postPage = postService.page(new Page<>(current, size), lambdaQueryWrapper);
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }

    /**
     * 编辑帖子（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editPost(@RequestBody @Valid PostEditRequest postEditRequest, HttpServletRequest request) {
        Post post = new Post();
        BeanUtil.copyProperties(postEditRequest, post);
        List<String> tags = postEditRequest.getTags();
        post.setTags(JSONUtil.toJsonStr(tags));
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ResponseCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        ThrowUtils.throwIf(!oldPost.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser), ResponseCode.NO_AUTH_ERROR);
        boolean result = postService.updateById(post);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

}
