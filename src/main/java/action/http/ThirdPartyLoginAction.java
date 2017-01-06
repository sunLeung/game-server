package action.http;

import common.net.HttpAction;
import common.net.HttpPacket;
import common.net.HttpProtocol;
import common.utils.Def;
import common.utils.JsonRespUtils;
import pojo.User;
import service.UserService;

@HttpProtocol(Def.PROTOCOL_THIRDPARTY_LOGIN)
public class ThirdPartyLoginAction extends HttpAction {

	@Override
	public String excute(HttpPacket packet) {
		try {
			String data = packet.getData();
			User user = UserService.thirdPartylogin(packet.getIp(), data);
			if (user != null) {
				return JsonRespUtils.success(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JsonRespUtils.fail(Def.CODE_LOGIN_EXCEPTION,
					"Login exception");
		}
		return JsonRespUtils.fail(Def.CODE_FAIL, "Login Fail");
	}

}
