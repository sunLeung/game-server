package common.db;

import common.config.Config;
import common.utils.TimerManagerUtils;
import game.dao.PlayerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class FlushDB {
	private static Logger logger= LoggerFactory.getLogger(FlushDB.class.getName());
	public static void flush(){
		logger.info("start flush db.");
		PlayerDao.flushPlayer();
		logger.info("end flush db.");
	}
	
	public static void init(){
		TimerManagerUtils.schedule(new Runnable() {
			@Override
			public void run() {
				flush();
			}
		}, Config.FLUSH_MINUTE, TimeUnit.MINUTES);
	}
}
