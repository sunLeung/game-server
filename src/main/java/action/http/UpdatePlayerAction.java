package action.http;

import common.net.HttpAction;
import common.net.HttpPacket;
import common.net.HttpProtocol;
import common.utils.Def;
import common.utils.JsonRespUtils;
import service.UserService;

@HttpProtocol(Def.PROTOCOL_UPDATE_PLAYER)
public class UpdatePlayerAction extends HttpAction {

	@Override
	public String excute(HttpPacket packet) {
		try {
			String data = packet.getData();

			if (UserService.updatePlayer(packet.getUserId(), data)) {
				return JsonRespUtils.success("Update success");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JsonRespUtils.fail(Def.CODE_LOGIN_EXCEPTION,
					"Update exception");
		}
		return JsonRespUtils.fail(Def.CODE_FAIL, "Update Fail");
	}

}
