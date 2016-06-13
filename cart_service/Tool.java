package LoginTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tool {
	
	private static Scanner scan=new Scanner(System.in);
	//��json�еݹ���ҵ���Ҫ������
	//����a.b[0].c.e
	//�������ݺܷ��㣬�������׳�Ϊƿ��
	public static String getValue(String content,String mkey)
	{
		JSONObject jj;
		String tcontent=content;
		String[] keys=mkey.split("\\.");
		String tkey="";
		int index=0;
		for(int i=0;i<keys.length;i++)
		{
			tkey=keys[i];
			try
			{
			  jj=new JSONObject(tcontent);
			  if(tkey.contains("["))
			  {
				index=Integer.parseInt(tkey.substring(tkey.indexOf('[')+1, tkey.indexOf(']')));
				String kkey=tkey.substring(0, tkey.indexOf('['));
				tcontent=jj.getString(kkey);
				JSONArray ja=new JSONArray(tcontent);
				tcontent=ja.getJSONObject(index).toString();
			  }
			  else{
				tcontent=jj.getString(tkey);
			  }
			  
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return tcontent;
	}
	
	//ͨ��������ʽ�������
	public static String getValByPattern(String content,String pattern)
	{
		String result="";
		Pattern pn = Pattern.compile(pattern);
		Matcher mc = pn.matcher(content);
		if (mc.find())
			result = content.substring(mc.start(), mc.end());
		return result;
	}
	
	
	
	//���õ���ģʽ����Ϊ�е���Դֻ��һ�ݶ�
	public static Scanner getScan()
	{
		return scan;
	}
	
	//�رղ��һ�����Դ
	public static void cloScan()
	{
		scan.close();
	}
	//��html���ݵĻ�ȡ����csspath���ӵĻ�ȡ
	//�������ʱ����
	
	//��ȡͼƬ���ұ����ڱ�Ŀ¼��

}
