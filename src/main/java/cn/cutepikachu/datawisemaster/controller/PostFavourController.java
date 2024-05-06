package cn.cutepikachu.datawisemaster.controller;


import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.PageRequest;
import cn.cutepikachu.datawisemaster.model.dto.postfavour.PostFavourAddRequest;
import cn.cutepikachu.datawisemaster.model.dto.postfavour.PostFavourQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import cn.cutepikachu.datawisemaster.service.IPostFavourService;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 帖子收藏 前端控制器
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Slf4j
@RestController
@RequestMapping("/post_favour")
public class PostFavourController {
    @Resource
    private IPostFavourService postFavourService;
    @Resource
    private IPostService postService;
    @Resource
    private IUserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody @Valid PostFavourAddRequest postFavourAddRequest,
                                              HttpServletRequest request) {
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long postId = postFavourAddRequest.getPostId();
        int result = postFavourService.doPostFavour(postId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param pageRequest
     * @param request
     */
    @PostMapping("/page/self")
    public BaseResponse<Page<PostVO>> pageSelfFavourPost(@RequestBody @Valid PageRequest pageRequest,
                                                         HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long current = pageRequest.getCurrent();
        long size = pageRequest.getPageSize();
        Page<Post> postPage = postFavourService.pageFavourPost(current, size, loginUser.getId());
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
    @PostMapping("/page")
    public BaseResponse<Page<PostVO>> pageFavourPost(@RequestBody @Valid PostFavourQueryRequest postFavourQueryRequest,
                                                     HttpServletRequest request) {
        long current = postFavourQueryRequest.getCurrent();
        long pageSize = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        Page<Post> postPage = postFavourService.pageFavourPost(current, pageSize, userId);
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }
}
