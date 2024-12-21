package components;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/* 客户端 */
public class Client {
    // 端口
    private static final int PORT = 1234;
    public static String user;
    public static Socket socket;

    // 在类的开头添加颜色常量
    private static final String BLUE_COLOR = "#0000FF";
    private static final String GRAY_COLOR = "#808080";
    private static final String RED_COLOR = "#FF0000";
    private static final String BLACK_COLOR = "#000000";

    // 添加一个格式化消息的辅助方法
    private String formatColorMessage(String message, String color) { return String.format("<font color='%s'>%s</font>", color, message); }

    public Client(String user) {
        Client.user = user;
        try {
            // 建立socket连接
            socket = new Socket("127.0.0.1", PORT);
            System.out.println("【" + user + "】欢迎来到聊天室！");
            // 建立客户端线程
            Thread tt = new Thread(new Recove(socket, user));
            // 启动线程
            tt.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception { new Client(user); }
}
class Recove implements Runnable {
    // 颜色常量
    private static final Color MESSAGE_COLOR_BLUE = new Color(0, 0, 255);
    private static final Color MESSAGE_COLOR_GRAY = new Color(128, 128, 128);
    private static final Color MESSAGE_COLOR_RED = new Color(255, 0, 0);
    private static final Color MESSAGE_COLOR_BLACK = new Color(0, 0, 0);

    public String currentUser;
    private Socket clientSocket;
    public BufferedReader reader;
    private String receivedMessage;
    Room chatRoom = new Room();

    // 修改消息显示方法
    private void displayMessage(JTextPane chatArea, String message, Color color) {
        StyledDocument doc = chatArea.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, color);

        try {
            doc.insertString(doc.getLength(), message + "\n", attr);
            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public Recove(Socket socket, String user) throws IOException {
        try {
            this.clientSocket = socket;
            this.currentUser = user;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((receivedMessage = reader.readLine()) != null) {
                String message = receivedMessage;

                if (message.equals("好友列表")) {
                    Room.friendsListModel.clear();
                    Room.receiverSelect.removeAllItems();
                    Room.receiverSelect.addItem("所有人");
                    message = reader.readLine();
                    String[] users = message.split(":");
                    for (String user : users) {
                        Room.friendsListModel.addElement(user);
                        Room.receiverSelect.addItem(user);
                    }
                } else if (message.equals("私聊")) {
                    message = reader.readLine();
                    System.out.println("收到：" + message);
                    displayMessage(Room.mainChatArea, message, MESSAGE_COLOR_RED);
                    displayMessage(Room.privateChatArea, message, MESSAGE_COLOR_RED);
                } else if (message.equals("聊天")) {
                    message = reader.readLine();
                    System.out.println("收到：" + message);
                    displayMessage(Room.mainChatArea, message, MESSAGE_COLOR_BLACK);
                    displayMessage(Room.privateChatArea, message, MESSAGE_COLOR_BLACK);
                } else if (message.equals("进入聊天室")) {
                    message = reader.readLine();
                    displayMessage(Room.mainChatArea, message, MESSAGE_COLOR_BLUE);
                    displayMessage(Room.privateChatArea, message, MESSAGE_COLOR_BLUE);
                } else if (message.equals("刷新")) {
                    Room.friendsListModel.clear();
                    Room.receiverSelect.removeAllItems();
                    Room.receiverSelect.addItem("所有人");
                    message = reader.readLine();
                    String[] users = message.split(":");
                    for (String user : users) {
                        Room.friendsListModel.addElement(user);
                        Room.receiverSelect.addItem(user);
                    }
                } else if (message.equals("下线")) {
                    message = reader.readLine();
                    displayMessage(Room.mainChatArea, message, MESSAGE_COLOR_GRAY);
                    displayMessage(Room.privateChatArea, message, MESSAGE_COLOR_GRAY);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
