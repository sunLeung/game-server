package service;

import common.utils.SecurityUtils;
import dao.PlayerDao;
import dao.UserMapper;
import game.player.PlayerBean;
import cache.PlayerCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.User;

/**
 * Created by uc on 2016/12/29.
 */
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 登录
     *
     * @param identity
     * @param password
     * @param deviceid
     * @return
     */
    public static User login(String identity, String password, String deviceid) {
        User user = null;
        //使用email登录
        if (identity.contains("@")) {
            User user = UserMapper.selectUserByEmail(identity);
            if (bean != null && password.equals(SecurityUtils.decryptPassword(bean.getPassword()))) {
                bean.setDeviceid(deviceid);
                bean.setToken(SecurityUtils.createUUIDString());
                p = PlayerCache.initPlayer(bean);
            }
        } else {//使用手机登录
            PlayerBean bean = PlayerDao.loadByPhone(identity);
            if (bean != null && password.equals(SecurityUtils.decryptPassword(bean.getPassword()))) {
                bean.setDeviceid(deviceid);
                bean.setToken(SecurityUtils.createUUIDString());
                p = PlayerCache.initPlayer(bean);
            }
        }
        return p;
    }

}
