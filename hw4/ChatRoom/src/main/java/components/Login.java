package components;

import java.awt.*;

import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * 登录界面
 * @author 22145
 */
class Landen extends Frame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//数据库连接对象
	public static Connection conn =null;
	//sql执行对象
	public static PreparedStatement ps = null;
	//处理结果集对象
	public static ResultSet rs =null;
	String inputbg="src/main/java/img/bg9.gif";
	
	//设置聊天标题
	JFrame jf=new JFrame("登陆窗口");
	
	//将图片添加到标签里
	ImageIcon bg=new ImageIcon(inputbg);
	JLabel label=new JLabel(bg);
	
	//添加汉字到面板
	JLabel jl1=new JLabel("用户名：");
	JLabel jl2=new JLabel("密码    ：");
	JLabel jl3=new JLabel("请选择性别：");
	
	
	//将性别选项添加到面板中并设置按钮
	static JRadioButton jrb1=new JRadioButton("男生");
	static JRadioButton jrb2=new JRadioButton("女生");
	static JRadioButton jrb3=new JRadioButton("保密");
	
	
	//设置用户名输入框长度
	JTextField jtf1=new JTextField(20);
	
	//设置密码输入框长度
	JPasswordField jtf2=new JPasswordField(20);
	
	//将连接按钮添加到面板
	JButton jb1=new JButton("登陆");
	
	//将断开按钮添加到面板
	JButton jb2=new JButton("断开");
	
	//将注册按钮添加到面板
	JButton jb3=new JButton("注册");
	
	//设置标题边框
	TitledBorder tb=new TitledBorder("");
	
	//设置单选
	ButtonGroup gb=new ButtonGroup();
	
	/**
	 * 显示登录界面
	 */
	public void init(){
		/**登陆背景**/
		//把标签的大小设为和图片大小相同
		label.setSize(480,400);
		jf.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
		
		
		//把窗口面板设为内容面板并设为透明，流动布局
		JPanel pan = (JPanel) jf.getContentPane();
		pan.setOpaque(false);
		pan.setLayout(new FlowLayout());
		jf.setLayout(null);
		
		
		/**用户名**/
		JLabel username = new JLabel("用户名：");
		Font f = new Font("微软雅黑",Font.PLAIN,20);
		
		
		//设置字体
		username.setFont(f);
		
		//设置颜色
		username.setForeground(Color.WHITE);
		
		/*
		 * 这个方法定义了组件的位置。 setBounds(x, y, width, height) x 和 y 指定左上角的新位置，由 width 和 height
		 * 指定新的大小。
		 */
		//用户名
		username.setBounds(100, 50, 80, 25);
		
		//用户名输入框
		jtf1.setBounds(200,50,165,25);
		pan.add(username);
		pan.add(jtf1);
		
		/**密码**/
		JLabel password = new JLabel("密   码：");
		Font f1= new Font("微软雅黑",Font.PLAIN,20);
		
		
		//设置字体
		password.setFont(f1);
		//设置颜色
		password.setForeground(Color.WHITE);
		//密码
		password.setBounds(100, 100, 80, 25);
		//密码输入框
		jtf2.setBounds(200, 100, 165, 25);
		pan.add(password);
		pan.add(jtf2);
		
		
		/**选择性别**/
		JLabel sex = new JLabel("请选择性别：");
		//设置字体
		sex.setFont(f1);
		//设置颜色
		sex.setForeground(Color.WHITE);
		//请选择性别
		sex.setBounds(60, 160, 120, 25);
		pan.add(sex);
		Font f2 = new Font("微软雅黑",Font.PLAIN,15);
		
		
		jrb1.setFont(f2);
		jrb2.setFont(f2);
		jrb3.setFont(f2);
		jrb1.setForeground(Color.WHITE);
		jrb2.setForeground(Color.WHITE);
		jrb3.setForeground(Color.WHITE);
		//选择性别按钮
		jrb1.setBounds(195, 163, 55, 25);
		jrb2.setBounds(260, 163, 55, 25);
		jrb3.setBounds(320, 163, 55, 25);
		jrb1.setOpaque(false);
		jrb2.setOpaque(false);
		jrb3.setOpaque(false);
		pan.add(jrb1);
		pan.add(jrb2);
		pan.add(jrb3);
		//登陆按钮
		jb1.setBounds(65, 260, 80, 30);
		pan.add(jb1);
		//断开按钮
		jb2.setBounds(190, 260, 80, 30);
		pan.add(jb2);
		//注册按钮
		jb3.setBounds(320, 260, 80, 30);
		pan.add(jb3);
		//设置按钮为单选
		gb.add(jrb1);
		gb.add(jrb2);
		gb.add(jrb3);
		//设置默认显示位置
		jf.setLocation(800,300);//初始位置
		
		//设置聊天登陆框大小
		jf.setSize(480, 400);
		
		//设置此窗体是否可由用户调整大小。
		jf.setResizable(false);
		
		//关闭聊天登陆框时自动关闭程序
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//将聊天登陆框在windows桌面显示
		jf.setVisible(true);
		
		//添加图标
		jf.setIconImage(new ImageIcon("src/main/java/img/企鹅2.png").getImage());
//		jf.setLocationRelativeTo(null);
//		jf.repaint();
		//添加监听
		jb1.addActionListener(this);//登陆按钮
		jb2.addActionListener(this);//断开按钮
		jb3.addActionListener(this);//注册按钮
		
	}
	
	/**登陆事件*/
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		String aa=jtf1.getText();
		String bb=jtf2.getText();
		//定义s1为空
		String s1=null;
			//读取连接，断开按钮
			jb1.setText("登陆");
			jb3.setText("注册");
			jb2.setText("断开");
			try {
				//获取连接
				conn = DBUtils.getConn();
				//编写sql语句
				String sql = "select * from user where username=? and password=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,aa);
				ps.setString(2,bb);
			    rs=ps.executeQuery();
					//添加监听器，如果点击注册是没有
					//如果登陆成功则启动程序
					//添加监听器，如果点击连接时没有选择用户名与则会提示信息重新填写
					if(event.getActionCommand().equals("登陆")){
						if(jtf1.getText().equals("")){
							JOptionPane.showMessageDialog(null,"请输入用户名！");
						}else if(jtf2.getText().equals("")) {
							JOptionPane.showMessageDialog(null,"请输入密码！");
						}else if(!jrb1.isSelected()&&!jrb2.isSelected()&&!jrb3.isSelected()){
							JOptionPane.showMessageDialog(null,"请选择性别！");
						}else if(rs.next()) {
							//用来显示/隐藏GUI组件的
							jf.setVisible(false);
							//用来显示聊天大厅性别
							if(jrb1.isSelected()){
								s1="boy";
							}else if(jrb2.isSelected()){
								s1="girl";
							}else if(jrb3.isSelected()){
								s1="secret";
							}
							//定义G_Menu对象
							Room gmu=new Room();
							//显示大厅用户名(性别)
							gmu.getMenu(jtf1.getText(),s1);
							//调用G_Menu对象中的sock方法
							gmu.sock();	
							DBUtils.close(conn, ps, rs);
						}else {
							JOptionPane.showMessageDialog(null,"用户名或密码错误!");
						}
					}
					}catch (Exception e) {
						e.printStackTrace();
					}finally {
						DBUtils.close(conn, ps, rs);
					}
		//如果点击注册则跳转注册页面
		if(event.getActionCommand().equals("注册")){
			//用来显示/隐藏GUI组件的
			jf.dispose();
            new Register();
		}
		//如果点击断开则结束程序
		if(event.getActionCommand().equals("断开")){
			System.exit(0);	
		}
	}
}
/**
 * 运行客户端面板
 * @author Administrator
 */
public class Login{
	public static void main(String[] args){
		new Landen().init();
	}
}