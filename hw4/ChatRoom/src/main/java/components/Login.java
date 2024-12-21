package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
 */
class Landen extends Frame implements ActionListener{

	private static final long serialVersionUID = 1L;
	//数据库连接对象
	public static Connection dbConnection = null;
	//sql执行对象
	public static PreparedStatement dbStatement = null;
	//处理结果集对象
	public static ResultSet dbResult = null;
	
	//设置聊天标题
	JFrame loginFrame = new JFrame("登录窗口");
	
	//添加汉字到面板
	JLabel usernameLabel = new JLabel("用户名：");
	JLabel passwordLabel = new JLabel("密码    ：");
	JLabel genderLabel = new JLabel("请选择性别：");
	
	
	//将性别选项添加到面板中并设置按钮
	static JRadioButton maleButton = new JRadioButton("男性");
	static JRadioButton femaleButton = new JRadioButton("女性");
	static JRadioButton otherButton = new JRadioButton("其它");
	
	
	//设置用户名输入框长度
	JTextField usernameInput = new JTextField(20);
	
	//设置密码输入框长度
	JPasswordField passwordInput = new JPasswordField(20);
	
	//将连接按钮添加到面板
	JButton loginButton = new JButton("登录");
	
	//将断开按钮添加到面板
	JButton disconnectButton = new JButton("断开");
	
	//将注册按钮添加到面板
	JButton registerButton = new JButton("注册");
	
	//设置标题边框
	TitledBorder titleBorder = new TitledBorder("");
	
	//设置单选
	ButtonGroup genderGroup = new ButtonGroup();
	
	/**
	 * 显示登录界面
	 */
	public void init(){
		/**登录背景**/
		
		//添加背景图片路径
		String inputbg = "src/main/java/img/login.jpg";
		//将图片添加到标签
		ImageIcon bg = new ImageIcon(inputbg);
		JLabel label = new JLabel(bg);
		//设置背景图片大小
		label.setSize(800,600);
		loginFrame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		
		//把窗口面板设为内容面板并设为透明，流动布局
		JPanel pan = (JPanel) loginFrame.getContentPane();
		pan.setOpaque(false);  // 设置为透明
		pan.setLayout(new FlowLayout());
		loginFrame.setLayout(null);
		
		
		/**用户名**/
		JLabel username = new JLabel("用户名：");
		Font f = new Font("宋体",Font.PLAIN,20);
		
		
		//设置字体
		username.setFont(f);
		
		//设置颜色
		username.setForeground(Color.WHITE);
		
		//用户名
		username.setBounds(200, 100, 120, 30);
		
		//用户名输入框
		usernameInput.setBounds(350, 100, 250, 30);
		pan.add(username);
		pan.add(usernameInput);
		
		/**密码**/
		JLabel password = new JLabel("密   码：");
		Font f1= new Font("宋体",Font.PLAIN,20);
		
		
		//设置字体
		password.setFont(f1);
		//设置颜色
		password.setForeground(Color.WHITE);
		//密码
		password.setBounds(200, 250, 120, 30);
		//密码输入框
		passwordInput.setBounds(350, 250, 250, 30);
		pan.add(password);
		pan.add(passwordInput);
		
		
		/**选择性别**/
		JLabel sex = new JLabel("请选择性别：");
		//设置字体
		sex.setFont(f1);
		//设置颜色
		sex.setForeground(Color.WHITE);
		//选择性别
		sex.setBounds(200, 400, 120, 30);
		pan.add(sex);
		Font f2 = new Font("宋体",Font.PLAIN,15);

		
		maleButton.setFont(f2);
		femaleButton.setFont(f2);
		otherButton.setFont(f2);
		maleButton.setForeground(Color.WHITE);
		femaleButton.setForeground(Color.WHITE);
		otherButton.setForeground(Color.WHITE);
		//选择性别按钮
		maleButton.setBounds(350, 400, 80, 30);
		femaleButton.setBounds(450, 400, 80, 30);
		otherButton.setBounds(550, 400, 80, 30);
		maleButton.setOpaque(false);
		femaleButton.setOpaque(false);
		otherButton.setOpaque(false);
		pan.add(maleButton);
		pan.add(femaleButton);
		pan.add(otherButton);
		//登录按钮
		loginButton.setBounds(250, 500, 100, 35);
		pan.add(loginButton);	
		//注册按钮
		registerButton.setBounds(400, 500, 100, 35);
		pan.add(registerButton);
		//设置按钮为单选
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);
		genderGroup.add(otherButton);
		//设置默认显示位置
		loginFrame.setLocation(800,300);//初始位置
		
