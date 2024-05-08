package cn.cutepikachu.datawisemaster.controller;


import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.PageRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.dto.postfavour.PostFavourAddRequest;
import cn.cutepikachu.datawisemaster.model.dto.postfavour.PostFavourQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Post;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.vo.PostVO;
import cn.cutepikachu.datawisemaster.service.IPostFavourService;
import cn.cutepikachu.datawisemaster.service.IPostService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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
     */
    @PostMapping("/")
    public BaseResponse<?> doPostFavour(@RequestBody @Valid PostFavourAddRequest postFavourAddRequest) {
        Long postId = postFavourAddRequest.getPostId();
        boolean result = postFavourService.doPostFavour(postId);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success();
    }

    /**
     * 获取我收藏的帖子列表
     */
    @PostMapping("/page/self")
    public BaseResponse<Page<PostVO>> pageSelfFavourPost(@RequestBody @Valid PageRequest pageRequest) {
        Integer current = pageRequest.getCurrent();
        Integer size = pageRequest.getPageSize();
        User loginUser = userService.getLoginUser();
        Page<Post> postPage = postFavourService.pageFavourPost(current, size, loginUser.getId());
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage);
        return ResponseUtil.success(postVOPage);
    }

    /**
     * 获取用户收藏的帖子列表
     */
    @PostMapping("/page")
    public BaseResponse<Page<PostVO>> pageFavourPost(@RequestBody @Valid PostFavourQueryRequest postFavourQueryRequest) {
        Integer current = postFavourQueryRequest.getCurrent();
        Integer pageSize = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        Page<Post> postPage = postFavourService.pageFavourPost(current, pageSize, userId);
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage);
        return ResponseUtil.success(postVOPage);
    }

}
