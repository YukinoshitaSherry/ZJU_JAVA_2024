package components;
import components.Client;  
/**
 * 下线标识：456987
 * 好友列表标识:1008611
 * 进入聊天室标识：10086
 * 聊天标识：10010
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
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
	//设置私聊按钮
	public JCheckBox jcb=new JCheckBox("私聊");
	//设置发送聊天框长度
	public JTextField jtf=new JTextField(36);
	//添加发送按钮
	public JButton jb1=new JButton("发送>>");
	//添加刷新按钮
	public JButton jb2=new JButton("刷新");
	public static DefaultListModel<Object> listModel1;
	public static JList<Object> lst1;
	//声明变量
	public String na;
	public String se;
	public String message;
/**显示聊天界面*/
public void getMenu(String name,String sex){
	//添加全部聊天
	jcomb.addItem("所有人");
	this.na=name;
	this.se=sex;
	//设置文本框不可编辑
	jta1.setEditable(false);
	jta2.setEditable(false);
	//JList并没有提供增删操作，需要使用DefaultListModel作为构造参数对列表进行增删的操作
	listModel1= new DefaultListModel<Object>();
	lst1 = new JList<Object>(listModel1);
	//一次只能选择一项
	lst1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//
	lst1.setVisibleRowCount(18);
	//设置固定单元的高度与宽度
	lst1.setFixedCellHeight(28);
	lst1.setFixedCellWidth(100);
	//添加滚动窗口
	JScrollPane jsp1=new JScrollPane(jta1);
	JScrollPane jsp2=new JScrollPane(jta2);
	JScrollPane jsp3=new JScrollPane(lst1);
	//添加聊天框标题
	jsp3.setBorder(new TitledBorder("好友列表"));
	jsp1.setBorder(new TitledBorder("主聊天频道"));
	jsp2.setBorder(new TitledBorder("我的频道"));
	//设置面板的行高列宽
	jp1.setLayout(new GridLayout(2,1));
	//将主聊天频道添加到面板1中
	jp1.add(jsp1);
	//将我的频道添加到面板1中
	jp1.add(jsp2);
	//组件按照加入的先后顺序按照设置的对齐方式从左向右排列，一行排满到下一行开始继续排列
	jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
	//对
	jp2.add(jl1);
	//添加下载菜单
	jp2.add(jcomb);
	//私聊按钮
	jp2.add(jcb);
	//组件按照加入的先后顺序按照设置的对齐方式从左向右排列，一行排满到下一行开始继续排列
	jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
	//发送文本框
	jp3.add(jtf);
	//发送按钮
	jp3.add(jb1);
	//面板4的行高列宽
	jp4.setLayout(new GridLayout(2,1));
	//将面板2,3放入面板4中
	jp4.add(jp2);
	jp4.add(jp3);
	//构造一个组件之间没有间距(默认间距为0像素)的新边框布局。
	jp5.setLayout(new BorderLayout());
	//将面板1放到面板5的上方,将面板4放到面板5的下方
	jp5.add(jp1,BorderLayout.NORTH);
	jp5.add(jp4,BorderLayout.SOUTH);
	//构造一个组件之间没有间距(默认间距为0像素)的新边框布局。
	jp6.setLayout(new BorderLayout());
	//将滚窗口list1放如面板6的上方,将面板2放入面板6的下方
	jp6.add(jsp3,BorderLayout.NORTH);
	jp6.add(jb2,BorderLayout.SOUTH);
	//组件按照加入的先后顺序按照设置的对齐方式从左向右排列，一行排满到下一行开始继续排列
	jp7.setLayout(new FlowLayout(FlowLayout.LEFT));
	//将面板5与面板6放入面板7中
	jp7.add(jp5);
	jp7.add(jp6);
	//在聊天窗口中添加面板7
	jf.add(jp7);
	//聊天窗口的默认显示位置
	jf.setLocation(700,200);
	//聊天窗口大小
	jf.setSize(700,650);
	//设置聊天窗口不可由用户调整大小
	jf.setResizable(false);
	//设置聊天窗口关闭时自动关闭程序
	jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	//聊天窗口在windows窗口上显示
	jf.setVisible(true);
	//给按钮添加鼠标监听，以接收发自此按钮的动作事件。当用户在此按钮上按下或释放鼠标时，发生动作事件。如果 l 为 null，则不抛出任何异常，也不执行任何动作。
	jb1.addActionListener(this);
	jb2.addActionListener(this);
	//自动换行
	jta1.setLineWrap(true);
	jta2.setLineWrap(true);
	//分别设置水平和垂直滚动条总是隐藏
	jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	jsp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	jsp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	jsp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	jsp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	//添加图标
	jf.setIconImage(new ImageIcon("src/main/java/img/企鹅2.png").getImage());
	//自动调整窗口大小
	jf.pack();
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
/**设置窗口关闭事件，如果点���窗口右上角叉号关闭，执行下边程序*/
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
				if(jcb.isSelected()){
					String name1=(String)jcomb.getSelectedItem();
					message="悄悄话"+na+"("+se+")"+"对"+name1+"说："+jtf.getText();
					//发送私聊标识
					pw.println("私聊");
					//发送私聊信息
					pw.println(na+":"+name1+"分开"+message);
					pw.flush();
				}else{
					//发送聊天标识
					pw.println("聊天");
					//发送聊天信息
					pw.println(na+"说："+jtf.getText());
					pw.flush();
				}
			}
		//点击刷新触发
		}else if(event.getActionCommand().equals("刷新")){
			//创建输出流
			pw=new PrintWriter(Client.socket.getOutputStream());
			//发送刷新标识
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