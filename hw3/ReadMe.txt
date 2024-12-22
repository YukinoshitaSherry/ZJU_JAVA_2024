# 在项目根目录(SearchEngine)下执行
# 清理并编译项目
mvn clean install

# 打包项目（这将创建包含所有依赖的 JAR 文件）
mvn package

# 运行
java -jar target/SearchEngine-1.0-SNAPSHOT-jar-with-dependencies.jar


爬取网站：
https://www.runoob.com/java/java-tutorial.html