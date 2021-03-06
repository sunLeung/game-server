package common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
	
	public static String doGet(String url) {
		return doGet(url,null);
	}
	
	public static String doGet(String url, Map<String,String> params) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			url=linkParams(url, params);
			System.out.println(url);
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setReadTimeout(2000);
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String doPost(String url, Map<String,String> params) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(2000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(getParamsStr(params));
			out.flush();
			if(conn.getResponseCode()==200)
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			else{
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
	
	public static String doPost(String url,Map<String,String> requestProperty, String body) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(requestProperty!=null)
			for(Entry<String,String> entry:requestProperty.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
//			conn.setReadTimeout(5000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			if(conn.getResponseCode()==200){
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else{
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			if(in!=null){
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
	
	/**
	 * 创建带参数的URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static String linkParams(String url,Map<String,String> params){
		StringBuffer sb=new StringBuffer();
		if(params==null)return url;
		if(params.size()<=0)return url;
		sb.append(url).append("?");
		for(Entry<String,String> entry:params.entrySet()){
			String value=entry.getValue();
			if(StringUtils.isEmpty(value))value="";
			sb.append(entry.getKey()).append("=").append(value).append("&");
		}
		return StringUtils.removeEnd(sb.toString(), "&");
	}
	
	/**
	 * 获取参数字符串
	 * @param params
	 * @return
	 */
	public static String getParamsStr(Map<String,String> params){
		StringBuffer sb=new StringBuffer();
		if(params==null)return "";
		if(params.size()<=0)return "";
		for(Entry<String,String> entry:params.entrySet()){
			String value=entry.getValue();
			if(StringUtils.isEmpty(value))value="";
			sb.append(entry.getKey()).append("=").append(value).append("&");
		}
		return StringUtils.removeEnd(sb.toString(), "&");
	}
}
