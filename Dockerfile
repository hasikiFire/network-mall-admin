# 使用 Maven 构建阶段
FROM maven:3.8.5-openjdk-17 AS builder

# 设置工作目录
WORKDIR /app

# 先复制 pom.xml 文件
COPY pom.xml .

# 下载 Maven 依赖
RUN mvn dependency:go-offline

# 复制剩余的应用源代码
COPY src ./src

# 打包应用
RUN mvn clean package -DskipTests

# 使用 OpenJDK 运行阶段
FROM openjdk:17-jdk-slim AS app

# 设置工作目录
WORKDIR /app

# 将构建的应用复制到新镜像
COPY --from=builder /app/target/network-mall-admin.jar /app/app.jar

RUN mkdir -p /app/logs

# 添加持久化卷声明
VOLUME /app/logs

# 暴露应用程序端口
EXPOSE 8080

# 定义构建参数
ARG SPRING_PROFILES_ACTIVE=test

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# 启动 Spring Boot 应用程序
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]