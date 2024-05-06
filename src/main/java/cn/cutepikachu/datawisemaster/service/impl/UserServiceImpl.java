package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.mapper.UserMapper;
import cn.cutepikachu.datawisemaster.model.dto.user.UserQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.LoginUserVO;
import cn.cutepikachu.datawisemaster.model.vo.UserVO;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cutepikachu.datawisemaster.constant.CommonConstant.SALT;
import static cn.cutepikachu.datawisemaster.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String userNickname, String userRole) {
        synchronized (userAccount.intern()) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
            long count = count(lambdaQueryWrapper);
            ThrowUtils.throwIf(count > 0, ResponseCode.PARAMS_ERROR, "账户已存在");
            String encryptPassword = DigestUtil.md5Hex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserNickname(userNickname);
            user.setUserRole(userRole.toLowerCase());
            boolean saveResult = save(user);
            ThrowUtils.throwIf(!saveResult, ResponseCode.SYSTEM_ERROR, "注册失败");
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        String encryptPassword = DigestUtil.md5Hex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        lambdaQueryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = getOne(lambdaQueryWrapper);
        ThrowUtils.throwIf(user == null, ResponseCode.PARAMS_ERROR, "账号或密码错误");
        setUserLoginState(request, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User currentUser = getUserLoginState(request);
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ResponseCode.NOT_LOGIN_ERROR);
        long userId = currentUser.getId();
        currentUser = getById(userId);
        ThrowUtils.throwIf(currentUser == null, ResponseCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User currentUser = getUserLoginState(request);
        return isAdmin(currentUser);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRole.ADMIN == UserRole.getEnumByValue(user.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        User loginUser = getUserLoginState(request);
        ThrowUtils.throwIf(loginUser == null, ResponseCode.OPERATION_ERROR, "未登录");
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public User getUserLoginState(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER_LOGIN_STATE);
    }

    @Override
    public LambdaQueryWrapper<User> getLambdaQueryWrapper(UserQueryRequest userQueryRequest) {

        Long id = userQueryRequest.getId();
        String userNickname = userQueryRequest.getUserNickname();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();

        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id != null, User::getId, id);
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(userRole), User::getUserRole, userRole);
        lambdaQueryWrapper.like(StrUtil.isNotBlank(userNickname), User::getUserNickname, userNickname);
        lambdaQueryWrapper.like(StrUtil.isNotBlank(userProfile), User::getUserProfile, userProfile);
        // 排序
        // 仅 createTime 和 updateTime
        if (StrUtil.isNotBlank(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, User::getCreateTime);
                case "updatetime" -> lambdaQueryWrapper.orderBy(true, isAsc, User::getUpdateTime);
            }
        }

        return lambdaQueryWrapper;
    }

    @Override
    public void setUserLoginState(HttpServletRequest request, User user) {
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
    }
}




