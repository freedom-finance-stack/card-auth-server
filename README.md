![GitHub release (latest SemVer)](https://img.shields.io/badge/release-v1.0.0-blue)
# Card Auth Server

Card Auth Server(Freedom Finance Stack Initiative) is an open source project for Access Control Server.

## Features

* CNP(Card Not Present) transactions in India.
* Logs
* Application Monitoring Module - Integrate and add multiple monitoring providers
* Deployment using Docker Containers

\... and more!

To learn more about Card Auth Server’s features and capabilities, see our [product page](https://razorpay.com/).

## Get started 
* The simplest way to set up Card Auth Server is to create a managed deployment with Dockerfile provided and deploy on VM or k8s.

### Run Card Server Auth Locally
* Spin up the SQL database
* Create a user and password
* Open the dev-database-dml.sql file in the editor of SQL.
* Execute the the queries for basic DB setup.
* Add the username and password in ``` card-auth-server-acs/src/main/resources/application.yml ```
* ```
  datasource:
  url: jdbc:mysql://${ACS_MYSQL_HOST:<IP ADDRESS>}:${ACS_MYSQL_PORT:3306}/${ACS_MYSQL_DATABASE:cas_db}
  username: ${ACS_MYSQL_USER:<USERNAME>}
  password: ${ACS_MYSQL_PASSWORD:<PASSWORD>}
  driverClassName: com.mysql.cj.jdbc.Driver 
  ```

#### Prerequisites
We need to install below libraries
* Java - `version "17.0.8" 2023-07-18 LTS`
* Maven - `Apache Maven 3.9.5`
* Mysql Server - `8.0.32`
* Java IDE(Intellij/Eclipse etc)

#### Using Docker Compose Dev
Docker Compose dev is located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).

You can start Card Auth Server ACS following below steps.
* Checkout Card Auth Server Repo
    ```
    git clone git@github.com:razorpay/card-auth-server.git
    ```
* Go to Card Auth Server Repo directory
    ``` 
    cd card-auth-server
    ```
* Create java jar binaries via
    ```
    mvn clean install -U
    ```
* Go to docker compose file directory
    ```
    cd ./scripts/deployment/dockerconf/card-auth-server/
    ```
* Run below command
    ```
    docker-compose -f docker-compose-dev.yaml up
    ```
* Check if server is up
    ```
    curl --location 'http://127.0.0.1:8080/actuator/health'
    ```

#### [Find out more on deployment](docs/README_DEPLOY.md)

## Documentation
For information related to documents of this project, refer to [Docs Readme](docs/README.md)

## How to contribute to the project
For contribution guidelines, see [CONTRIBUTING](CONTRIBUTING.md).

## Our Pledge
We take participation in our community as a harassment-free experience for everyone and we pledge to act in ways to contribute to an open, welcoming, diverse and inclusive community.

If you have experienced or been made aware of unacceptable behaviour, please remember that you can report this. Read our [Code of Conduct](CODE_OF_CONDUCT).