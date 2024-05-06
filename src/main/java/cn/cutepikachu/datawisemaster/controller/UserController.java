package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.model.dto.user.*;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.LoginUserVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.cutepikachu.datawisemaster.constant.CommonConstant.SALT;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userNickname = userRegisterRequest.getUserNickname();
        String userRole = userRegisterRequest.getUserRole();
        long result = userService.userRegister(userAccount, userPassword, checkPassword, userNickname, userRole);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest,
                                               HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ResponseCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }


    /**
     * 创建用户（管理员）
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAddRequest.getUserAccount());
        long count = userService.count(lambdaQueryWrapper);
        ThrowUtils.throwIf(count > 0, ResponseCode.PARAMS_ERROR, "账户已存在");
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtil.md5Hex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户（管理员）
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<?> deleteUser(@RequestBody @Valid DeleteRequest deleteRequest) {
        boolean result = userService.removeById(deleteRequest.getId());
        if (!result) {
            return ResultUtils.error(ResponseCode.PARAMS_ERROR, "用户不存在");
        }
        return ResultUtils.success(result);
    }

    /**
     * 更新用户（管理员）
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result;
        try {
            result = userService.updateById(user);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "更新用户信息失败");
        }
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "更新用户信息失败");
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<User> getUserById(@RequestParam long id) {
        ThrowUtils.throwIf(id <= 0, ResponseCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResponseCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（管理员）
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<User>> pageUser(@RequestBody @Valid UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        LambdaQueryWrapper<User> lambdaQueryWrapper = userService.getLambdaQueryWrapper(userQueryRequest);
        Page<User> userPage = userService.page(new Page<>(current, size), lambdaQueryWrapper);
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<UserVO>> pageUserVO(@RequestBody @Valid UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        LambdaQueryWrapper<User> lambdaQueryWrapper = userService.getLambdaQueryWrapper(userQueryRequest);
        Page<User> userPage = userService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateSelfRequest
     * @param request
     * @return
     */
    @PostMapping("/update/self")
    public BaseResponse<Boolean> updateSelf(@RequestBody @Valid UserUpdateSelfRequest userUpdateSelfRequest,
                                            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtil.copyProperties(userUpdateSelfRequest, user);
        user.setId(loginUser.getId());
        if (user.getUserPassword() != null) {
            String encryptPassword = DigestUtil.md5Hex((SALT + user.getUserPassword()).getBytes());
            user.setUserPassword(encryptPassword);
        }
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
