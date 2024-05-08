package cn.cutepikachu.datawisemaster.controller;


import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.dto.postthumb.PostThumbAddRequest;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.service.IPostThumbService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 帖子点赞 前端控制器
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Slf4j
@RestController
@RequestMapping("/post_thumb")
public class PostThumbController {

    @Resource
    private IPostThumbService postThumbService;

    @Resource
    private IUserService userService;

    /**
     * 点赞 / 取消点赞
     */
    @PostMapping("/")
    public BaseResponse<?> doThumb(@RequestBody @Valid PostThumbAddRequest postThumbAddRequest) {
        // 登录才能点赞
        final User loginUser = userService.getLoginUser();
        Long postId = postThumbAddRequest.getPostId();
        boolean result = postThumbService.doPostThumb(postId);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success();
    }

}
