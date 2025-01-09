#!/bin/bash

# 设置变量
REPO_URL="https://github.com/hasikiFire/network-mall-admin.git"
PROJECT_NAME="network-mall-admin"
HOST_PORT=29999
DEFAULT_CONFIG_FILE="./application-test.yml" # 默认配置文件（测试环境）
PROD_CONFIG_FILE="./application-prod.yml"   # 生产环境配置文件

# 检查传入参数
if [ "$1" == "prod" ]; then
    CONFIG_FILE=$PROD_CONFIG_FILE
    SPRING_PROFILES_ACTIVE="prod"
    echo ">> 使用生产环境配置文件: $CONFIG_FILE"
else
    CONFIG_FILE=$DEFAULT_CONFIG_FILE
    SPRING_PROFILES_ACTIVE="test"
    echo ">> 使用测试环境配置文件: $CONFIG_FILE"
fi

# Step 1: 克隆代码库
echo ">> 克隆代码库..."
rm -rf $PROJECT_NAME
git clone "$REPO_URL"

# Step 2：将配置文件复制到项目的 resources 目录中
echo ">> 复制配置文件到项目目录..."
cp $CONFIG_FILE ./$PROJECT_NAME/src/main/resources/application-prod.yml

# Step 3: 构建 Docker 镜像
echo ">> 构建 Docker 镜像..."
cd "$PROJECT_NAME"
docker build --build-arg SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE -t network-mall-admin-$SPRING_PROFILES_ACTIVE .

# Step 4: 启动 Docker 容器
echo ">> 启动 Docker 容器..."
export SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE # 设置环境变量
docker-compose down || true
docker-compose up -d

# 检查容器是否运行
if docker ps | grep -q "network-mall-admin-$SPRING_PROFILES_ACTIVE"; then
    echo ">> 部署成功！应用程序正在运行在 http://localhost:$HOST_PORT"
else
    echo ">> 部署失败！检查 Docker 日志以获取更多信息。"
    exit 1
fi