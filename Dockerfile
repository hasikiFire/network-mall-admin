# 构建阶段
FROM maven:3.9.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 运行阶段
# 使用 OpenJDK 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 jar 文件到运行阶段
COPY --from=builder /app/target/network-mall-admin.jar app.jar

# 将配置文件复制到容器内
COPY src/main/resources/application-prod.yml /app/config/application-prod.yml


# 暴露应用程序端口
EXPOSE 8080

# 设置环境变量，指定使用生产环境配置
ENV SPRING_PROFILES_ACTIVE=prod

# 启动 Spring Boot 应用程序
CMD ["java", "-Dspring.config.location=classpath:/config/application-prod.yml", "-jar", "app.jar"]
