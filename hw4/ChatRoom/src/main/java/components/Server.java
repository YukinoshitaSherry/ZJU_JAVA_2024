package components;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 服务器
 * @author 22145
 */
public class Server extends Frame implements ActionListener {
    private static final long serialVersionUID = 1L;
    // 定义图片位置
    String bg10 = "src/main/java/img/register.jpg";
    String start = "src/main/java/img/start.gif";
    // 启动服务器窗口
    JFrame serverFrame = new JFrame();
    // 服务器监听窗口
    JFrame statusFrame = new JFrame();
    // 将图片添加到标签里
    ImageIcon bg = new ImageIcon(bg10);
    JLabel label = new JLabel(bg);
    // 将监听背景图片加入窗口
    ImageIcon start1 = new ImageIcon(start);
    JLabel label2 = new JLabel(start1);
    // 端口输入框
    JTextField jtf = new JTextField(20);
    // 定义启动服务器按钮
    JButton jb = new JButton("启动服务器");
    JButton jb2 = new JButton("关闭服务器");
    // 设置服务器端口
    private static final int PORT = 1234;
    // 创建G_Menu对象
    Room gm = new Room();
    // 声明变量
    private ServerSocket server;
    // 创建打印集
    public ArrayList<PrintWriter> list;
    public static String user;
    // 定义用户集合
    public static ArrayList<User> list1 = new ArrayList<User>();
    public User uu;
    public Server(String user) { Server.user = user; }
    /**服务器启动监听**/
    public void startdemo() {
        // 设置窗口标题
        statusFrame.setTitle("服务器监听中...");

        // 设置窗口尺寸
        int frameWidth = 480;
        int frameHeight = 320;

        // 加载ZJU2图标
        ImageIcon originalIcon = new ImageIcon("src/main/java/img/ZJU2.jpg");
        // 设置图标大小为100x100
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);

