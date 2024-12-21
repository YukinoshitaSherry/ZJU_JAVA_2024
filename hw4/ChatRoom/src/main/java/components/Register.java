package components;
import components.DBUtils;
import components.Login;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 * 注册界面
 * @author 22145
 */
public class Register extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	//数据库连接对象
	public static Connection conn =null;
	//sql执行对象
	public static PreparedStatement ps = null;
	//处理结果集对象
	public static ResultSet rs =null;
	String inputbg="src/main/java/img/register.jpg";
	/**********************定义各控件********************************/
    JFrame jf=new JFrame("注册窗口");
    //将图片添加到标签
    ImageIcon bg = new ImageIcon(inputbg);
    JLabel label = new JLabel(bg);
	//用户名输入框
    private static JTextField tfAccount=new JTextField(20);
    //密码输入框
    private static JPasswordField pfPassword=new JPasswordField(10);
    //确认密码输入框
    private JPasswordField pfPassword2=new JPasswordField(10);
    //定义注册按钮与返回登录按钮
    private JButton btRegister=new JButton("注册");
    private JButton btExit=new JButton("返回登录");
    /**界面初始化*/
    public Register() {
        super("注册");
        this.setLayout(new FlowLayout());
    	label.setSize(480,400);
    	jf.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
    	//透明，流动窗口
    	JPanel pan2 = (JPanel) jf.getContentPane();
    	pan2.setOpaque(false);
    	pan2.setLayout(new FlowLayout());
    	pan2.setLayout(null);
    	/**请输入用户名**/
    	JLabel username = new JLabel("用 户 名 ：");
    	Font font = new Font("宋体", Font.PLAIN, 20);
    	//设置字体
    	username.setFont(font);
    	//设置颜色
    	username.setForeground(Color.BLACK);
    	//请输入用户名
    	username.setBounds(110,50,150,25);
    	//用户名输入框
    	tfAccount.setBounds(210,50,165,25);
    	pan2.add(username);
    	pan2.add(tfAccount);
    	/**请输入密码**/
    	JLabel password = new JLabel("密     码 ：");
    	password.setFont(font);
    	password.setForeground(Color.BLACK);
    	//请输入密码
    	password.setBounds(110,100,180,25);
    	//密码输入框
    	pfPassword.setBounds(210,100,165,25);
    	pan2.add(password);
    	pan2.add(pfPassword);
    	/**请再次输入密码**/
    	JLabel password2 = new JLabel("确认密码：");
    	password2.setFont(font);
    	password2.setForeground(Color.BLACK);
    	//请再次输入密码
    	password2.setBounds(110,150,180,25);
    	//密码输入框
    	pfPassword2.setBounds(210,150,165,25);
    	pan2.add(password2);
    	pan2.add(pfPassword2);
    	//注册按钮
    	btRegister.setBounds(110,250,80,30);
    	pan2.add(btRegister);
    	//返回登录按钮
    	btExit.setBounds(280,250,120,30);
    	pan2.add(btRegister);
    	jf.setLocation(800,300);//初始位置
        jf.setSize(480,400);//窗口大小
        jf.setIconImage(new ImageIcon("src/main/java/img/企鹅2.png").getImage());
        jf.add(btExit);//返回登录按钮
        jf.setResizable(false);//不可编辑
        jf.setVisible(true);//桌面显示
        
        /**增加监听*/
        btRegister.addActionListener(this);//注册按钮监听
        btExit.addActionListener(this);//返回登录按钮监听
    }
    
    /**
     * 当点击注册按钮时，密码相同，密码不为空，用户不重复则会注册成功
     */
    public void actionPerformed(ActionEvent e) {
    	String username=tfAccount.getText();
    	@SuppressWarnings("deprecation")
		String password=pfPassword.getText();
    	try {
			//返回值为1则插入成功
    	if(e.getSource()==btRegister) {
    		//获取连接
			conn = DBUtils.getConn();
			/**************/
			String sql3="alter table user drop id";
			ps=conn.prepareStatement(sql3);
			ps.executeUpdate();
			String sql2="alter table user add id int(11) primary key auto_increment first";
			ps=conn.prepareStatement(sql2);
			ps.executeUpdate();
			/************/
			
			
            String password1=new String(pfPassword.getPassword());
            String password2=new String(pfPassword2.getPassword());
            if(!password1.equals(password2)) {
                JOptionPane.showMessageDialog(this,"两个密码不相同");
                return;
            }else if(password1.equals("") && password2.equals("")){
            	JOptionPane.showMessageDialog(this,"密码不能为空");
            	return;
			}else {
					//编写sql语句
					String sql="insert into user values(null,?,?)";
					//处理sql语句
					ps = conn.prepareStatement(sql);
					ps.setString(1, username);
					ps.setString(2, password);			
					//执行sql语句
					try {
						int rows=ps.executeUpdate();
						if(rows!= 0) {
							JOptionPane.showMessageDialog(this,"注册成功");
							//返回登录页面
							jf.dispose();
							//用来显示/隐藏GUI组件的
							new Landen().init();
						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(this,"用户名已存在");
					}
			}
    }
    if (e.getSource()==btExit) {
    /**当点击返回登录按钮时会返回输出谢谢光临*/
    	JOptionPane.showMessageDialog(this,"谢谢光临");
        //返回登录页面
        jf.dispose();
        new Landen().init();
	}
    }catch (Exception ex) {
    	ex.printStackTrace();
    }finally {
    		DBUtils.close(conn, ps, rs);
    	}
    }
}
 

