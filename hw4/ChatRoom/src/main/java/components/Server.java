package components;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * 服务器
 * @author 22145
 */
public class Server extends Frame implements ActionListener{
	private static final long serialVersionUID = 1L;
	//定义图片位置
	String bg10="src/main/java/img/register.jpg";
	String start="src/main/java/img/start.gif";
	//启动服务器窗口
	JFrame jf = new JFrame("服务端启动");
	//服务器监听窗口
	JFrame jf2 = new JFrame("启动监听");
	//将图片添加到标签里
	ImageIcon bg=new ImageIcon(bg10);
	JLabel label=new JLabel(bg);
	//将监听背景图片加入窗口
	ImageIcon start1 = new ImageIcon(start);
	JLabel label2 = new JLabel(start1);
	//端口输入框
	JTextField jtf=new JTextField(20);
	//定义启动服务器按钮
	JButton jb=new JButton("启动服务器");
	JButton jb2 = new JButton("关闭服务器");
	//设置服务器端口
	private static final int PORT=1234;
	//创建G_Menu对象
	Room gm=new Room();
	//声明变量
	private ServerSocket server;
	//创建打印集合
	public ArrayList<PrintWriter> list;
	public static String user;
	//定义用户集合
	public static ArrayList<User> list1=new ArrayList<User>();
	public User uu;
	public Server(String user){
		Server.user=user;
	}
	/**服务器启动监听**/
	public void startdemo() {
		/**监听背景**/
		label2.setSize(480,300);
		jf2.getLayeredPane().add(label2,new Integer(Integer.MIN_VALUE));
		//透明流动窗口
		JPanel p2 = (JPanel) jf2.getContentPane();
		p2.setOpaque(false);
		p2.setLayout(new FlowLayout());
		jf2.setLayout(null);
		//退出服务器按钮
		jb2.setBounds(170,210,120,50);
		p2.add(jb2);
		//设置默认显示位置
		jf2.setLocation(800,290);//初始位置
		//设置聊天登录框大小
		jf2.setSize(480, 320);
		//设置此窗体是否可由用户调整大小。
		jf2.setResizable(false);
		//关闭聊天登录框时自动关闭程序
		jf2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//将聊天登录框在windows桌面显示
		jf2.setVisible(true);
		//添加图标
		jf2.setIconImage(new ImageIcon("src/main/java/img/企鹅2.png").getImage());
		//添加监听
		jb2.addActionListener(this);//退出服务器按钮
		jb2.setText("关闭服务器");
	}
	/**显示启动服务端页面**/
	public void initdemo() {
		/**启动背景**/
		label.setSize(800,600);
		jf.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
		//把窗口面板设为内容面板并设为透明，流动布局
		JPanel p = (JPanel) jf.getContentPane();
		p.setOpaque(false);
		p.setLayout(new FlowLayout());
		jf.setLayout(null);
		/**端口**/
		JLabel start = new JLabel("端口号：");
		Font f=new Font("宋体",Font.PLAIN,20);
		//设置字体
		start.setFont(f);
		//设置颜色
		start.setForeground(Color.BLACK);
		start.setBounds(180,100,80,25);
		jtf.setBounds(260, 100, 40, 30);
		p.add(start);
		p.add(jtf);
		jtf.setText("8088");
		/**启动按钮**/
		jb.setBounds(180, 260, 120, 50);
		p.add(jb);
		//设置默认显示位置
		jf.setLocation(800,300);//初始位置
		//设置聊天登录框大小
		jf.setSize(800, 600);
		//设置此窗体是否可由用户调整大小。
		jf.setResizable(false);
		//关闭聊天登录框时自动关闭程序
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//将聊天登录框在windows桌面显示
		jf.setVisible(true);
		//添加图标
		jf.setIconImage(new ImageIcon("src/main/java/img/企鹅2.png").getImage());
		//添加监听
		jb.addActionListener(this);//启动服务器按钮
		
	}
	public static void main(String[] args){
		new Server(user).initdemo();
	}
	class Chat implements Runnable{
		Socket socket;
		private BufferedReader br;
		private String msg;
		private String mssg="";
		public Chat(Socket socket){
			try{
				this.socket=socket;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	public void run(){
		try{
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while((msg=br.readLine())!=null){
				//匹配字符串 显示好友列表
				if(msg.equals("好友列表")){
					msg=br.readLine();
					//将用户信息跟消息分隔开
					String[] st=msg.split(":");
					//将用户信息添加到User对象中
					uu=new User(st[0],st[1],socket);
					//将对象添加到用户集合
					list1.add(uu);
					//遍历用户集合
					Iterator<User> it=Server.list1.iterator();
					while(it.hasNext()){
						User use=it.next();
						msg=use.getUsername()+"("+use.getPassword()+"):";
						//将所有的用户信息连接成一个字符串
						mssg+=msg;
						}
					//显示好友列表匹配标识
					sendMessage("好友列表");
					//群发消息
					sendMessage(mssg);
					//显示说话消息
					}else if(msg.equals("聊天")){
						msg=br.readLine();
						System.out.println(msg);
						//显示说话信息匹配标识
						sendMessage("聊天");
						sendMessage(msg);
						//显示进入聊天室
					}else if(msg.equals("进入聊天室")){
						msg=br.readLine();
						System.out.println(msg);
						//进入聊天室匹配标识
						sendMessage("进入聊天室");
						sendMessage(msg);
						//私聊
					}else if(msg.equals("私聊")){
						msg=br.readLine();
						//把传进来的用户信息跟说话内容分开
						String[] rt=msg.split("分开");
						//在服务器端显示说话内容
						System.out.println(rt[1]);
						//因为是私聊，传过来两个用户的用户信息，这句作用是再把两个用户信息分开
						String[] tg=rt[0].split(":");
						//遍历用户集合
						Iterator<User> iu=Server.list1.iterator();
						while(iu.hasNext()){
							User se=iu.next();
							//如果传进来的用户信息跟集合中的用户信息吻合
							if(tg[1].equals(se.getUsername()+"("+se.getPassword()+")")){
								try{
									//建立用户自己的流
									PrintWriter pwriter=new PrintWriter(se.getSock().getOutputStream());
									//匹配标识
									pwriter.println("私聊");
									//向单独用户发送消息
									pwriter.println(rt[1]);
									pwriter.flush();
									System.out.println(rt[1]);
								}catch(Exception e){
									e.printStackTrace();
								}
							//如果传进来的用户信息跟集合中的用户信息吻合
							}else if(tg[0].equals(se.getUsername())){
								try{
									PrintWriter pwr=new PrintWriter(se.getSock().getOutputStream());//建立用户自己的流
									//匹配标识
									pwr.println("私聊");
									//向单独用户发送消息
									pwr.println(rt[1]);
									pwr.flush();
									System.out.println(rt[1]);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						}
					//下线
					}else if(msg.equals("下线")){
						msg=br.readLine();
						//在服务端显示信息
						System.out.println(msg);
						//匹配字符串
						sendMessage("下线");
						//匹配完毕后群发消息
						sendMessage(msg);
						//将传过来的用户名跟信息分隔开
						String[] si=msg.split(":");
						//遍历用户集合
						Iterator<User> at=Server.list1.iterator();
						while(at.hasNext()){
							User sr=at.next();
							//如果传过来的用户名跟用户集合里的用户吻合
							if(sr.getUsername().equals(si[0])){
							//将吻合的用户移除
							list1.remove(sr);
							//关闭此用户的socket
							sr.getSock().close();
							}
						}
						break;
					//刷新
					}else if(msg.equals("刷新")){
						String mssge="";
						//遍历用户集合
						Iterator<User> iter=Server.list1.iterator();
						while(iter.hasNext()){
							User uus=iter.next();
							msg=uus.getUsername()+"("+uus.getPassword()+"):";
							//将所有的用户信息连接成一个字符串
							mssge+=msg;
						}
						//发送刷新匹配标识
						sendMessage("刷新");
						//群发消息
						sendMessage(mssge);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	//群发消息方法
	public void sendMessage(String message){
		try{
			//输出流集合
			for(PrintWriter pw:list){
				pw.println(message);
				pw.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
		}
	}
	/**启动服务器**/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb) {
			//用来显示/隐藏GUI组件的
			jf.setVisible(false);
			JOptionPane.showMessageDialog(this, "服务器正在启动....","服务器正在启动....",3);
			//初始化共享集合
			list =new ArrayList<PrintWriter>();
			new Server(user).startdemo();
			try{
				//向系统申请服务端口
				server=new ServerSocket(PORT);
		        	while(true){
							//接收客户端线程
							Socket client=server.accept();
							PrintWriter writer = new PrintWriter(client.getOutputStream());
					        list.add(writer); Thread t = new Thread(new Chat(client));
					        //启动线程
					        t.start();
					}
			}catch(Exception e1){
				jf2.setVisible(false);
				JOptionPane.showMessageDialog(this, "服务器启动错误","服务器启动错误",0);
			}
		}else if (e.getActionCommand().equals("关闭服务器")) {
        	jf2.setVisible(false);
       	    JOptionPane.showMessageDialog(this, "服务器已关闭");
       	    System.exit(0);
		}else{
			jf2.setVisible(false);
			JOptionPane.showMessageDialog(this, "服务器启动错误","服务器启动错误",0);
			}
		}
}