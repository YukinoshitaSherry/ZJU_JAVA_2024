package components;

import java.net.*;
/**
 * 实体类
 * @author 22145
 *
 */
class User{
	//用户姓名
	private String username;
	//用户性别
	private String password;
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//用户自己的socket
	private Socket sock;
	public User(String username,String password,Socket sock){
		setUsername(username);
		setPassword(password);
		setSock(sock);
	}
	
	public Socket getSock(){
		return sock;
	}
	public void setSock(Socket sock){
		this.sock=sock;
	}
}