#! /bin/bash

# 开始执行部署PN-9900程序
echo -e "\e[1;32m开始部署PN-9900基础依赖服务程序 \e[0m"

# 获取认证结果
resultJWT=$(curl --location --request POST 'http://127.0.0.1:9001/api/auth' --header 'Content-Type: text/plain' --data '{  "password": "Pn123456",  "username": "admin"}')
echo $resultJWT

# 解析json
parse_json() {
  echo "${1//\"/}" | sed "s/.*$2:\([^,}]*\).*/\1/"
}

# 获取token认证
value=$(parse_json $resultJWT "jwt")
# 获取compose.yml文件内容并转义换行符

echo -e "\e[1;32m开始执行base脚本 \e[0m"
result6=$(sed s/$/"\\\n"/ /njpn/PN-9900/deploy/portainer/data/compose/6/docker-compose.yml | tr -d '\n')

# 替换变量中的引号，否则portainer启动失败
result=${result6//\"/\\\"}

echo "curl --location --request PUT -w %{http_code} 'http://127.0.0.1:9001/api/stacks/6?endpointId=1' --header 'Authorization: Bearer ${value}' --header 'Content-Type: application/json' --data '{ \"id\": 6, \"StackFileContent\": \"${result}\",\"Env\": [],\"Prune\": false }'" | sh
if [ $? -eq 0 ]; then
  echo -e "\e[1;32m执行base脚本成功 \e[0m"
else
  echo -e "\e[1;31m执行base脚本失败 \e[0m"
  exit 1
fi

sleep 60

HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

# 恢复nacos配置的数据
echo -e "\e[1;32m恢复nacos配置的数据 \e[0m"
docker cp ../sql/nacos.sql nacos-mysql:/tmp/nacos.sql
docker exec nacos-mysql bash -c "/usr/bin/mysql -hlocalhost -P3306 -unacos -pnacos nacos_devtest < /tmp/nacos.sql"
docker exec nacos-mysql rm -rf /tmp/nacos.sql

sleep 60

# 恢复mysql数据
echo -e "\e[1;32m开始恢复pn9900数据 \e[0m"
docker cp ../sql/pn_9900.sql mysql:/tmp/pn_9900.sql
docker exec mysql bash -c "/usr/bin/mysql -hlocalhost -P5236 -uroot -pPn123456 -f pn9900 < /tmp/pn_9900.sql"
docker exec mysql rm -rf /tmp/pn_9900.sql
echo -e "\e[1;32mpn9900数据恢复成功 \e[0m"

# 防止nacos读不到mysql配置,60秒后重启nacos
sleep 60
docker restart nacos

echo -e "\e[1;32m部署PN-9900基础依赖服务程序成功 \e[0m"
