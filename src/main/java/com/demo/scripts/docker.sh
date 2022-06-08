#! /bin/bash

# 安装docker
echo -e "\e[1;32m开始解压docker镜像 \e[0m"

HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

tar -zxvf docker/docker-19.03.8.tgz
cp docker/docker/* /usr/bin/
cp docker/docker.service /etc/systemd/system/docker.service
chmod +x /etc/systemd/system/docker.service
# 加载docker守护线程
systemctl daemon-reload
# 启动并加入开机启动
systemctl start docker
systemctl enable docker.service
# 重启docker
systemctl restart docker
# 删除中间文件
rm -rf docker/docker

# 验证docker是否安装成功
docker version
if [ $? -eq 0 ]; then
  echo -e "\e[1;32m离线安装docker成功 \e[0m"
else
  echo -e "\e[1;31m离线安装docker失败 \e[0m"
  exit 1
fi
