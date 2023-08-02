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
The simplest way to set up Card Auth Server is to create a managed deployment with Dockerfile provided and deploy on VM or k8s.

### Run Card Server Auth Locally

#### Prerequiste
We need to install below libraries
* Java - `version "11.0.16" 2022-07-19 LTS`
* Maven - `Apache Maven 3.8.6`
* Mysql Server - `8.0.32`
* Java IDE(Intellij/Eclipse etc)

#### Using Docker Compose Dev
Docker Compose dev is located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).

You can start Card Auth Server ACS following below steps.
* Checkout Card Auth Server Repo
    ```
    git checkout git@github.com:razorpay/card-auth-server.git
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

## Contribute
For contribution guidelines, see [CONTRIBUTING](CONTRIBUTING.md).