		//设置聊天登录框大小
		loginFrame.setSize(800, 600);
		
		//设置此窗体是否可由用户调整大小。
		loginFrame.setResizable(false);
		
		//关闭聊天登录框时自动关闭程序
		loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//将聊天登录框在windows桌面显示
		loginFrame.setVisible(true);
		
		//添加图标
		loginFrame.setIconImage(new ImageIcon("src/main/java/img/ZJU1.png").getImage());
//		loginFrame.setLocationRelativeTo(null);
//		loginFrame.repaint();
		//添加监听
		loginButton.addActionListener(this);//登录按钮
		registerButton.addActionListener(this);//注册按钮
		
	}
	
	/**登录事件*/
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event){
		String aa = usernameInput.getText();
		String bb = passwordInput.getText();
		
		if(event.getActionCommand().equals("登录")){
			System.out.println("点击登录按钮");
			
			if(aa.equals("")){
				JOptionPane.showMessageDialog(null,"请输入用户名！");
				return;
			}
			if(bb.equals("")) {
				JOptionPane.showMessageDialog(null,"请输入密码！");
				return;
			}
			if(!maleButton.isSelected() && !femaleButton.isSelected() && !otherButton.isSelected()){
				JOptionPane.showMessageDialog(null,"请选择性别！");
				return;
			}
			
			try {
				System.out.println("尝试连接数据库...");
				dbConnection = DBUtils.getConn();
				if(dbConnection == null) {
					System.out.println("数据库连接失败: DBUtils.getConn() 返回 null");
					JOptionPane.showMessageDialog(null, "数据库连接失败: 无法获取连接");
					return;
				}
				System.out.println("数据库连接成功!");
				
				String sql = "select * from user where username=? and password=?";
				dbStatement = dbConnection.prepareStatement(sql);
				dbStatement.setString(1, aa);
				dbStatement.setString(2, bb);
				System.out.println("执行SQL查询: " + sql);
				System.out.println("用户名: " + aa);
				System.out.println("密码: " + bb);
				
				
				dbResult = dbStatement.executeQuery();
				System.out.println("查询完成，检查结果...");
				
				if(dbResult.next()) {
					System.out.println("登录成功，准备打开聊天室...");
					loginFrame.setVisible(false);
					String s1 = maleButton.isSelected() ? "boy" : 
							   femaleButton.isSelected() ? "girl" : "non-binary";
					
					Room chatroom = new Room();
					chatroom.getMenu(aa, s1);
					System.out.println("聊天室界面创建完成，准备建立连接...");
					chatroom.sock();
					System.out.println("聊天室连接建立完成");
				} else {
					System.out.println("用户名或密码错误");
					JOptionPane.showMessageDialog(null,"用户名或密码错误!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("发生异常: " + e.getMessage());
				JOptionPane.showMessageDialog(null, "登录失败: " + e.getMessage());
			} finally {
				DBUtils.close(dbConnection, dbStatement, dbResult);
			}
		}
		//如果点击注册则跳转注册页面
		if(event.getActionCommand().equals("注册")){
			//用来显示/隐藏GUI组件的
			loginFrame.dispose();
			new Register();
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