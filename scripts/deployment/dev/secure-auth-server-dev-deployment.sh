#!/bin/sh

exec 2>&1

echo "!!Kindly note, you should be in project root directory to execute this script!!"

echo "Compiling Project and creating jars"
mvn clean compile; mvn clean install -U
echo "Project Compilation should be Success!!"

cd ./scripts/deployment/dockerconf/secure-auth-server/ || exit

echo "Creating docker images for Secure Auth Server & Mysql Server(used by Secure Auth Server)"
docker-compose -f docker-compose-dev.yaml up -d

echo "Check if Secure Auth Server is UP and running.."
curl --location 'http://127.0.0.1:8080/actuator/health'