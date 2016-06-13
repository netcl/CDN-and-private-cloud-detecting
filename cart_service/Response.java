package LoginTest;

import org.apache.http.client.methods.CloseableHttpResponse;

//��һ����ר�Ź����ص�����
public class Response {
   
	//����ִ�е�״̬
	/*
	 * equal 0 ��ʾ�����������������󷵻ص�״̬����Ҫcheck��
	 * equal 1 ��ʾ�����쳣�������״̬ʲô�ľͲ���check��
	 */
	private int state;
	//���󷵻ص�����
	private String content;
	private CloseableHttpResponse resp;
	//���󻨷ѵ�ʱ��
	private long duration;
	//���ص��ļ���С
	private long filesize;
	
	public Response()
	{
		
	}
	
	public Response(CloseableHttpResponse resp,String content,int state)
	{
		this.resp=resp;
		this.content=content;
		this.state=state;
	}
	
	public void setResp(CloseableHttpResponse resp)
	{
		this.resp=resp;
	}
	
	public CloseableHttpResponse getResp()
	{
		return resp;
	}
	
	public void setContent(String content)
	{
		this.content=content;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setState(int scode)
	{
		this.state=scode;
	}
	
	public int getState()
	{
		return this.state;
	}
	
	public void setDuration(long l)
	{
		this.duration=l;
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public void setFileSize(long size)
	{
		this.filesize=size;
	}
	
	public long getFileSize()
	{
		return filesize;
	}
}
