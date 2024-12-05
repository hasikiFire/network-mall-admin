# 使用 OpenJDK 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将应用程序的 jar 文件复制到镜像中
COPY target/network-mall-admin.jar app.jar

# 将配置文件复制到容器内
COPY src/main/resources/application-prod.yml /app/config/application-prod.yml


# 暴露应用程序端口
EXPOSE 8080

# 设置环境变量，指定使用生产环境配置
ENV SPRING_PROFILES_ACTIVE=prod

# 启动 Spring Boot 应用程序
CMD ["java", "-Dspring.config.location=classpath:/config/application-prod.yml", "-jar", "app.jar"]
