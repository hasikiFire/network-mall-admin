#!/bin/bash

# 设置变量
REPO_URL="https://github.com/hasikiFire/network-mall-admin.git"
PROJECT_NAME="network-mall-admin"
HOST_PORT=29999
CONFIG_FILE="./application-prod.yml" # 生产环境配置文件的路径

# Step 1: 克隆代码库
echo ">> 克隆代码库..."
rm -rf $PROJECT_NAME
git clone "$REPO_URL"

# Step 2：将 application-prod.yml 复制到项目的 resources 目录中
echo ">> 复制生产环境配置文件到项目目录..."
cp $CONFIG_FILE ./$PROJECT_NAME/src/main/resources/application-prod.yml

# Step 3: 构建并启动服务
echo ">> 使用 Docker Compose 构建和启动服务..."
cd "$PROJECT_NAME"
docker-compose down || true
docker-compose up --build -d

# 检查容器是否运行
if docker ps | grep -q "$PROJECT_NAME"; then
    echo ">> 部署成功！应用程序正在运行在 http://localhost:$HOST_PORT"
else
    echo ">> 部署失败！检查 Docker 日志以获取更多信息。"
    exit 1
fi
