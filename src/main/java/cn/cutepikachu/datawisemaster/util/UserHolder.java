package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.model.entity.User;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class UserHolder {

    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

}
