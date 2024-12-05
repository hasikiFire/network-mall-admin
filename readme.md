# 启动项目

1. 添加 application-dev.yml
2. mvn clean package
3. mvn spring-boot:run
4. 访问 http://localhost:8080/swagger-ui/index.html#/ 文档地址！

# 部署项目

1. 添加 application-prod.yml 文件到服务器任意目录 a
2. 复制 deploy.sh 到服务器目录 a 下并执行 
3. sudo chmod +x deploy.sh 
4. sudo ./deploy.sh