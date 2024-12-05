#!/bin/bash

# 设置变量
REPO_URL="https://github.com/hasikiFire/network-mall-admin"
PROJECT_NAME="network-mall-admin"
JAR_NAME="network-mall-admin.jar"
DOCKER_IMAGE="network-mall-admin:latest"
DOCKER_CONTAINER="network-mall-admin"
HOST_PORT=29999
PORT=8080
CONFIG_FILE="/application-prod.yml" # 生产环境配置文件的路径

# Step 1: 克隆代码库
echo ">> 克隆代码库..."
if [ -d "$PROJECT_NAME" ]; then
    echo ">> 目录已存在，更新代码库..."
    cd "$PROJECT_NAME"
    git pull
else
    git clone "$REPO_URL"
    cd "$PROJECT_NAME" || exit
fi

# 将 application-prod.yml 复制到项目的 resources 目录中
echo ">> 复制生产环境配置文件到项目目录..."
cp $CONFIG_FILE /$PROJECT_NAME/src/main/resources/application-prod.yml

# Step 2: 使用 Maven 构建项目
echo ">> 使用 Maven 构建项目..."
mvn clean package -DskipTests

# 检查构建是否成功
if [ ! -f "target/$JAR_NAME" ]; then
    echo ">> 构建失败！检查日志以获取更多信息。"
    exit 1
fi

# Step 3: 构建 Docker 镜像
echo ">> 构建 Docker 镜像..."

docker build -t "$DOCKER_IMAGE" .

# Step 4: 启动容器
echo ">> 启动 Docker 容器..."
docker stop "$DOCKER_CONTAINER" 2>/dev/null || true
docker rm "$DOCKER_CONTAINER" 2>/dev/null || true

docker run -d --name "$DOCKER_CONTAINER" -p "$HOST_PORT:$PORT" "$DOCKER_IMAGE"

# 检查容器是否运行
if docker ps | grep -q "$DOCKER_CONTAINER"; then
    echo ">> 部署成功！应用程序正在运行在 http://localhost:$HOST_PORT"
else
    echo ">> 部署失败！检查 Docker 日志以获取更多信息。"
    exit 1
fi
