package components;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
/**
 * 聊天室大厅
 * @author 22145
 */
public class Room extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	// 主窗口
	JFrame mainFrame = new JFrame("聊天室");
	// 客户端连接相关
	public Client clientSocket;
	public PrintWriter writer;
	
	// 面板组件
	public JPanel userInfoPanel = new JPanel();    // 原jp1
	public JPanel messagePanel = new JPanel();     // 原jp2
	public JPanel friendListPanel = new JPanel();  // 原jp3
	public JPanel chatPanel = new JPanel();        // 原jp4
	public JPanel mainChatPanel = new JPanel();    // 原jp5
	public JPanel onlineFriendsPanel = new JPanel(); // 原jp6
	public JPanel mainContentPanel = new JPanel();   // 原jp7
	
	// 聊天区域
	public static JTextPane mainChatArea = new JTextPane();  // 原jta1
	public static JTextPane privateChatArea = new JTextPane(); // 原jta2
	
	// 发送相关组件
	public JLabel toLabel = new JLabel("对");      // 原jl1
	public static JComboBox<String> receiverSelect = new JComboBox<>(); // 原jcomb
	public JTextArea messageInput = new JTextArea(8, 20);    // 原jtf
	public JButton sendButton = new JButton("发送>>");       // 原jb1
	public JButton refreshButton = new JButton("刷新");      // 原jb2
	
	// 好友列表相关
	public static DefaultListModel<Object> friendsListModel = new DefaultListModel<>(); // 原listModel1
	public static JList<Object> friendsList = new JList<>(friendsListModel);  // 原lst1
	
	// 用户信息
	public String username;           // 原na
	public String gender;            // 原se
	public String currentMessage;    // 原message
	
	// 个人信息面板组件
	public JPanel personalInfoPanel = new JPanel();  // 原jpUserInfo
	public JLabel personalInfoTitle = new JLabel("个人信息"); // 原lblUserTitle
	public JLabel usernameLabel = new JLabel();      // 原lblUsername
	public JLabel genderLabel = new JLabel();        // 原lblGender
	public JLabel noFriendsLabel = new JLabel("目前暂时无好友在线", SwingConstants.CENTER); // 原lblNoFriends
	public JButton exitButton = new JButton("退出");
