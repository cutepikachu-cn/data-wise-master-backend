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
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.cutepikachu.datawisemaster.util.UserHolder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cutepikachu.datawisemaster.constant.CommonConstant.SALT;
import static cn.cutepikachu.datawisemaster.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-05 16:39:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword, String userNickname, UserRole userRole) {
        synchronized (userAccount.intern()) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
            long count = count(lambdaQueryWrapper);
            ThrowUtil.throwIf(count > 0, ResponseCode.PARAMS_ERROR, "账户已存在");
            String encryptPassword = DigestUtil.md5Hex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserNickname(userNickname);
            user.setUserRole(userRole);
            boolean saveResult = save(user);
            ThrowUtil.throwIf(!saveResult, ResponseCode.SYSTEM_ERROR, "注册失败");
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
        ThrowUtil.throwIf(user == null, ResponseCode.PARAMS_ERROR, "账号或密码错误");
        // 将登录用户状态保存至 session
        setUserLoginState(request, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser() {
        User currentUser = UserHolder.getUser();
        ThrowUtil.throwIf(currentUser == null || currentUser.getId() == null, ResponseCode.NOT_LOGIN_ERROR);
        Long userId = currentUser.getId();
        currentUser = getById(userId);
        ThrowUtil.throwIf(currentUser == null, ResponseCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 先判断是否已登录
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        Long userId = currentUser.getId();
        return this.getById(userId);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRole.ADMIN == user.getUserRole();
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        User loginUser = UserHolder.getUser();
        ThrowUtil.throwIf(loginUser == null, ResponseCode.OPERATION_ERROR, "未登录");
        // 移除登录态
        removeUserLoginState(request);
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
        return user.toVO(UserVO.class);
    }

    @Override
    public Page<UserVO> pageUserVO(Page<User> userPage) {
        List<User> userList = userPage.getRecords();
        Page<UserVO> userVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        if (userList.isEmpty()) {
            return userVOPage;
        }
        List<UserVO> userVOList = userList.stream().map(user -> user.toVO(UserVO.class)).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    private void setUserLoginState(HttpServletRequest request, User user) {
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
    }

    private void removeUserLoginState(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    @Override
    public LambdaQueryWrapper<User> getLambdaQueryWrapper(UserQueryRequest userQueryRequest) {

        Long id = userQueryRequest.getId();
        String userNickname = userQueryRequest.getUserNickname();
        String userProfile = userQueryRequest.getUserProfile();
        UserRole userRole = userQueryRequest.getUserRole();

        String sortField = userQueryRequest.getSortField();
        SortOrder sortOrder = userQueryRequest.getSortOrder();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id != null, User::getId, id);
        lambdaQueryWrapper.eq(ObjectUtil.isNotNull(userRole), User::getUserRole, userRole);
        lambdaQueryWrapper.like(StrUtil.isNotBlank(userNickname), User::getUserNickname, userNickname);
        lambdaQueryWrapper.like(StrUtil.isNotBlank(userProfile), User::getUserProfile, userProfile);
        // 排序
        if (StrUtil.isNotBlank(sortField)) {
            boolean isAsc = sortOrder == SortOrder.ASCENDING;
            switch (sortField.toLowerCase()) {
                case "createtime":
                    lambdaQueryWrapper.orderBy(true, isAsc, User::getCreateTime);
                    break;
                case "updatetime":
                    lambdaQueryWrapper.orderBy(true, isAsc, User::getUpdateTime);
                    break;
            }
        }

        return lambdaQueryWrapper;
    }

}