        // 创建提示文字
        JLabel textLabel = new JLabel("请启动客户端");
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("宋体", Font.BOLD, 16));

        // 设置内容面板
        JPanel contentPane = (JPanel)statusFrame.getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);

        // 计算图标位置使其居中
        int iconX = (frameWidth - 100) / 2;
        int iconY = (frameHeight - 150) / 2; // 预留文字空间
        iconLabel.setBounds(iconX, iconY, 100, 100);

        // 计算文字位置使其居中
        int textWidth = 150;
        int textHeight = 30;
        int textX = (frameWidth - textWidth) / 2;
        int textY = iconY + 110; // 图标下方10像素
        textLabel.setBounds(textX, textY, textWidth, textHeight);

        // 添加组件
        contentPane.add(iconLabel);
        contentPane.add(textLabel);

        // 设置窗口属性
        statusFrame.setSize(frameWidth, frameHeight);
        statusFrame.setLocationRelativeTo(null);
        statusFrame.setResizable(false);
        statusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 添加窗口图标
        statusFrame.setIconImage(new ImageIcon("src/main/java/img/ZJU1.png").getImage());

        // 显示窗口
        statusFrame.setVisible(true);
    }
    /**显示启动服端页面**/
    public void initdemo() {
        // 启动背景
        String inputbg = "src/main/java/img/register.jpg";
        ImageIcon bg = new ImageIcon(inputbg);
        label.setIcon(bg);
        label.setSize(800, 600);
        serverFrame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        // 其他代码保持不变
        JPanel mainPanel = (JPanel)serverFrame.getContentPane();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new FlowLayout());
        serverFrame.setLayout(null);

        /**端口**/
        JLabel start = new JLabel("端口号：");
        Font f = new Font("宋体", Font.PLAIN, 20);
        // 设置字体
        start.setFont(f);
        // 设置颜色
        start.setForeground(Color.BLACK);
        start.setBounds(180, 100, 80, 25);
        jtf.setBounds(260, 100, 40, 30);
        mainPanel.add(start);
        mainPanel.add(jtf);
        jtf.setText("8088");
        /**启动按钮**/
        jb.setBounds(180, 160, 120, 50);
        // 添加退出按钮
        JButton exitButton = new JButton("退出");
        exitButton.setBounds(320, 160, 120, 50);
        mainPanel.add(exitButton);
        mainPanel.add(jb);
        // 设置默认显示位置
        serverFrame.setLocation(800, 300); // 初始位置
        // 设置聊天登录框大小
        serverFrame.setSize(800, 600);
        // 设置此窗体是否可由用户调整大小。
        serverFrame.setResizable(false);
        // 关闭聊天登录框时自动关闭程序
        serverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 将聊天登录框在windows桌面显示
        serverFrame.setVisible(true);
        // 添加图标
        serverFrame.setIconImage(new ImageIcon("src/main/java/img/ZJU1.png").getImage());
        // 添加监听
        jb.addActionListener(this); // 启动服务器按钮
        exitButton.addActionListener(this);
    }
    public static void main(String[] args) { new Server(user).initdemo(); }
    class Chat implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private String currentMessage;
        private String messageBuffer = "";

        public Chat(Socket socket) {
            try {
                this.clientSocket = socket;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while ((currentMessage = reader.readLine()) != null) {
                    // 匹配字符串 显示好友列表
                    if (currentMessage.equals("好友列表")) {
                        currentMessage = reader.readLine();
                        // 将用户信息跟消息分隔开
                        String[] userInfo = currentMessage.split(":");
                        // 将用户信息添加到User对象中
                        uu = new User(userInfo[0], userInfo[1], clientSocket);
                        // 将对象添加到用户集合
                        list1.add(uu);
                        // 遍历用户集合
                        Iterator<User> it = Server.list1.iterator();
                        while (it.hasNext()) {
                            User use = it.next();
                            currentMessage = use.getUsername() + "(" + use.getPassword() + "):";
                            // 将所有的用户信息连接成一个字符串
                            messageBuffer += currentMessage;
                        }
                        // 显示好友列表匹配标识
                        sendMessage("好友列表");
                        // 群发消息
                        sendMessage(messageBuffer);
                        // 显示说话消息
                    } else if (currentMessage.equals("聊天")) {
                        currentMessage = reader.readLine();
                        System.out.println(currentMessage);
                        // 显示说话信息匹配标识
                        sendMessage("聊天");
                        sendMessage(currentMessage);
                        // 显示进入聊天室
                    } else if (currentMessage.equals("进入聊天室")) {
                        currentMessage = reader.readLine();
                        System.out.println(currentMessage);
                        // 进入聊天室匹配标识
                        sendMessage("进入聊天室");
                        sendMessage(currentMessage);
                        // 私聊
                    } else if (currentMessage.equals("私聊")) {
                        currentMessage = reader.readLine();
                        // 把传进来的用户信息跟说话内容分开
                        String[] privateInfo = currentMessage.split("分开");
                        // 在服务器端显示说话内容
                        System.out.println(privateInfo[1]);
                        // 因为是私聊，传过来两个用户的用户信息，这句作用是再把两个用户信息分开
                        String[] targetInfo = privateInfo[0].split(":");
                        // 遍历用户集合
                        Iterator<User> iu = Server.list1.iterator();
                        while (iu.hasNext()) {
                            User se = iu.next();
                            // 如果传进来的用户信息跟集合中的用户信息吻合
                            if (targetInfo[1].equals(se.getUsername() + "(" + se.getPassword() + ")")) {
                                try {
                                    // 建立用户自己的流
                                    PrintWriter pwriter = new PrintWriter(se.getSock().getOutputStream());
                                    // 匹配标识
                                    pwriter.println("私聊");
                                    // 向单独用户发送消息
                                    pwriter.println(privateInfo[1]);
                                    pwriter.flush();
                                    System.out.println(privateInfo[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 如果传进来的用户信息跟集合中的用户信息吻合
                            } else if (targetInfo[0].equals(se.getUsername())) {
                                try {
                                    PrintWriter pwr = new PrintWriter(se.getSock().getOutputStream()); // 建立用自己的流
                                    // 匹标识
                                    pwr.println("私聊");
                                    // 向单独用户发送消息
                                    pwr.println(privateInfo[1]);
                                    pwr.flush();
                                    System.out.println(privateInfo[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // 下线
                    } else if (currentMessage.equals("下线")) {
                        currentMessage = reader.readLine();
                        // 在服务端显示信息
                        System.out.println(currentMessage);
                        // 匹配字符串
                        sendMessage("下线");
                        // 匹配完毕后群发消息
                        sendMessage(currentMessage);
                        // 将传过来的同户名跟信息分隔开
                        String[] si = currentMessage.split(":");
                        // 遍历用户集合
                        Iterator<User> at = Server.list1.iterator();
                        while (at.hasNext()) {
                            User sr = at.next();
                            // 如果传过来的用户名跟用户集合里的用户吻合
                            if (sr.getUsername().equals(si[0])) {
                                // 将吻合的用户移除
                                list1.remove(sr);
                                // 关闭此用户的socket
                                sr.getSock().close();
                            }
                        }
                        break;
                        // 刷新
                    } else if (currentMessage.equals("刷新")) {
                        String mssge = "";
                        // 遍历户集合
                        Iterator<User> iter = Server.list1.iterator();
                        while (iter.hasNext()) {
                            User uus = iter.next();
                            currentMessage = uus.getUsername() + "(" + uus.getPassword() + "):";
                            // 将所有的用户信息连接成一个字符串
                            mssge += currentMessage;
                        }
                        // 发送刷新配标识
                        sendMessage("刷新");
                        // 群发消息
                        sendMessage(mssge);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 群发消息方法
    public void sendMessage(String message) {
        try {
            // 输出流集合
            for (PrintWriter pw : list) {
                pw.println(message);
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**启动服务器**/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            try {
                // 初始化共享集合
                list = new ArrayList<PrintWriter>();

                // 启动监听窗口
                startdemo();

                // 隐藏启动窗口
                serverFrame.setVisible(false);

                // 启动服务器监听线程
                new Thread(() -> {
                    try {
                        server = new ServerSocket(PORT);
                        System.out.println("服务器启动成功，监听端口: " + PORT);

                        while (true) {
                            Socket client = server.accept();
                            PrintWriter writer = new PrintWriter(client.getOutputStream());
                            list.add(writer);
                            new Thread(new Chat(client)).start();
                        }
                    } catch (IOException ex) {
                        if (!server.isClosed()) {
                            ex.printStackTrace();
                        }
                    }
                }).start();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == jb2) {
            try {
                if (server != null && !server.isClosed()) {
                    server.close();
                }
                statusFrame.dispose();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}