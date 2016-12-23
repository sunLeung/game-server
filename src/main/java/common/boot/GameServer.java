package common.boot;

import common.config.Config;
import common.net.AdminServer;
import common.net.HttpProtocolContent;
import common.net.HttpServer;
import common.net.SocketServer;
import common.utils.TimerManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServer {
	private static Logger logger= LoggerFactory.getLogger(GameServer.class.getName());
	public static boolean isTrace=true;
	
	public static void main(String[] args) {
		try {
			long s = System.currentTimeMillis();
//			init();
			HttpServer.startHttpServer(9000);
			AdminServer.startAdminServer(9001);
			SocketServer.startSocketServer(9002);
			logger.info("GameServer started.Use seconds " + (System.currentTimeMillis() - s) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void init(){
		Config.init();
		HttpProtocolContent.init();
	}
	
	public static void stop(){
		try {
			TimerManagerUtils.destroyed();
			HttpServer.stopHttpServer();
			AdminServer.stopAdminServer();
			System.out.println("[INFO] GameServer stoped.");
			Config.PRINTER_RUN=false;
			Thread.sleep(1000);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
