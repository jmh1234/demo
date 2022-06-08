#! /bin/bash

# 部署Portainer
echo -e "\e[1;32m开始部署Portainer \e[0m"

HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

cd ../../deploy

# 获取当前路径
CURRENT_PATH=$(pwd)

# 执行指令
docker run -d -p 9001:9000 --restart=always --name portainer -v /var/run/docker.sock:/var/run/docker.sock -v $CURRENT_PATH/portainer/data:/data portainer/portainer

cd ${HOME}

# 拷贝portainer文件
tar -zxvf portainer/portainer.tar.gz -C $CURRENT_PATH

# 拷贝nginx文件
tar -zxvf nginx/nginx.tar.gz -C $CURRENT_PATH

# 拷贝mysql_cnf文件
#tar -zxvf mysql/mysql_cnf.tar.gz -C $CURRENT_PATH

# 拷贝nginx_mqtt文件
tar -zxvf nginx/nginx_mqtt.tar.gz -C $CURRENT_PATH

# 拷贝nginx_cluster文件
tar -zxvf nginx/nginx_cluster.tar.gz -C $CURRENT_PATH

# 拷贝emqx文件
tar -zxvf emqx/emqx.tar.gz -C $CURRENT_PATH

sleep 20

cd ${HOME}

# 生成新的compose文件
# 1.1. 基础应用compose转化
sh portainer/baseYmlConvert.sh
if [ $? -ne 0 ]; then
	echo "base docker compose yml convert failed."
	endTime_s=`date +%s`
	echo "deploy failed, elapsed:"$[ $endTime_s - $startTime_s ]" sec."
	exit 1
fi

# 1.2. 平台服务compose转化
sh portainer/platformYmlConvert.sh
if [ $? -ne 0 ]; then
	echo "platform docker compose yml convert failed."
	endTime_s=`date +%s`
	echo "deploy failed, elapsed:"$[ $endTime_s - $startTime_s ]" sec."
	exit 1
fi

# 1.3. 应用服务compose转化
sh portainer/applicationYmlConvert.sh
if [ $? -ne 0 ]; then
	echo "application docker compose yml convert failed."
	endTime_s=`date +%s`
	echo "deploy failed, elapsed:"$[ $endTime_s - $startTime_s ]" sec."
	exit 1
fi

# 1.4. minio compose转化
sh portainer/minioYmlConvert.sh
if [ $? -ne 0 ]; then
	echo "minio docker compose yml convert failed."
	endTime_s=`date +%s`
	echo "deploy failed, elapsed:"$[ $endTime_s - $startTime_s ]" sec."
	exit 1
fi

sleep 15

docker restart portainer

echo -e "\e[1;32m部署Portainer成功 \e[0m"
