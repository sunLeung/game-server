package mapper;

import pojo.User;

/**
 * @Description:
 * @author: leungyuxin
 * @date: 2017/1/2
 */
public interface UserMapper {
    User selectUserById(long id);

    User selectUserByEmail(String email);

    User selectUserByPhone(String phone);

    long insertUser(User user);
}
