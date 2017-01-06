package cache;

import dao.UserDao;
import pojo.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserContent {
    private static Map<Long, User> userCache = new ConcurrentHashMap<Long, User>();


    public static Map<Long, User> getUserCache() {
        return userCache;
    }

    public static void setUserCache(Map<Long, User> userCache) {
        UserContent.userCache = userCache;
    }

    /**
     * 获取用户对象
     *
     * @param id
     * @return
     */
    public static User getUser(Long id) {
        User user = getUserCache().get(id);
        if (user == null) {
            user = UserDao.loadById(id);
            if (user != null) {
                user = initUser(user);
            }
        } else {
            user.setUpdateTime(System.currentTimeMillis());
        }
        return user;
    }

    /**
     * 初始化用户
     *
     * @param user
     * @return
     */
    public static User initUser(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        getUserCache().put(user.getId(), user);
        return user;
    }
}
