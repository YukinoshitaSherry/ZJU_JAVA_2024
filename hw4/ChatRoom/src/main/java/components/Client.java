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
/**
 * 客户端
 * @author Allen
 */
public class Client{
//端口
private static final int PORT=1234;
public static String user;
public static Socket socket;

// 在类的开头添加颜色常量
private static final String BLUE_COLOR = "#0000FF";
private static final String GRAY_COLOR = "#808080";
private static final String RED_COLOR = "#FF0000";
private static final String BLACK_COLOR = "#000000";

// 添加一个格式化消息的辅助方法
private String formatColorMessage(String message, String color) {
    return String.format("<font color='%s'>%s</font>", color, message);
}

public Client(String user){
	Client.user=user;
	try{
		//建立socket连接
		socket=new Socket("127.0.0.1",PORT);
		System.out.println("【"+user+"】欢迎来到聊天室！");
		//建立客户端线程
		Thread tt=new Thread(new Recove(socket,user));
		//启动线程
		tt.start();
		}catch(Exception e){
			e.printStackTrace();
			}
	}
public static void main(String[] args) throws Exception{
	new Client(user);
	}
}
class Recove implements Runnable{
	// 将颜色常量定义移到Recove类内部
	private static final Color BLUE = new Color(0, 0, 255);
	private static final Color GRAY = new Color(128, 128, 128);
	private static final Color RED = new Color(255, 0, 0);
	private static final Color BLACK = new Color(0, 0, 0);
	
	// 修改消息追加方法
	private void appendToPane(JTextPane pane, String message, Color color) {
		StyledDocument doc = pane.getStyledDocument();
		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, color);
		
		try {
			doc.insertString(doc.getLength(), message + "\n", attr);
			pane.setCaretPosition(doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public String user;
	private Socket socket;
	public BufferedReader br;
	private String msg;
	Room gm=new Room();
	public Recove(Socket socket,String user) throws IOException{
		try{
			this.socket=socket;
			this.user=user;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while((msg=br.readLine())!=null){
				String message=msg;
				//匹配字符串 显示好友列表
				if(message.equals("好友列表")){
					//接收前清空好友列表
					Room.listModel1.clear();
					//清空JCombox
					Room.jcomb.removeAllItems();
					Room.jcomb.addItem("所有人");
					message=br.readLine();
					//将接收到的所有用户信息分隔开
					String[] str=message.split(":");
					for(String ss:str){
						//将所有用户信息添加到好友列表
						Room.listModel1.addElement(ss);
						//将所有用户信息添加到JCombox
						Room.jcomb.addItem(ss);
					}
				/**私聊*/
				}else if(message.equals("私聊")){
					//读取一行数据
					message=br.readLine();
					//在服务器端显示私聊消息
					System.out.println("收到："+message);
					//在我的频道显示私聊信息
					appendToPane(Room.jta2, message, RED);
				/**显示说话信息*/
				}else if(message.equals("聊天")){
					message=br.readLine();
					//在服务器端显示说话信息
					System.out.println("收到："+message);
					//在公共频道显示说话信息
					appendToPane(Room.jta1, message, BLACK);
					//在我的频道显示说话信息
					appendToPane(Room.jta2, message, BLACK);
				/**显示进入聊天室*/
				}else if(message.equals("进入聊天室")){
					message=br.readLine();
					// 直接使用appendToPane方法和BLUE常量
					appendToPane(Room.jta1, message, BLUE);
					appendToPane(Room.jta2, message, BLUE);
				/**刷新*/
				}else if(message.equals("刷新")){
					//将好友列表清空
					Room.listModel1.clear();
					//将JCombox 清空
					Room.jcomb.removeAllItems();
					Room.jcomb.addItem("所有人");
					message=br.readLine();
					//将接收到的用户信息分隔开
					String[] sr=message.split(":");
					for(String sst:sr){
						//将刷新后所有用户信息添加到好友列表
						Room.listModel1.addElement(sst);
						//将刷新后所有用户信息添加到JCombox
						Room.jcomb.addItem(sst);
						}
				/**下线*/
				}else if(message.equals("下线")){
					message=br.readLine();
					// 使用HTML格式显示灰色的退出消息
					appendToPane(Room.jta1, message, GRAY);
					appendToPane(Room.jta2, message, GRAY);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
