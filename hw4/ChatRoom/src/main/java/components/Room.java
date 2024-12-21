package components;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
/**
 * 聊天室大厅
 * @author 22145
 */
public class Room extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	//设置聊天室标题
	JFrame jf=new JFrame("聊天室");
	//声明变量
	public Client soc;
	public PrintWriter pw;
	//创建面板
	public JPanel jp1=new JPanel();//面板1
	public JPanel jp2=new JPanel();//面板2
	public JPanel jp3=new JPanel();//面板3
	public JPanel jp4=new JPanel();//面板4
	public JPanel jp5=new JPanel();//面板5
	public JPanel jp6=new JPanel();//面板6
	public JPanel jp7=new JPanel();//面板7
	//设置文本框
	public static JTextArea jta1=new JTextArea(12,42);//主文本框
	public static JTextArea jta2=new JTextArea(12,42);//我的频道
	//添加相应文本汉字
	public JLabel jl1=new JLabel("对");
	//设置下拉菜单
	public static JComboBox<String> jcomb=new JComboBox<String>();
	//设置发送聊天框长度
	public JTextField jtf=new JTextField(36);
	//添加发送按钮
	public JButton jb1=new JButton("发送>>");
	//添加刷新按钮
	public JButton jb2=new JButton("刷新");
	public static DefaultListModel<Object> listModel1 = new DefaultListModel<>();
	public static JList<Object> lst1 = new JList<>(listModel1);
	//声明变量
	public String na;
	public String se;
	public String message;
	// 添加个人信息面板
	public JPanel jpUserInfo = new JPanel();
	public JLabel lblUserTitle = new JLabel("个人信息");
	public JLabel lblUsername = new JLabel();
	public JLabel lblGender = new JLabel();
	public JLabel lblNoFriends = new JLabel("目前暂时无好友在线", SwingConstants.CENTER);
