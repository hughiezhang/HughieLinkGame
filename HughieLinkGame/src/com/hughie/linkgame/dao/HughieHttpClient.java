package com.hughie.linkgame.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.base.service.HughieRequestParams;

/**
 * 获取网络请求
 * @ClassName: HughieHttpClient
 * @author hughiezhang
 * @since 2015-09-14 18:19
 */
public class HughieHttpClient {
	private static final int DEFAULT_MAX_CONNECTIONS = 10;
	private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	
	private static final int TIME_OUT = 30*1000;   				//超时时间
	
	private static String PROXY_IP = "http://10.0.0.172:80";
	private static BasicHttpParams httpParams;
	private static final DefaultHttpClient httpClient;
	private static HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
	
	static {
		httpParams = new BasicHttpParams();
		
		// 设置获取连接的最大等待时间  
		ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		// 设置每个路由最大连接数  
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		// 设置最大连接数  
		ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
		
		// 设置读取超时时间 
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
		
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
		
		httpClient = new DefaultHttpClient(cm, httpParams);
	}
	
	private static String execute(HttpUriRequest httpRequest) throws IOException, ClientProtocolException,
			HttpResponseException {
		HttpResponse httpResponse = httpClient.execute(httpRequest, httpContext);
		StatusLine status = httpResponse.getStatusLine();
		String responseBody = null;
		HttpEntity entity = null;
		HttpEntity temp = httpResponse.getEntity();
		if (temp != null) {
			entity = new BufferedHttpEntity(temp);
			responseBody = EntityUtils.toString(entity, "UTF-8");
		}
		
		if(status.getStatusCode() >= 300) {
			throw new HttpResponseException(status.getStatusCode(), status.getReasonPhrase());
		}
		
		return responseBody;
	}
	
	public static String getViaCmwap(String url) throws IOException {
		URL targetUrl = new URL(url);
		URL proxyUrl = new URL(PROXY_IP + targetUrl.getPath() + "?" + targetUrl.getQuery());
		HughieLoggerManager.println("网络连接: " + proxyUrl.toString());
		
		HttpURLConnection mHttpUrlConnection = (HttpURLConnection) proxyUrl.openConnection();
		mHttpUrlConnection.setRequestProperty("X-Online-Host", targetUrl.getHost() + ":" + targetUrl.getPort());
		mHttpUrlConnection.setRequestMethod("GET");
		mHttpUrlConnection.setRequestProperty("Accept", "text/html");
		mHttpUrlConnection.setRequestProperty("Pragma", "No-cache");
		mHttpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
		mHttpUrlConnection.setConnectTimeout(30 * 1000);
		byte[] rspData;
		try {
			if(mHttpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new HttpResponseException(mHttpUrlConnection.getResponseCode(), mHttpUrlConnection.getResponseMessage());
			}
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedInputStream input = new BufferedInputStream(mHttpUrlConnection.getInputStream());
			int ch = 0;
			while((ch = input.read()) != -1) {
				bos.write(ch);
			}
			rspData = bos.toByteArray();
		} finally {
			mHttpUrlConnection.disconnect();
		}
		
		return rspData == null ? "{}" : new String(rspData);
	}
	
	public static String getViaBadNetwork(String url) throws ClientProtocolException, IOException {
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT * 3);
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT * 3);
		HttpUriRequest httpRequest = new HttpGet(url);
		
		HughieLoggerManager.println("getViaBadNetwork()" + " get请求: " + url + " get响应： " + execute(httpRequest));
		return execute(httpRequest);
	}
	
	public static String get(String url, HughieRequestParams params) throws HttpResponseException, ClientProtocolException,
			IOException {
		return get(getUrlWithQueryString(url, params));
	}
	
	public static String get(String url) throws ClientProtocolException, IOException {
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpUriRequest httpRequest = new HttpGet(url);
		
		HughieLoggerManager.println("get()" + " get请求: " + url + " get响应： " + execute(httpRequest));
		return execute(httpRequest);
	}
	
	public static String getUrlWithQueryString(String url, HughieRequestParams params) {
		if (params != null) {
			String paramString = params.getParamString();
			if(url.indexOf("?") == -1) {
				url += "?" + paramString;
			} else {
				url += "&" + paramString;
			}
		}
		
		return url;
	}
	
	/**
     * android上传文件到服务器
     * @param String urlStr: 请求的url
     * @param Map<String, String> textMap 参数的字段及值
     * @param file  需要上传的文件
     * @param nameType 上传图片的标志
     * @return  返回响应的内容
     */
	public static String getContentByPost(String urlStr, Map<String, String> textMap, File bitmaps,
			String nameType){
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716";	//boundary就是request头和上传文件内容的分隔符 
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(TIME_OUT);	//连接超时 单位毫秒
			conn.setReadTimeout(TIME_OUT);	//读取超时 单位毫秒
			/* 将此URLConnection的doOutput字段的值设置为指定的值
			 * URL连接可用于输入和/或输出。如果打算使用URL连接进行输出，则将doOutput标志设置为true，
			 * 如果不打算使用，则设置为false。默认值为false。
			 */
			conn.setDoOutput(true);
			/* 将此URLConnection的doInput字段的值设置为指定的值
			 * URL连接可用于输入和/或输出。如果打算使用URL连接进行输入，则将doInput标志设置为true，
			 * 如果不打算使用，则设置为false。默认值为true。
			 */
			conn.setDoInput(true);
			conn.setUseCaches(false);	//Post请求不能使用缓存 
			conn.setRequestMethod("POST");	//设定请求的方法为"POST"，默认是GET
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if(textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if(inputValue == null) {
						continue;
					}
					
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			
			//单张图片
			if(bitmaps != null) {
				String filename = bitmaps.getName();
				String contentType = "application/octet-stream";
				
				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\"" + nameType + "\"; filename=\"" + filename  + "\"\r\n");
				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
				
				out.write(strBuf.toString().getBytes());
				DataInputStream in = new DataInputStream(new FileInputStream(bitmaps));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				in.close();
			}
			
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		
		return res;
	}
}
