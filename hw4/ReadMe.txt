# 先运行服务端
java -cp target/ChatRoom-1.0-SNAPSHOT.jar components.Server

# 再运行客户端（从Login开始）
java -cp target/ChatRoom-1.0-SNAPSHOT.jar components.Login


mvn clean install