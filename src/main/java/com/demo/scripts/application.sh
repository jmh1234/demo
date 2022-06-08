#! /bin/bash

# 开始执行部署PN-9900程序
echo -e "\e[1;32m开始部署PN-9900应用服务程序 \e[0m"

HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

cd ../../deploy

# 获取当前路径
CURRENT_PATH=$(pwd)

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

echo -e "\e[1;32m开始执行PN-9900应用脚本 \e[0m"
result=$(sed s/$/"\\\n"/ $CURRENT_PATH/portainer/data/compose/1/docker-compose.yml | tr -d '\n')

echo "curl --location --request PUT -w %{http_code} 'http://127.0.0.1:9001/api/stacks/1?endpointId=1' --header 'Authorization: Bearer ${value}' --header 'Content-Type: application/json' --data '{ \"id\": 1, \"StackFileContent\": \"${result}\",\"Env\": [],\"Prune\": false }'" | sh
if [ $? -eq 0 ]; then
  echo -e "\e[1;32m执行PN-9900应用脚本成功 \e[0m"
else
  echo -e "\e[1;31m执行PN-9900应用脚本失败 \e[0m"
  exit 1
fi

echo -e "\e[1;32m部署PN-9900应用服务成功 \e[0m"