/**显示聊天界面*/
public void getMenu(String name, String sex) {
	//添加全部聊天
	receiverSelect.addItem("所有人");
	this.username = name;
	this.gender = sex;
	
	//设置文本框不可编辑
	mainChatArea.setEditable(false);
	privateChatArea.setEditable(false);
	
	// 确保发送消息的文本框可编辑
	messageInput.setEditable(true);
	messageInput.setEnabled(true);
	
	// 设置窗口基本属性
	mainFrame.setSize(1400, 800);
	mainFrame.setLocationRelativeTo(null);  // 居中显示
	mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	mainFrame.setResizable(false);
	
	// 配置左侧面板
	JPanel jpLeft = new JPanel();
	jpLeft.setLayout(new BorderLayout());
	
	// 个人信息面板
	personalInfoPanel.setLayout(new GridLayout(2, 1, 5, 5));
	personalInfoPanel.setBorder(new TitledBorder("个人信息"));
	usernameLabel.setText("账号: " + name);
	genderLabel.setText("性别: " + sex);
	personalInfoPanel.add(usernameLabel);
	personalInfoPanel.add(genderLabel);
	
	// 发送消息面板
	JPanel jpSend = new JPanel();
	jpSend.setLayout(new BoxLayout(jpSend, BoxLayout.Y_AXIS));
	jpSend.setBorder(new TitledBorder("发送消息"));
	
	// 选择发送对象面板，使用FlowLayout让组件紧凑排列
	JPanel jpSelect = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));  // 减小间距
	jpSelect.add(toLabel);
	jpSelect.add(receiverSelect);
	receiverSelect.setPreferredSize(new Dimension(150, 25));
	
	// 文本框面板
	JPanel jpText = new JPanel(new BorderLayout());
	JScrollPane textScrollPane = new JScrollPane(messageInput);
	textScrollPane.setPreferredSize(new Dimension(200, 100));
	jpText.add(textScrollPane, BorderLayout.CENTER);
	
	// 在发送消息面板中添加按钮面板
	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
	sendButton.setPreferredSize(new Dimension(100, 30));  // 发送按钮
	refreshButton.setPreferredSize(new Dimension(80, 30));   // 刷新按钮
	exitButton.setPreferredSize(new Dimension(80, 30));  // 退出按钮

	buttonPanel.add(sendButton);
	buttonPanel.add(Box.createHorizontalStrut(5));
	buttonPanel.add(refreshButton);
	buttonPanel.add(Box.createHorizontalStrut(5));
	buttonPanel.add(exitButton);

	// 组装面板，减小垂直间距
	jpSend.add(jpSelect);
	jpSend.add(Box.createVerticalStrut(2));  // 减小间距
	jpSend.add(jpText);
	jpSend.add(Box.createVerticalStrut(5));
	jpSend.add(buttonPanel);  // 添加按钮面板
	
	// 组装左侧面板
	jpLeft.add(personalInfoPanel, BorderLayout.NORTH);
	jpLeft.add(jpSend, BorderLayout.CENTER);
	
	// 设置聊天区域和好友列表面板的布局
	mainChatPanel.setLayout(new BorderLayout());  // 改用BorderLayout
	onlineFriendsPanel.setLayout(new BorderLayout());  // 改用BorderLayout

	// 设置滚动面板，让它填充整个区域
	JScrollPane mainChatScroll = new JScrollPane(mainChatArea);
	JScrollPane privateChatScroll = new JScrollPane(privateChatArea);
	JScrollPane friendsListScroll = new JScrollPane(friendsList);

	// 设置边框
	mainChatScroll.setBorder(BorderFactory.createTitledBorder("主聊天频道"));
	privateChatScroll.setBorder(BorderFactory.createTitledBorder("我的频道"));
	friendsListScroll.setBorder(BorderFactory.createTitledBorder("在线好友列表"));

	// 组装面板
	userInfoPanel.setLayout(new GridLayout(2, 1, 0, 0));  // 移除间距
	userInfoPanel.add(mainChatScroll);
	userInfoPanel.add(privateChatScroll);

	// 将面板添加到各自的容器中，使用BorderLayout.CENTER让它们填充整个空间
	mainChatPanel.add(userInfoPanel, BorderLayout.CENTER);
	onlineFriendsPanel.add(friendsListScroll, BorderLayout.CENTER);
	
	// 设置各面板大小
	jpLeft.setPreferredSize(new Dimension(280, 750));
	mainChatPanel.setPreferredSize(new Dimension(900, 750));
	onlineFriendsPanel.setPreferredSize(new Dimension(270, 750));
	
	// 设置主面板布局，移除面板间距
	mainContentPanel.setLayout(new BorderLayout(0, 0));  // 将间距为0
	mainContentPanel.add(jpLeft, BorderLayout.WEST);
	mainContentPanel.add(mainChatPanel, BorderLayout.CENTER);
	mainContentPanel.add(onlineFriendsPanel, BorderLayout.EAST);
	
	// 移除边框和内边距
	mainChatPanel.setBorder(null);
	onlineFriendsPanel.setBorder(null);
	mainContentPanel.setBorder(null);

	// 设置滚动面板的边框样式更简洁
	mainChatScroll.setBorder(BorderFactory.createTitledBorder(
	    BorderFactory.createLineBorder(Color.LIGHT_GRAY), "主聊天频道"));
	privateChatScroll.setBorder(BorderFactory.createTitledBorder(
	    BorderFactory.createLineBorder(Color.LIGHT_GRAY), "我的频道"));
	friendsListScroll.setBorder(BorderFactory.createTitledBorder(
	    BorderFactory.createLineBorder(Color.LIGHT_GRAY), "在线好友列表"));

	
	// 添加到主窗口并显示
	mainFrame.add(mainContentPanel);
	mainFrame.setVisible(true);  // 置窗口可
	
	// 添加按钮监听器
	sendButton.addActionListener(this);
	refreshButton.addActionListener(this);
	// 添加退出按钮监听器
	exitButton.addActionListener(this);
}
/**启动聊天室口*/
public void sock(){
	try{
		//将用户信息保存成字符串形式
		String user=username+"("+gender+")";
		//创建客户端对象
		clientSocket=new Client(user);
		//创建输出流
		writer=new PrintWriter(Client.socket.getOutputStream());
		//发送好友列表标识
		writer.println("好友列表");
		//发送用户信息
		writer.println(username+":"+gender);
		//将内存中的数据次性输出
		writer.flush();
		//发送进入聊天室标识
		writer.println("进入聊天室");
		//发送进入聊天室信息
		writer.println("【"+username+"】"+"进入聊天室");
		//将内存中的数据一次性输出
		writer.flush();
	}catch(Exception e){
		e.printStackTrace();
	}
}
/**设置窗口关闭事件，如果点击窗口右上角叉号关闭，执行下边程序*/
public Room() {
	mainFrame.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			try {
				//创建输出流
				writer=new PrintWriter(Client.socket.getOutputStream());
				//发送下线标识
				writer.println("下线");
				//发送下线信息
				writer.println(username+":离开聊天室");
				//将内存中的数据一次性输出
				writer.flush();
				mainFrame.dispose();//关闭窗口
			}catch(Exception ex) {
				
			}
		}
	});
}
/**事件触发*/
public void actionPerformed(ActionEvent event){
	//获取发送，刷新按钮
	sendButton.setText("发送>>");
	refreshButton.setText("刷新");
	try{
		//创建输出流
		writer=new PrintWriter(Client.socket.getOutputStream());
		//点击发送触发
		if(event.getActionCommand().equals("发送>>")){
			if(!messageInput.getText().equals("")){
				String selectedUser = (String)receiverSelect.getSelectedItem();
				if(!"所有人".equals(selectedUser)){
					// 私聊
					currentMessage = username + "(" + gender + ")" + "对" + selectedUser + "说：" + messageInput.getText();
					writer.println("私聊");
					writer.println(username + ":" + selectedUser + "分开" + currentMessage);
					writer.flush();
				} else {
					// 群聊
					writer.println("聊天");
					writer.println(username + "说：" + messageInput.getText());
					writer.flush();
				}
			}
		//点击刷新触发
		}else if(event.getActionCommand().equals("刷新")){
			//创建输出流
			writer=new PrintWriter(Client.socket.getOutputStream());
			//发送刷新识
			writer.println("刷新");
			//将内存中数据一次性输出
			writer.flush();
			}
		// 处理退出按钮
		if (event.getSource() == exitButton) {
			// 发送下线消息
			writer.println("下线");
			writer.println(username + ":离开聊天室");
			writer.flush();
			// 关闭窗口
			mainFrame.dispose();
			return;
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	//空输栏信息
	messageInput.setText("");
	//输入焦点
	messageInput.requestFocus();
	}
}