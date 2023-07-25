#!/bin/sh

exec 2>&1

echo "!!Kindly note, you should be in project root directory to execute this script!!"

echo "Compiling Project and creating jars"
mvn clean install -U
echo "Project Compilation should be Success!!"

echo "Creating docker images for Card Auth Server ACS & Mysql Server(used by Card Auth Server ACS)"
docker-compose -f ./scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml up -d

echo "Sleeping for 10 seconds before checking if all dockers are up.."
sleep 10

echo "Creating default schema in mysql db.."
mysql -h 127.0.0.1 -P 5506 -u cas-acs-user -ppassword cas_db < ./card-auth-server-dao/src/main/resources/sql/DDL.sql

echo "Inserting default values in newly created schema"
mysql -h 127.0.0.1 -P 5506 -u cas-acs-user -ppassword cas_db < ./scripts/deployment/dev/dev-database-dml.sql

echo "Checking if Card Auth Server ACS is UP and running.."

TIMEOUT=20
ENDTIME=$(($(date +%s) + TIMEOUT))
SERVER_URL="http://127.0.0.1:8080/actuator/health"

while [ $(date +%s) -lt $ENDTIME ]; do
    STATUS=$(curl -s -o /dev/null -w "%{http_code}" $SERVER_URL)
    if [ $STATUS -eq 200 ]; then
        echo "Server is up at http://127.0.0.1:8080/swagger-ui/index.html !!"
        break
    fi
    sleep 1
done