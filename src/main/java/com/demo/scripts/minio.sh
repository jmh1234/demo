#! /bin/bash

echo -e "\e[1;32m开始部署minio依赖服务程序 \e[0m"

# 获取认证结果
resultJWT=$(curl --location --request POST 'http://127.0.0.1:9001/api/auth' --header 'Content-Type: text/plain' --data '{  "password": "Pn123456",  "username": "admin"}')

# 解析json
parse_json() {
  echo "${1//\"/}" | sed "s/.*$2:\([^,}]*\).*/\1/"
}

# 获取token认证
value=$(parse_json $resultJWT "jwt")
echo $value

echo -e "\e[1;32m开始执行minio脚本 \e[0m"
# 获取compose.yml文件内容并转义换行符
result3=$(sed s/$/"\\\n"/ /njpn/PN-9900/deploy/portainer/data/compose/3/docker-compose.yml | tr -d '\n')

# 替换变量中的引号，否则portainer启动失败
result=${result3//\"/\\\"}

echo "curl --location --request PUT -w %{http_code} 'http://127.0.0.1:9001/api/stacks/3?endpointId=1' --header 'Authorization: Bearer ${value}' --header 'Content-Type: application/json' --data '{ \"id\": 3, \"StackFileContent\": \"${result}\",\"Env\": [],\"Prune\": false }'" | sh
if [ $? -eq 0 ]; then
  echo -e "\e[1;32m执行minio脚本成功 \e[0m"
else
  echo -e "\e[1;31m执行minio脚本失败 \e[0m"
  exit 1
fi

sleep 60

HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd ${HOME}

tar -zcvf minio/minio_certs.tar.gz -C ../../deploy/minio/
tar -zxvf minio/ossbucket.tar.gz -C ../../deploy/minio/data/

echo -e "\e[1;32m部署minio依赖服务程序成功 \e[0m"
