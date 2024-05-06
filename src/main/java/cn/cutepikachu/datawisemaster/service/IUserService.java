package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.dto.user.UserQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.vo.LoginUserVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
public interface IUserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param userNickname  用户昵称
     * @param userRole      用户角色
     * @return 新用户 id
     */
    long userRegister(String userAccount,
                      String userPassword,
                      String checkPassword,
                      String userNickname,
                      String userRole);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);


    /**
     * 从 Session 中获取用户登录状态
     *
     * @param request
     * @return
     */
    User getUserLoginState(HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    LambdaQueryWrapper<User> getLambdaQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 将用户登录状态设置到 Session 中
     *
     * @param request
     * @param user
     */
    void setUserLoginState(HttpServletRequest request, User user);
}
