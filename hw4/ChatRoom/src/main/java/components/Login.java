package components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


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
	
	//设置聊天标题
	JFrame jf=new JFrame("登录窗口");
	
	//添加汉字到面板
	JLabel jl1=new JLabel("用户名：");
	JLabel jl2=new JLabel("密码    ：");
	JLabel jl3=new JLabel("请选择性别：");
	
	
	//将性别选项添加到面板中并设置按钮
	static JRadioButton jrb1=new JRadioButton("男性");
	static JRadioButton jrb2=new JRadioButton("女性");
	static JRadioButton jrb3=new JRadioButton("其它");
	
	
	//设置用户名输入框长度
	JTextField jtf1=new JTextField(20);
	
	//设置密码输入框长度
	JPasswordField jtf2=new JPasswordField(20);
	
	//将连接按钮添加到面板
	JButton jb1=new JButton("登录");
	
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
		/**登录背景**/
		
		//把窗口面板设为内容面板并设为不透明，流动布局
		JPanel pan = (JPanel) jf.getContentPane();
		pan.setOpaque(true);
		pan.setLayout(new FlowLayout());
		jf.setLayout(null);
		pan.setBackground(Color.WHITE);
		jf.setContentPane(pan);
		
		
		/**用户名**/
		JLabel username = new JLabel("用户名：");
		Font f = new Font("宋体",Font.PLAIN,20);
		
		
		//设置字体
		username.setFont(f);
		
		//设置颜色
		username.setForeground(Color.BLACK);
		
		/*
		 * 这个方法定义了组件的位置。 setBounds(x, y, width, height) x 和 y 指定左上角的新位置，由 width 和 height
		 * 指定新的大小。
		 */
		//用户名
		username.setBounds(200, 100, 120, 30);
		
		//用户名输入框
		jtf1.setBounds(350, 100, 250, 30);
		pan.add(username);
		pan.add(jtf1);
		
		/**密码**/
		JLabel password = new JLabel("密   码：");
		Font f1= new Font("宋体",Font.PLAIN,20);
		
		
		//设置字体
		password.setFont(f1);
		//设置颜色
		password.setForeground(Color.BLACK);
		//密码
		password.setBounds(200, 200, 120, 30);
		//密码输入框
		jtf2.setBounds(350, 200, 250, 30);
		pan.add(password);
		pan.add(jtf2);
		
		
		/**选择性别**/
		JLabel sex = new JLabel("请选择性别：");
		//设置字体
		sex.setFont(f1);
		//设置颜色
		sex.setForeground(Color.BLACK);
		//选择性别
		sex.setBounds(200, 300, 120, 30);
		pan.add(sex);
		Font f2 = new Font("宋体",Font.PLAIN,15);

		
		jrb1.setFont(f2);
		jrb2.setFont(f2);
		jrb3.setFont(f2);
		jrb1.setForeground(Color.BLACK);
		jrb2.setForeground(Color.BLACK);
		jrb3.setForeground(Color.BLACK);
		//选择性别按钮
		jrb1.setBounds(350, 300, 80, 30);
		jrb2.setBounds(450, 300, 80, 30);
		jrb3.setBounds(550, 300, 80, 30);
		jrb1.setOpaque(false);
		jrb2.setOpaque(false);
		jrb3.setOpaque(false);
		pan.add(jrb1);
		pan.add(jrb2);
		pan.add(jrb3);
		//登录按钮
		jb1.setBounds(250, 400, 100, 35);
		pan.add(jb1);
		//断开按钮
		jb2.setBounds(400, 400, 100, 35);
		pan.add(jb2);
		//注册按钮
		jb3.setBounds(550, 400, 100, 35);
		pan.add(jb3);
		//设置按钮为单选
		gb.add(jrb1);
		gb.add(jrb2);
		gb.add(jrb3);
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
//		jf.setLocationRelativeTo(null);
//		jf.repaint();
		//添加监听
		jb1.addActionListener(this);//登录按钮
		jb2.addActionListener(this);//断开按钮
		jb3.addActionListener(this);//注册按钮
		
	}
	
	/**登录事件*/
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		String aa=jtf1.getText();
		String bb=jtf2.getText();
		//定义s1为空
		String s1=null;
			//读取连接，断开按钮
			jb1.setText("登录");
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
					//如果登录成功则启动程序
					//添加监听器，如果点击连接时没有选择用户名与则会提示信息重新填写
					if(event.getActionCommand().equals("登录")){
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