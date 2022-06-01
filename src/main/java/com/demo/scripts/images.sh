#! /bin/bash

echo -e "\e[1;32m开始加载镜像 \e[0m"


HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

tar -zxvf images.tar.gz -C $HOME

pwd

# 加载镜像
# 1. 安装mysql
docker load -i images/mysql.tar
# 2. 安装mysql-nacos
docker load -i images/nacos-mysql.tar
# 3. 安装redis
docker load -i images/redis.tar
# 4. 安装emqx
docker load -i images/emqx.tar
# 5. 安装nacos
docker load -i images/nacos.tar
# 6. 安装nginx
docker load -i images/nginx.tar
# 7. 安装portainer
docker load -i images/portainer.tar
# 8. 安装minio
docker load -i images/minio.tar
# 9. 安装linxjre
docker load -i images/linxjre.tar
# 10. 安装openjdk
docker load -i images/openjdk.tar

# 删除中间文件
rm -rf images/

echo -e "\e[1;32m加载镜像成功 \e[0m"
