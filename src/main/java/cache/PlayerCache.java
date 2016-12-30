package cache;

import dao.PlayerDao;
import dao.UserMapper;
import game.player.Player;
import game.player.PlayerBean;
import pojo.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
	private static Map<Integer, User> userCacheContent = new ConcurrentHashMap<Integer, User>();

	public static Map<Integer, User> getUserCacheContent() {
		return userCacheContent;
	}

	public static void setUserCacheContent(Map<Integer, User> userCacheContent) {
		PlayerCache.userCacheContent = userCacheContent;
	}
	
	/**
	 * 获取用户对象
	 * @param id
	 * @return
	 */
	public static Player getUser(long id) {
		User user = getUserCacheContent().get(id);
		if (user == null) {
			User u = UserMapper.selectUserById(id);
			if(bean!=null){
				p=initPlayer(bean);
			}
		}else{
			p.setUpdateTime(System.currentTimeMillis());
		}
		return p;
	}
	
	/**
	 * 初始化玩家
	 * @param bean
	 * @return
	 */
	public static Player initPlayer(PlayerBean bean){
		Player p=new Player();
//		p.setBean(bean);
//		p.setLoadTime(System.currentTimeMillis());
//		p.setUpdateTime(System.currentTimeMillis());
//		getUserCacheContent().put(bean.getId(), p);
		return p;
	}
}
