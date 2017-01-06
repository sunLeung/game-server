package action.http;

import com.fasterxml.jackson.databind.JsonNode;
import common.net.HttpAction;
import common.net.HttpPacket;
import common.net.HttpProtocol;
import common.utils.Def;
import common.utils.JsonRespUtils;
import common.utils.JsonUtils;
import common.utils.StringUtils;
import pojo.User;
import service.UserService;

@HttpProtocol(Def.PROTOCOL_LOGIN)
public class LoginAction extends HttpAction{

	@Override
	public String excute(HttpPacket packet) {
		try {
			String data=packet.getData();
			JsonNode node=JsonUtils.decode(data);
			String identity = JsonUtils.getString("identity", node);
			String password = JsonUtils.getString("password",node);
			if(StringUtils.isNotBlank(identity)&&StringUtils.isNotBlank(password)){
				User user= UserService.login(identity, password);
				if(user!=null){
					return JsonRespUtils.success(user);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return JsonRespUtils.fail(Def.CODE_LOGIN_EXCEPTION, "Login exception");
		}
		return JsonRespUtils.fail(Def.CODE_FAIL, "The password is incorrect.");
	}
	
}
