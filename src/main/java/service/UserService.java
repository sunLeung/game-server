package service;

import cache.UserContent;
import com.fasterxml.jackson.databind.JsonNode;
import common.utils.*;
import dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: leungyuxin
 * @date: 2017-01-02
 */
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);


    /**
     * 验证玩家是否登录
     *
     * @param id
     * @param token
     * @return
     */
    public static boolean isLogin(Long id, String token) {
        boolean result = false;
        User user = UserContent.getUser(id);
        if (user != null && user.getId() == id.longValue() && token.equals(user.getToken())) {
            result = true;
        }
        return result;
    }

    /**
     * 登录
     *
     * @param identity
     * @param password
     * @return
     */
    public static User login(String identity, String password) {
        User user = null;
        //使用email登录
        if (identity.contains("@")) {
            user = UserDao.loadByEmail(identity);
            if (user != null && password.equals(SecurityUtils.decryptPassword(user.getPassword()))) {
                user.setToken(SecurityUtils.createUUIDString());
                user = UserContent.initUser(user);
            }
        } else {//使用手机登录
            user= UserDao.loadByPhone(identity);
            if (user != null && password.equals(SecurityUtils.decryptPassword(user.getPassword()))) {
                user.setToken(SecurityUtils.createUUIDString());
                user = UserContent.initUser(user);
            }
        }
        return user;
    }

    /**
     * 第三方渠道登陆
     *
     * @param deviceid
     * @param data
     * @return
     */
    public static User thirdPartylogin(String deviceid, String ip, String data) {
        User user = null;
        JsonNode jsonData = JsonUtils.decode(data);
        String unionid = JsonUtils.getString("unionid", jsonData);
        if ("1".equals(unionid)) {//微信登陆
            user = weixinLogin(jsonData, deviceid);
        }
        return user;
    }

    public static User weixinLogin(JsonNode jsonData, String deviceid) {
        User user = null;
        JsonNode identity = jsonData.get("identity");
        String openid = identity.get("openid").asText();
        String openkey = identity.get("openkey").asText();
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(openkey)) {
            return null;
        }

        String appid = "wxcde873f99466f74a";
        String appkey = "bc0994f30c0a12a9908e353cf05d4dea";
        String url = "http://msdktest.qq.com/auth/check_token/";
        String timestamp = System.currentTimeMillis() / 1000 + "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid + "");
        params.put("timestamp", timestamp);
        params.put("sig", MD5.encode(appkey + timestamp));
        params.put("encode", 1 + "");
        url = HttpUtils.linkParams(url, params);


        Map<String, Object> content = new HashMap<String, Object>();
        content.put("accessToken", openkey);
        content.put("openid", openid);
        String result = HttpUtils.doPost(url, null, JsonUtils.encode2Str(content));
        System.out.println("weixin login:" + result);
        JsonNode rdata = JsonUtils.decode(result);
        int ret = JsonUtils.getInt("ret", rdata);
        if (ret == 0) {
//            user = UserDao.loadByThirdParty(openid);
//            if (user != null) {
//                user.setToken(SecurityUtils.createUUIDString());
//                user = UserContent.initUser(user);
//            } else {
//                user = new PlayerBean();
//                user.setSecret(SecurityUtils.createUUIDString());
//                int sex = JsonUtils.getInt("sex", jsonData);
//                user.setSex(sex);
//                String name = JsonUtils.getString("name", jsonData);
//                if (StringUtils.isBlank(name)) {
//                    name = StringUtils.randomName();
//                }
//                user.setName(name);
//                user.setToken(SecurityUtils.createUUIDString());
//                user.setThirdParty(openid);
//
//                user.setMoney(100);
//
//                int id = PlayerDao.save(user);
//                if (id != -1) {
//                    user = UserContent.getPlayer(id);
//                }
//            }
        }
        return user;
    }

//    public static boolean updatePlayer(int playerid, String data) {
//        JsonNode jsonData = JsonUtils.decode(data);
//        Player p = UserContent.getPlayer(playerid);
//        if (p != null) {
//            String name = JsonUtils.getString("name", jsonData);
//            if (StringUtils.isNotBlank(name))
//                p.getBean().setName(name);
//            int sex = JsonUtils.getInt("sex", jsonData);
//            if (sex != -1)
//                p.getBean().setSex(sex);
//            int r = PlayerDao.update(p.getBean());
//            if (r != -1) {
//                return true;
//            }
//        }
//        return false;
//    }

}
