#! /bin/bash

# 部署Portainer
echo -e "\e[1;32m开始部署Portainer \e[0m"

# 执行指令
docker run -d -p 9001:9000 --restart=always --name portainer -v /var/run/docker.sock:/var/run/docker.sock -v /njpn/PN-9900/deploy/portainer/data:/data portainer/portainer

# 拷贝portainer文件
tar -zxvf /njpn/PN-9900/install/portainer.tar.gz -C /njpn/PN-9900/deploy/

# 拷贝nginx文件
tar -zxvf /njpn/PN-9900/install/nginx.tar.gz -C /njpn/PN-9900/deploy/

# 拷贝mysql_cnf文件
tar -zxvf /njpn/PN-9900/install/mysql_cnf.tar.gz -C /njpn/PN-9900/deploy/

# 拷贝nginx_mqtt文件
tar -zxvf /njpn/PN-9900/install/nginx_mqtt.tar.gz -C /njpn/PN-9900/deploy/

# 拷贝nginx_cluster文件
tar -zxvf /njpn/PN-9900/install/nginx_cluster.tar.gz -C /njpn/PN-9900/deploy/

# 拷贝emqx文件
tar -zxvf /njpn/PN-9900/install/emqx.tar.gz -C /njpn/PN-9900/deploy/

echo -e "\e[1;32m部署Portainer成功 \e[0m"