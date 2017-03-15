package com.demo.rk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

public class RuoKuai {
	
	
	public static void main(String[]args){
		String param = String.format("username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s", RkConstant.userName,RkConstant.password,RkConstant.type,RkConstant.timeOut,RkConstant.softid,RkConstant.softKey);
		
		
		
	}
	
	/**
	 * 
	 * 答题
	 * @param url 			请求URL，不带参数 如：http://api.ruokuai.com/create.xml
	 * @param param			请求参数，如：username=test&password=1
	 * @param data			图片二进制流
	 * @return				平台返回结果XML样式 
	 * @throws IOException
	 */
	public static String httpPostBase64(String url, String param,String base64) throws IOException {
		long time = (new Date()).getTime();
		URL u = null;
		HttpURLConnection con = null;
		String boundary = "----------" + MD5(String.valueOf(time));
		String boundarybytesString = "\r\n--" + boundary + "\r\n";
		OutputStream out = null;
		u = new URL(url);
		con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		con.setConnectTimeout(95000); //此值与timeout参数相关，如果timeout参数是90秒，这里就是95000，建议多5秒
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(true);
		con.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		out = con.getOutputStream();
		for (String paramValue : param.split("[&]")) {
			out.write(boundarybytesString.getBytes("UTF-8"));
			String paramString = "Content-Disposition: form-data; name=\""
					+ paramValue.split("[=]")[0] + "\"\r\n\r\n" + paramValue.split("[=]")[1];
			out.write(paramString.getBytes("UTF-8"));
		}
		out.write(boundarybytesString.getBytes("UTF-8"));
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"1.png\""); //注释:文件名一定要有的）
		sb.append("\r\n");
		sb.append("Content-Type: application/octet-stream"); //注释:文件名一定要有的）
		sb.append("\r\n");
		sb.append("Content-Transfer-Encoding: base64"); //注释:（注释:采用base64这一行一定要有）
		sb.append("\r\n\r\n");
		sb.append(base64);
		
		out.write(sb.toString().getBytes("UTF-8"));
		
		String tailer = "\r\n--" + boundary + "--\r\n";
		out.write(tailer.getBytes("UTF-8"));

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}
		return buffer.toString();
	}

	
	
	/**
	 * 字符串MD5加密
	 * @param s 原始字符串
	 * @return  加密后字符串
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
