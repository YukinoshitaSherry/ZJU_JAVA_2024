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

    // 数据库连接相关
    public static Connection dbConnection = null;
    public static PreparedStatement dbStatement = null;
    public static ResultSet dbResult = null;

    // 主窗口和面板
    private JFrame registerFrame = new JFrame("注册窗口");
    private JPanel mainPanel;

    // 输入组件
    private JTextField usernameInput = new JTextField(20);
    private JPasswordField passwordInput = new JPasswordField(10);
    private JPasswordField confirmPasswordInput = new JPasswordField(10);

    // 按钮组件
    private JButton registerButton = new JButton("注册");
    private JButton returnButton = new JButton("返回登录");

    public Register() { initializeUI(); }

    private void initializeUI() {
        // 设置背景
        String backgroundPath = "src/main/java/img/register.jpg";
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundPath));
        backgroundLabel.setSize(480, 400);
        registerFrame.getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));

        // 初始化主面板
        mainPanel = (JPanel)registerFrame.getContentPane();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        setupInputFields();
        setupButtons();
        setupWindowProperties();
        addActionListeners();
    }

    private void setupInputFields() {
        // 用户名输入区域
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setBounds(100, 100, 80, 25);
        usernameInput.setBounds(190, 100, 180, 25);

        // 密码输入区域
        JLabel passwordLabel = new JLabel("密码：");
        passwordLabel.setBounds(100, 150, 80, 25);
        passwordInput.setBounds(190, 150, 180, 25);

        // 确认密码区域
        JLabel confirmLabel = new JLabel("确认密码：");
        confirmLabel.setBounds(100, 200, 80, 25);
        confirmPasswordInput.setBounds(190, 200, 180, 25);

        // 添加到面板
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameInput);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordInput);
        mainPanel.add(confirmLabel);
        mainPanel.add(confirmPasswordInput);
    }

    private void setupButtons() {
        registerButton.setBounds(120, 280, 100, 30);
        returnButton.setBounds(250, 280, 100, 30);

        mainPanel.add(registerButton);
        mainPanel.add(returnButton);
    }

    private void setupWindowProperties() {
        registerFrame.setSize(480, 400);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setResizable(false);
        registerFrame.setVisible(true);
    }

    private void addActionListeners() {
        registerButton.addActionListener(this);
        returnButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegistration();
        } else if (e.getSource() == returnButton) {
            registerFrame.dispose();
            new Landen().init();
        }
    }

    private void handleRegistration() {
        String username = usernameInput.getText();
        String password = new String(passwordInput.getPassword());
        String confirmPassword = new String(confirmPasswordInput.getPassword());

        if (validateInput(username, password, confirmPassword)) {
            registerUser(username, password);
        }
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请填写完整信息！");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "两次密码输入不一致！");
            return false;
        }
        return true;
    }

    private void registerUser(String username, String password) {
        try {
            // 数据库操作代码...
            // 这里保持原有的数据库注册逻辑不变
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "注册失败：" + ex.getMessage());
        }
    }
}
