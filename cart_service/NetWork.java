package LoginTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

//�����������緽�������
//��ָ����dns���������
public class NetWork {
	
	private CloseableHttpClient httpclient;
	private PoolingHttpClientConnectionManager cm;
	private HttpClientContext context;
	private CookieStore cs;
	private String lastIp;
	
	public NetWork()
	{
		RequestConfig globalConfig = RequestConfig.custom()
				 .setCookieSpec(CookieSpecs.STANDARD)
				 .build();

		cm=new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("locahost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);
		httpclient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig)
				.setConnectionManager(cm)
				.build();
		cs = new BasicCookieStore();
		context = HttpClientContext.create();
		context.setCookieStore(cs);
		
	}
	
	public void closeAll()
	{
		if(httpclient!=null)
		{
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean execPic(String url,String name)
	{
		HttpGet hg=new HttpGet(url);
		try {
			CloseableHttpResponse resp = httpclient.execute(hg, context);
			HttpEntity entity = resp.getEntity();
			InputStream is=entity.getContent();
			FileOutputStream fo=new FileOutputStream(new File(name+".jpg"));
			byte[] buff=new byte[1024];
			int len=0;
			while((len=is.read(buff))!=-1)
			{
				 fo.write(buff,0, len);
			}
			fo.flush();
			fo.close();
			is.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			hg.abort();
		}
		return false;
	}
	
	
	public boolean  execJdPic(String url,String name)
	{
	    HttpGet hg=new HttpGet(url);
	    hg.setHeader("Accept", "image/webp,*/*;q=0.8");
	    hg.setHeader("Accept-Encoding", "gzip, deflate, sdch");
	    hg.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
	    hg.setHeader("Cache-Control", "no-cache");
	    hg.setHeader("Connection", "keep-alive");
	    hg.setHeader("Host", "authcode.jd.com");
	    hg.setHeader("Pragma", "no-cache");
	    hg.setHeader("Referer", "https://passport.jd.com/new/login.aspx?ReturnUrl=http%3A%2F%2Fwww.jd.com%2F");
	    hg.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
		try {
			CloseableHttpResponse resp = httpclient.execute(hg, context);
			HttpEntity entity = resp.getEntity();
			InputStream is=entity.getContent();
			FileOutputStream fo=new FileOutputStream(new File(name+".jpg"));
			byte[] buff=new byte[1024];
			int len=0;
			while((len=is.read(buff))!=-1)
			{
				 fo.write(buff,0, len);
			}
			fo.flush();
			fo.close();
			is.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			hg.abort();
		}
		return false;
	}
	
	//��get��post��ת��
	/**
	 * ���� uri=https://passport.jd.com/uc/login
	 * �޸�Ϊhttps://58.83.212.154/uc/login
	 * @param hg
	 * @return
	 */
	private InetAddress getIp(HttpGet hg) throws Exception
	{
		URI uri=hg.getURI();
		InetAddress ia=InetAddress.getByName(uri.getHost());
		return ia;
	}
	
	private InetAddress getIp(HttpPost hp)throws Exception
	{
		URI uri=hp.getURI();
		InetAddress ia=InetAddress.getByName(uri.getHost());
		return ia;
	}
	
	
	public Response execGet(HttpGet hg) {
		try {
			InetAddress ia=getIp(hg);
			lastIp=ia.getHostAddress();
			context.setTargetHost(new HttpHost(ia));
			long startMili=System.currentTimeMillis();
			CloseableHttpResponse resp = httpclient.execute(hg, context);
			long endMili=System.currentTimeMillis();
			//������΢ʵ��һ�� 
			//��Ҫ�������������
			Response res = new Response();
			HttpEntity entity = resp.getEntity();
			MEntity mentity=new MEntity(entity);
			String content = new String(mentity.toString().getBytes("utf-8"), "UTF-8");
			res.setFileSize(mentity.getContentLength());
			res.setResp(resp);
			res.setContent(content);
			res.setState(0);
			res.setDuration(endMili-startMili);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Response res = new Response();
			res.setState(-1);
			res.setDuration(-1);
			return res;
		} finally {
			hg.abort();
		}
	}
	
	//ִ��post�����ҷ���response
	public Response execPost(HttpPost hp) {
		try {
			InetAddress ia=getIp(hp);
			lastIp=ia.getHostAddress();
			context.setTargetHost(new HttpHost(ia));
			long startMili=System.currentTimeMillis();
			CloseableHttpResponse resp = httpclient.execute(hp, context);
			long endMili=System.currentTimeMillis();
			//�����host�Ĳ���
			//System.out.println("post the host is:"+hh.getHostName());
			Response res = new Response();
			HttpEntity entity = resp.getEntity();
			MEntity mentity=new MEntity(entity);
			String content = new String(mentity.toString().getBytes("utf-8"), "UTF-8");
			res.setFileSize(mentity.getContentLength());
			res.setResp(resp);
			res.setContent(content);
			res.setState(0);
			res.setDuration(endMili-startMili);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Response res = new Response();
			res.setState(-1);
			res.setDuration(-1);
			return res;
		} finally {
			hp.abort();
		}
	}
	
	//���cookie
	public void addCookie(String name,String value)
	{
		BasicClientCookie bcc=new BasicClientCookie(name,value);
		cs.addCookie(bcc);
	}
	
	public String getCookie(String name)
	{
		String result="";
	
		List<Cookie> list = cs.getCookies();
		for (Cookie ck : list) {
			if (ck.getName().equals(name))
				result = ck.getValue();
		}
		return result;
	}
	
	public void printAllCookie()
	{
		List<Cookie> list=cs.getCookies();
		for(Cookie cook:list)
		{
			System.out.println(cook.getName()+":"+cook.getValue());
		}
	}
	
	//����Ӧ�ð�cookie����һ��
	//��cookie�洢���ļ�����
	public void storeCookie(String shopname)
	{
		try{
			//cookie�ı���ʹ����Ȼ���ڲ�������
			List<Cookie> list=cs.getCookies();
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(shopname+".txt")));
			//List<Cookie> list=cs.getCookies();
			for(Cookie cook:list)
			{
				bw.write(cook.getName()+":"+cook.getValue()+"\n");
			}
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//���ļ��ж�ȡcookie
	public void readCookie(String shopname)
	{
		try{
			//�����cookiestore
			cs.clear();
			//�ļ���ȡ
			BufferedReader br=new BufferedReader(new FileReader(new File(shopname+".txt")));
		    String temp="";
		    while((temp=br.readLine())!=null)
		    {
		    	System.out.println(temp);
		    	String[] temps=temp.split(":");
		    	System.out.println(temps[0]+"="+temps[1]);
		    	addCookie(temps[0],temps[1]);
		    }
		    br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//��¼��һ��ִ��exeGet����exePost��ip��ַ
	public String getIp()
	{
		return lastIp;
	}
	
	//�����ݶ�Ϊ5��
	public int getPing(String ip)
	{
		Ping ping=new Ping();
		return ping.doPing(ip,5);
	}
	
}
