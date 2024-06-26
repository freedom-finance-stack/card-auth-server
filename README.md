![GitHub release (latest SemVer)](https://img.shields.io/badge/release-v1.0.0-blue)

# Card Auth Server
A **3D Secure Access Control Server** is an authentication protocol designed to add an additional layer of security to online credit and debit card transactions.

The 3D Secure protocol is used by major credit card companies such as Visa, Mastercard, and American Express to provide an additional layer of security for online transactions. The protocol is designed to combat the rising threat of card-not-present (CNP) fraud.

The Access Control Server (ACS) is a software module that enables issuing banks to participate in Verified by Visa, MasterCard SecureCode etc. programs. The ACS functions as oxygen to the 3D Secure protocol, allowing merchants to validate a cardholder's credentials for payment authentication.

EMVCo also plays a role in the 3D Secure Access Control Server (ACS) ecosystem. EMVCo provides test requirements for ACS as a system under test (For detailed information refer official [EMVCo_3DS_Spec_v220](https://docs.3dsecure.io/3dsv2/_downloads/b412903d6e2c99b7828246fa10db5b3e/EMVCo_3DS_Spec_v220.pdf) document).

## Features of ACS

Here are some features of a 3D Secure Access Control Server:

* **Authentication**: Service offers a wide range of features aimed at streamlining authentication and facilitating seamless online transactions. Our 3DS ACS (Authentication Server) supports both 3DS 2.1.0 and 3DS 2.2.0 protocols, ensuring a smooth authentication process.


* **Extensibilty**: Easily pluggable to support advance cardholder verification methods such as Out of Band, OTP (One-Time Password), risk-based authentication, decoupled authentication, and non-payment authentication, you can rest assured that you required minimum code change to enhance security measures during transactions.

* **Ease of deployment**: 
  * Ease of Deployment: Our open-source project prioritizes ease of deployment, ensuring that users can quickly and effortlessly set up the solution in their environment.

  * Versatile Deployment Methods: We support a wide variety of deployment methods to accommodate diverse user preferences and infrastructure requirements.

  * Configurable Docker Image: We have developed a highly configurable Docker image, available in our repository, enabling seamless deployment across different containerized environments.

  * AWS Terraform Script: To further streamline deployment on AWS, we have meticulously crafted Terraform scripts. These scripts facilitate one-click deployment on AWS, provisioning all necessary resources and configuring the architecture for optimal performance and scalability.

## Get started 
#### Prerequisites/Dependencies Required
We need to install below libraries
* Java - `version "17.0.8" 2023-07-18 LTS`
* Maven - `Apache Maven 3.9.5`
* Mysql Server - `8.0.32`
* Java IDE(Intellij/Eclipse etc)

## Dev Setup
### Run Card Server Auth Locally
1. Setup using Script
   * Open the terminal
   * Clone Card Auth Server Repo from Github
     ```
     git clone git@github.com:razorpay/card-auth-server.git
     ```
   * Run the Dev Deployment Script 
     ```
      card-auth-server/scripts/deployment/dev/cas-acs-dev-deployment.sh  
     ```
   * Server must have started. Check by visiting 
      ```
      curl --location 'http://127.0.0.1:8080/actuator/health' 
     ```
  
2. Local setup manually
   * Spin up the SQL database
   * Create a user and password
   * Open the dev-database-dml.sql file in the editor of SQL with above created user and password.
   * Execute the the queries for basic DB setup.
   * Add the username and password in ``` card-auth-server-acs/src/main/resources/application.yml ```
     * ```
       datasource:
       url: jdbc:mysql://${ACS_MYSQL_HOST:<IP ADDRESS>}:${ACS_MYSQL_PORT:3306}/${ACS_MYSQL_DATABASE:cas_db}
       username: ${ACS_MYSQL_USER:<USERNAME>}
       password: ${ACS_MYSQL_PASSWORD:<PASSWORD>}
       driverClassName: com.mysql.cj.jdbc.Driver 
       ```

  * Check out the [guide](docs/Endpoint%20Description.md) for a more comprehensive setup, which includes detailed description about the API endpoints exposed.

---
####  The simplest way to set up Card Auth Server is to create a managed deployment with Dockerfile provided and deploy on VM or k8s.
#### Using Docker Compose Dev
Docker Compose dev is located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).


## Deployment and Configuration
1. To learn more about the bank deployment and configuration read the comprehensive guide here
[Find out more on deployment and configuration](docs/README_DEPLOY.md).

2. Use [DDL](card-auth-server-dao/src/main/resources/sql/DDL.sql) SQL Script for data dump while starting ACS server on production.

## Documentation
For information related to documents of this project, refer to [Docs Readme](docs/README.md)

## How to contribute to the project
For contribution guidelines, see [CONTRIBUTING](docs/CONTRIBUTING.md).


## Our Pledge
We take participation in our community as a harassment-free experience for everyone and we pledge to act in ways to contribute to an open, welcoming, diverse and inclusive community.

If you have experienced or been made aware of unacceptable behaviour, please remember that you can report this. Read our **Code of Conduct**.
