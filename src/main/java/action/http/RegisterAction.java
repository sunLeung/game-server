package action.http;

import dao.UserDao;
import cache.UserContent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;

import common.net.HttpAction;
import common.net.HttpPacket;
import common.net.HttpProtocol;
import common.utils.Def;
import common.utils.JsonRespUtils;
import common.utils.JsonUtils;
import common.utils.SecurityUtils;
import common.utils.StringUtils;
import pojo.User;

@HttpProtocol(Def.PROTOCOL_REGISTER)
public class RegisterAction extends HttpAction{

	@Override
	public String excute(HttpPacket packet) {
		try {
			String data=packet.getData();
			JsonNode node=JsonUtils.decode(data);
			String name = JsonUtils.getString("name",node);
			int sex = JsonUtils.getInt("sex",node);
			String phone = JsonUtils.getString("phone",node);
			String email = JsonUtils.getString("email",node);
			String password1 = JsonUtils.getString("password1",node);
			String password2 = JsonUtils.getString("password2",node);
			
			if(StringUtils.isBlank(name)||sex==-1||(StringUtils.isBlank(phone)&&StringUtils.isBlank(email))||StringUtils.isBlank(password1)||StringUtils.isBlank(password2)){
				return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "The necessary parameters cannot be null.");
			}
			
			if(!password1.equals(password2)){
				return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Two the password is not the same.");
			}
			
			if(password1.length()<6||password1.length()>16){
				return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Password length must between 6 and 16.");
			}
			
			if(StringUtils.isNotBlank(phone)){
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(phone);
				if(!isNum.matches()){
					return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Phone parameter is wrong.");
				}
			}
			
			if(StringUtils.isNotBlank(email)){
				Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher isEmail = pattern.matcher(email);
				if(!isEmail.matches()){
					return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Email parameter is wrong.");
				}
			}
			
			User u= UserDao.loadByPhone(phone);
			if(u!=null){
				return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Phone has been registered.");
			}
			
			u=UserDao.loadByEmail(email);
			if(u!=null){
				return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Email has been registered.");
			}
			
			User user=new User();
			user.setEmail(email);
			user.setName(name);
			user.setPassword(SecurityUtils.encryptPassword(password1));
			user.setPhone(phone);
			user.setSex(sex);
			user.setToken(SecurityUtils.createUUIDString());

			long id=UserDao.save(user);
			if(id!=-1){
				User user1= UserContent.getUser(id);
				Map<String,Object> r=new HashMap<String, Object>();
				r.put("id", user1.getId());
				r.put("name", user1.getName());
				r.put("token", user1.getToken());
				return JsonRespUtils.success(r);
			}
			return JsonRespUtils.fail(Def.CODE_REGISTER_FAIL, "Create player fail.");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonRespUtils.fail(Def.CODE_REGISTER_EXCEPTION, "Catch exception.");
		}
	}
	
}
