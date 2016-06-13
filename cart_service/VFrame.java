package LoginTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

//����߳���Ҫ������ȡ��֤�룬��������join��������ǰ���̵߳ȴ���ִ�����
public class VFrame extends JFrame implements ActionListener{

	//һЩ�����ı���
	//���������紦�����
	private NetWork nw;
	//�������֤����վ
	private String cname;
	//ͼƬ��url(���Ҳ���ʱ������)
	private String url;
	//��Ҫ�ڱ�������ʾ����Ϣ
	private String title;
	//flag
	private boolean flag;
	//textfield;
	private JLabel label;
	private JTextField tf;
	public String result;
	private int i=0;
	
	
	//���ϸ�ڵĵط�����Ҫ�޸�
	public VFrame(NetWork nw,String cname,String url,String title)
	{
		//�����ı���
		this.nw=nw;
		this.cname=cname;
		this.url=url;
		this.title=title;
		flag=false;
		//��������
		setTitle(cname+"--"+title);
		setLayout(null);
		setSize(200,200);
		
		label=new JLabel();
		//�ȵõ�ͼƬ
		cname+=Math.random();
		if(cname.contains("jd"))
		{
			System.out.println("yes here!");
			nw.execJdPic(url, cname);
		}
		else
		{
			nw.execPic(url, cname);
		}
		//init
		//Client cl=new Client(cname+".jpg");
		
		//get and set
		//String str=null;
		//str=cl.run();
		//System.out.println("��֤�룺"+str);
		//tf=new JTextField();
		//tf.setText(str);
		//flag=true;
		//��������������ͼƬ�
		label.setIcon(new ImageIcon(cname+".jpg"));
		label.setBounds(0, 40, 120, 40);
		add(label);
		File image=new File(cname+".jpg");
		if(image.exists())
			image.delete();
		//ˢ�°�ť
		JButton but1=new JButton("refresh");
		but1.setBounds(120, 40, 60, 40);
		but1.addActionListener(this);
		add(but1);
		//�����
		tf=new JTextField();
		tf.setBounds(20, 100, 100, 40);
		add(tf);
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					result=tf.getText();
					flag=true;
				}
			}
		});
		//ȷ����ť
		JButton but2=new JButton("ok");
		but2.setBounds(120, 100,60,40);
		but2.addActionListener(this);
		add(but2);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		if(command.equals("refresh"))
		{
			//System.out.println("ˢ����֤��");
			tf.setText("");
			cname+=i;
			if(cname.contains("jd"))
			{
				System.out.println("yes here!");
				nw.execJdPic(url, cname);
			}
			else
			{
				nw.execPic(url, cname);
			}
			label.setIcon(new ImageIcon(cname+".jpg"));
			label.repaint();
			File image=new File(cname+".jpg");
			if(image.exists())
				image.delete();
			this.repaint();
			i++;
		}
		else if(command.equals("ok"))
		{
			//System.out.println("�ص����ڷ��ؽ��");
			result=tf.getText();
			flag=true;
		}
	}
	
	public String getText()
	{
		while(!flag)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.dispose();
		return tf.getText();
	}
	
	public static void main(String[] args)throws Exception
	{
		//�����ȳ�ʼ�����ɱ�����
		String url="https://vcs.suning.com/vcs/imageCode.htm?uuid=eaf19088-1776-4d6e-95aa-18019f4a0b42&yys=1441245723554";
		NetWork nw=new NetWork();
		String cname="sn";
		VFrame mm=new VFrame(nw,cname,url,"��֤��");
		String result=mm.getText();
		System.out.println("yes i came here"+result);
		
	}
}