/**显示聊天界面*/
public void getMenu(String name, String sex) {
	//添加全部聊天
	jcomb.addItem("所有人");
	this.na = name;
	this.se = sex;
	
	//设置文本框不可编辑
	jta1.setEditable(false);
	jta2.setEditable(false);
	
	// 设置窗口基本属性
	jf.setSize(1200, 800);
	jf.setLocationRelativeTo(null);  // 居中显示
	jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	jf.setResizable(false);
	
	// 配置左侧面板
	JPanel jpLeft = new JPanel();
	jpLeft.setLayout(new BorderLayout());
	
	// 个人信息面板
	jpUserInfo.setLayout(new GridLayout(2, 1, 5, 5));
	jpUserInfo.setBorder(new TitledBorder("个人信息"));
	lblUsername.setText("账号: " + name);
	lblGender.setText("性别: " + sex);
	jpUserInfo.add(lblUsername);
	jpUserInfo.add(lblGender);
	
	// 发送消息面板
	JPanel jpSend = new JPanel();
	jpSend.setLayout(new GridLayout(4, 1, 5, 5));
	jpSend.setBorder(new TitledBorder("发送消息"));
	
	// 选择发送对象
	JPanel jpSelect = new JPanel(new FlowLayout(FlowLayout.LEFT));
	jpSelect.add(jl1);
	jpSelect.add(jcomb);
	
	// 文本框和按钮
	JPanel jpText = new JPanel(new FlowLayout(FlowLayout.LEFT));
	jtf.setPreferredSize(new Dimension(200, 25));
	jpText.add(jtf);
	
	JPanel jpButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
	jpButtons.add(jb1);
	jpButtons.add(jb2);
	
	// 添加到发送消息面板
	jpSend.add(jpSelect);
	jpSend.add(jpText);
	jpSend.add(jpButtons);
	
	// 组装左侧面板
	jpLeft.add(jpUserInfo, BorderLayout.NORTH);
	jpLeft.add(jpSend, BorderLayout.CENTER);
	
	// 设置聊天区域
	jp1.setLayout(new GridLayout(2, 1, 0, 5));
	
	// 设置滚动面板
	JScrollPane jsp1 = new JScrollPane(jta1);
	JScrollPane jsp2 = new JScrollPane(jta2);
	JScrollPane jsp3 = new JScrollPane(lst1);
	
	jsp1.setBorder(new TitledBorder("主聊天频道"));
	jsp2.setBorder(new TitledBorder("我的频道"));
	jsp3.setBorder(new TitledBorder("在线好友列表"));
	
	jp1.add(jsp1);
	jp1.add(jsp2);
	jp5.add(jp1);
	jp6.add(jsp3);
	
	// 设置各面板大小
	jpLeft.setPreferredSize(new Dimension(250, 750));
	jp5.setPreferredSize(new Dimension(700, 750));
	jp6.setPreferredSize(new Dimension(200, 750));
	
	// 设置主面板布局
	jp7.setLayout(new BorderLayout(10, 0));
	jp7.add(jpLeft, BorderLayout.WEST);
	jp7.add(jp5, BorderLayout.CENTER);
	jp7.add(jp6, BorderLayout.EAST);
	
	// 添加到主窗口并显示
	jf.add(jp7);
	jf.setVisible(true);  // 设置窗口可见
	
	// 添加按钮监听器
	jb1.addActionListener(this);
	jb2.addActionListener(this);
}
/**启动聊天室窗口*/
public void sock(){
	try{
		//将用户信息保存成字符串形式
		String user=na+"("+se+")";
		//创建客户端对象
		soc=new Client(user);
		//创建输出流
		pw=new PrintWriter(Client.socket.getOutputStream());
		//发送好友列表标识
		pw.println("好友列表");
		//发送用户信息
		pw.println(na+":"+se);
		//将内存中的数据一次性输出
		pw.flush();
		//发送进入聊天室标识
		pw.println("进入聊天室");
		//发送进入聊天室信息
		pw.println("【"+na+"】"+"进入聊天室");
		//将内存中的数据一次性输出
		pw.flush();
	}catch(Exception e){
		e.printStackTrace();
	}
}
/**设置窗口关闭事件，如果点击窗口右上角叉号关闭，执行下边程序*/
public Room() {
	jf.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			try {
				//创建输出流
				pw=new PrintWriter(Client.socket.getOutputStream());
				//发送下线标识
				pw.println("下线");
				//发送下线信息
				pw.println(na+":离开聊天室");
				//将内存中的数据一次性输出
				pw.flush();
				jf.dispose();//关闭窗口
			}catch(Exception ex) {
				
			}
		}
	});
}
/**事件触发*/
public void actionPerformed(ActionEvent event){
	//获取发送，刷新按钮
	jb1.setText("发送>>");
	jb2.setText("刷新");
	try{
		//创建输出流
		pw=new PrintWriter(Client.socket.getOutputStream());
		//点击发送触发
		if(event.getActionCommand().equals("发送>>")){
			if(!jtf.getText().equals("")){
				String selectedUser = (String)jcomb.getSelectedItem();
				if(!"所有人".equals(selectedUser)){
					// 私聊
					message = "私聊" + na + "(" + se + ")" + "对" + selectedUser + "说：" + jtf.getText();
					pw.println("私聊");
					pw.println(na + ":" + selectedUser + "分开" + message);
					pw.flush();
				} else {
					// 群聊
					pw.println("聊天");
					pw.println(na + "说：" + jtf.getText());
					pw.flush();
				}
			}
		//点击刷新触发
		}else if(event.getActionCommand().equals("刷新")){
			//创建输出流
			pw=new PrintWriter(Client.socket.getOutputStream());
			//发送刷新识
			pw.println("刷新");
			//将内存中数据一次性输出
			pw.flush();
			}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	//清空输入栏信息
	jtf.setText("");
	//输入焦点
	jtf.requestFocus();
	}
}