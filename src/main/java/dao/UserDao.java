package dao;

import common.db.DbContent;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.User;

/**
 * @Description:
 * @author: leung
 * @date: 2017-01-02
 */
public class UserDao {
    public static Logger logger = LoggerFactory.getLogger(UserDao.class);

    public static long save(User user){
        SqlSession session = DbContent.getSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.insertUser(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return -1;
    }

    public static User loadById(long id) {
        SqlSession session = DbContent.getSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.selectUserById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }

    public static User loadByEmail(String email) {
        SqlSession session = DbContent.getSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.selectUserByEmail(email);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }

    public static User loadByPhone(String phone) {
        SqlSession session = DbContent.getSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.selectUserByPhone(phone);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }
}
