package dao;

import pojo.User;

/**
 * Created by uc on 2016/12/29.
 */
public interface UserMapper {
    User selectUserById(long id);

    User selectUserByEmail(String email);

    User selectUserByPhone(String phone);
}
