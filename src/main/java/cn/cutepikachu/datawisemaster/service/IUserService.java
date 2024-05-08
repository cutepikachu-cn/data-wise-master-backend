package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.dto.user.UserQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.LoginUserVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户 服务类
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
    Long userRegister(String userAccount,
                      String userPassword,
                      String checkPassword,
                      String userNickname,
                      UserRole userRole);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 登录用户信息封装对象
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @return 当前用户对象
     */
    User getLoginUser();

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @return 当前登录用户对象
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 判断用户是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员
     */
    boolean isAdmin(User user);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息封装对象
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取用户信息封装对象
     *
     * @param user 用户对象
     * @return 用户信息封装对象
     */
    UserVO getUserVO(User user);

    /**
     * 分页获取封装的用户信息
     *
     * @param userPage 分页用户对象
     * @return 分页封装的用户信息
     */
    Page<UserVO> pageUserVO(Page<User> userPage);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 查询参数
     */
    LambdaQueryWrapper<User> getLambdaQueryWrapper(UserQueryRequest userQueryRequest);
}
