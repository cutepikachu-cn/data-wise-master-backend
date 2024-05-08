package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.dto.user.*;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.LoginUserVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cn.cutepikachu.datawisemaster.constant.CommonConstant.SALT;

/**
 * <p>
 * 用户 前端控制器
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
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userNickname = userRegisterRequest.getUserNickname();
        UserRole userRole = userRegisterRequest.getUserRole();
        Long result = userService.userRegister(userAccount, userPassword, checkPassword, userNickname, userRole);
        return ResponseUtil.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest,
                                               HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtil.success(loginUserVO);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<?> userLogout(HttpServletRequest request) {
        ThrowUtil.throwIf(request == null, ResponseCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR, "退出登录失败");
        return ResponseUtil.success();
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User user = userService.getLoginUser();
        return ResponseUtil.success(userService.getLoginUserVO(user));
    }


    /**
     * 创建用户（管理员）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAddRequest.getUserAccount());
        long count = userService.count(lambdaQueryWrapper);
        ThrowUtil.throwIf(count > 0, ResponseCode.PARAMS_ERROR, "账户已存在");
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtil.md5Hex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success(user.getId());
    }

    /**
     * 删除用户（管理员）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<?> deleteUser(@RequestBody @Valid DeleteRequest deleteRequest) {
        boolean result = userService.removeById(deleteRequest.getId());
        if (!result) {
            return ResponseUtil.error(ResponseCode.PARAMS_ERROR, "用户不存在");
        }
        return ResponseUtil.success(result);
    }

    /**
     * 更新用户（管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<?> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR, "更新用户信息失败");
        return ResponseUtil.success();
    }

    /**
     * 根据 id 获取用户（管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<User> getUserById(@RequestParam Long id) {
        ThrowUtil.throwIf(id <= 0, ResponseCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtil.throwIf(user == null, ResponseCode.NOT_FOUND_ERROR);
        return ResponseUtil.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(@RequestParam Long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResponseUtil.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（管理员）
     */
    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<User>> pageUser(@RequestBody @Valid UserQueryRequest userQueryRequest) {
        Integer current = userQueryRequest.getCurrent();
        Integer size = userQueryRequest.getPageSize();
        LambdaQueryWrapper<User> lambdaQueryWrapper = userService.getLambdaQueryWrapper(userQueryRequest);
        Page<User> userPage = userService.page(new Page<>(current, size), lambdaQueryWrapper);
        return ResponseUtil.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<UserVO>> pageUserVO(@RequestBody @Valid UserQueryRequest userQueryRequest) {
        Integer current = userQueryRequest.getCurrent();
        Integer pageSize = userQueryRequest.getPageSize();
        LambdaQueryWrapper<User> lambdaQueryWrapper = userService.getLambdaQueryWrapper(userQueryRequest);
        Page<User> userPage = userService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<UserVO> userVOPage = userService.pageUserVO(userPage);
        return ResponseUtil.success(userVOPage);
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/update/self")
    public BaseResponse<?> updateSelf(@RequestBody @Valid UserUpdateSelfRequest userUpdateSelfRequest) {
        User loginUser = userService.getLoginUser();
        User user = new User();
        BeanUtil.copyProperties(userUpdateSelfRequest, user);
        user.setId(loginUser.getId());
        if (user.getUserPassword() != null) {
            String encryptPassword = DigestUtil.md5Hex((SALT + user.getUserPassword()).getBytes());
            user.setUserPassword(encryptPassword);
        }
        boolean result = userService.updateById(user);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success();
    }

}
