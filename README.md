# Secure Auth Server

Secure Auth Server(Freedom Finance Stack Initiative) is an open source project for Access Control Server.

## Features

* CNP(Card Not Present) transactions in India.
* Logs
* Application Monitoring Module - Integrate and add multiple monitoring providers
* Deployment using Docker Containers

\... and more!

To learn more about Secure Auth Server’s features and capabilities, see our [product page](https://razorpay.com/).

## Get started
The simplest way to set up Secure Auth Server is to create a managed deployment with Dockerfile provided and deploy on VM or k8s.

### Run Secure Server Auth Locally

#### Prerequiste
We need to install below libraries
* Java - `version "11.0.16" 2022-07-19 LTS`
* Maven - `Apache Maven 3.8.6`
* Mysql Server - `8.0.32`
* Java IDE(Intellij/Eclipse etc)

#### Using Docker Compose Dev
Docker Compose dev is located [here](https://github.com/razorpay/secure-auth-server/blob/master/scripts/deployment/dockerconf/secure-auth-server/docker-compose-dev.yaml).

You can start Secure Auth ACS Server following below steps.
* Checkout Secure Auth Server Repo
    ```
    git checkout git@github.com:razorpay/secure-auth-server.git
    ```
* Go to Secure Auth Server Repo directory
    ``` 
    cd secure-auth-server
    ```
* Create java jar binaries via
    ```
    mvn clean compile
    mvn clean install -U
    ```
* Go to docker compose file directory
    ```
    cd secure-auth-server/scripts/deployment/dockerconf/secure-auth-server/
    ```
* Run below command
    ```
    docker-compose -f docker-compose-dev.yaml up
    ```
* Check if server is up
    ```
    curl --location 'http://127.0.0.1:8080/actuator/health'
    ```

#### Using Built-In Automated Shell Script
* Go to Secure Auth Server Repo directory
    ``` 
    cd secure-auth-server
    ```
* Run below command
    ```
    sh secure-auth-server/scripts/deployment/dev/secure-auth-server-dev-deployment.sh
    ```

## Documentation
For information related to documents of this project, refer to [Docs Readme](docs/README.md)

## Contribute
For contribution guidelines, see [CONTRIBUTING](CONTRIBUTING.md).
