package LoginTest;
//ר��������Ϊ���صĽ��
public class Result {
   //ִ�в�����״̬
   //-1��ʾû�гɹ�
   public int status=-1;
   //ִ�в���ʱ��vpn
   public static String vpn;
   //record vpc
   public static String vpc;
   //ִ�в����ĵ���
   public String shop;
   //ִ�в����ĵ�ǰʱ��(����ı�׼ʱ��)
   public long ctime;
   //ִ�еĲ�������
   public String extype;
   //http������
   public String httptype;
   //�����ĳ���ʱ��
   public long duration;
   //ping��ʱ��
   public int ptime;
   //���ص�����
   public String filetype;
   //�ļ���С(��λ����Ϊbyte)
   public long filesize;
   //ip��ַ
   public String ip;
   //ִ�в�����url��ַ
   public String url;
   
   public Result()
   {
	   
   }
   
   public String toString()
   {
	   return status+"\t"+
			  vpn+"\t"+
			  shop+"\t"+
			  ctime+"\t"+
			  extype+"\t"+
			  httptype+"\t"+
			  duration+"\t"+
			  ptime+"\t"+
			  filetype+"\t"+
			  filesize+"\t"+
			  ip+"\t"+
	          url;
   }
}
