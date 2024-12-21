package components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * 客户端
 * @author Allen
 */
public class Client{
//端口
private static final int PORT=1234;
public static String user;
public static Socket socket;
private String message;
private String na;

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
	public String user;
	private Socket socket;
	public BufferedReader br;
	private String msg;
	Room gm=new Room();
	private String username;
	
	public Recove(Socket socket,String user) throws IOException{
		try{
			this.socket=socket;
			this.user=user;
			this.username = user.substring(0, user.indexOf("("));
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
					boolean hasOnlineFriends = false;
					for(String ss:str){
						//不显示自己
						if(!ss.equals(user)) {
							Room.listModel1.addElement(ss);
							Room.jcomb.addItem(ss);
							hasOnlineFriends = true;
						}
					}
					//如果没有其他在线用户,显示提示信息
					if(!hasOnlineFriends) {
						Room.listModel1.addElement("目前暂时无好友在线");
					}
				/**私聊*/
				}else if(message.equals("私聊")){
					message = br.readLine();
					System.out.println("收到：" + message);
					// 解析消息格式，直接显��完整消息
					Room.jta2.append(message + "\n\n");
				/**显示说话消息*/
				}else if(message.equals("聊天")){
					message = br.readLine();
					System.out.println("收到：" + message);
					// 直接显示完整消息
					Room.jta1.append(message + "\n\n");
					Room.jta2.append(message + "\n\n");
				/**显示进入聊天室*/
				}else if(message.equals("进入聊天室")){
					message = br.readLine();
					// 直接显示完整消息
					Room.jta1.append(message + "\n\n");
					Room.jta2.append(message + "\n\n");
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
					Room.jta1.append(message + "\n\n");
					Room.jta2.append(message + "\n\n");
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
