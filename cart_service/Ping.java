package LoginTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java�ٷ���ping��������ѧϰ���Ĵ���
 * http://docs.oracle.com/javase/1.5.0/docs/guide/nio/example/Ping.java
 * @author xiaoguodong
 *
 */
public class Ping {

	public Ping()
	{
		
	}
	
	//����ping�Ľ����λ��ms
	//���������-1
	public int  doPing(String ip,int times)
	{
		Process p=null;
		try{
		String s = null;
		String result="";
	    p = Runtime.getRuntime().exec("ping "+ip+" -n "+times);
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    // read the output from the command
	   // System.out.println("Here is the standard output of the command:\n");
	    while ((s = stdInput.readLine()) != null)
	    {
	       result+=s;
	    }
	    //���s����Ӧ�ķ���
	    Pattern pa=Pattern.compile("(?<=ƽ�� = ).*(?=ms)");
		Matcher m=pa.matcher(new String(result.getBytes("gbk"),"gbk"));
		if(m.find())
			result=result.substring(m.start(),m.end());
		
	    // read any errors from the attempted command
	    //System.out.println("Here is the standard error of the command (if any):\n");
	     while ((s = stdError.readLine()) != null)
	      {
	         System.out.println(s);
	      }
	     return Integer.parseInt(result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if(p!=null)
				p.destroy();
		}
		return -1;
	}
	
	public static void main(String[] args)
	{
		Ping ping=new Ping();
		int result=ping.doPing("211.151.155.11", 5);
		System.out.println(result);
	}
}
